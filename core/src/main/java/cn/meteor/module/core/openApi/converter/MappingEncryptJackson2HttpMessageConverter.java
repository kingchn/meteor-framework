package cn.meteor.module.core.openApi.converter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.util.TypeUtils;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.PrettyPrinter;
import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ser.FilterProvider;

public class MappingEncryptJackson2HttpMessageConverter extends AbstractJackson2HttpMessageConverter {

	private String jsonPrefix;


	/**
	 * Construct a new {@link MappingJackson2HttpMessageConverter} using default configuration
	 * provided by {@link Jackson2ObjectMapperBuilder}.
	 */
	public MappingEncryptJackson2HttpMessageConverter() {
		this(Jackson2ObjectMapperBuilder.json().build());
	}

	/**
	 * Construct a new {@link MappingJackson2HttpMessageConverter} with a custom {@link ObjectMapper}.
	 * You can use {@link Jackson2ObjectMapperBuilder} to build it easily.
	 * @see Jackson2ObjectMapperBuilder#json()
	 */
	public MappingEncryptJackson2HttpMessageConverter(ObjectMapper objectMapper) {
		super(objectMapper, MediaType.APPLICATION_JSON, new MediaType("application", "*+json"));
	}

	/**
	 * Specify a custom prefix to use for this view's JSON output.
	 * Default is none.
	 * @see #setPrefixJson
	 */
	public void setJsonPrefix(String jsonPrefix) {
		this.jsonPrefix = jsonPrefix;
	}

	/**
	 * Indicate whether the JSON output by this view should be prefixed with ")]}', ". Default is false.
	 * <p>Prefixing the JSON string in this manner is used to help prevent JSON Hijacking.
	 * The prefix renders the string syntactically invalid as a script so that it cannot be hijacked.
	 * This prefix should be stripped before parsing the string as JSON.
	 * @see #setJsonPrefix
	 */
	public void setPrefixJson(boolean prefixJson) {
		this.jsonPrefix = (prefixJson ? ")]}', " : null);
	}


	@Override
	protected void writePrefix(JsonGenerator generator, Object object) throws IOException {
		if (this.jsonPrefix != null) {
			generator.writeRaw(this.jsonPrefix);
		}
		String jsonpFunction =
				(object instanceof MappingJacksonValue ? ((MappingJacksonValue) object).getJsonpFunction() : null);
		if (jsonpFunction != null) {
			generator.writeRaw("/**/");
			generator.writeRaw(jsonpFunction + "(");
		}
	}

	@Override
	protected void writeSuffix(JsonGenerator generator, Object object) throws IOException {
		String jsonpFunction =
				(object instanceof MappingJacksonValue ? ((MappingJacksonValue) object).getJsonpFunction() : null);
		if (jsonpFunction != null) {
			generator.writeRaw(");");
		}
	}
	
	private static final MediaType TEXT_EVENT_STREAM = new MediaType("text", "event-stream");
	private PrettyPrinter ssePrettyPrinter;
	
	protected void init(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
		setDefaultCharset(DEFAULT_CHARSET);
		DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
		prettyPrinter.indentObjectsWith(new DefaultIndenter("  ", "\ndata:"));
		this.ssePrettyPrinter = prettyPrinter;
	}
	
	/* (non-Javadoc)
	 * @see org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter#writeInternal(java.lang.Object, java.lang.reflect.Type, org.springframework.http.HttpOutputMessage)
	 */
	@Override
	protected void writeInternal(Object object, Type type, HttpOutputMessage outputMessage)
			throws IOException, HttpMessageNotWritableException {

		MediaType contentType = outputMessage.getHeaders().getContentType();
		JsonEncoding encoding = getJsonEncoding(contentType);
//		JsonGenerator generator = this.objectMapper.getFactory().createGenerator(outputMessage.getBody(), encoding);
		ByteArrayOutputStream baosm = new ByteArrayOutputStream();
		JsonGenerator generator = this.objectMapper.getFactory().createGenerator(baosm, encoding);
		try {
			writePrefix(generator, object);

			Class<?> serializationView = null;
			FilterProvider filters = null;
			Object value = object;
			JavaType javaType = null;
			if (object instanceof MappingJacksonValue) {
				MappingJacksonValue container = (MappingJacksonValue) object;
				value = container.getValue();
				serializationView = container.getSerializationView();
				filters = container.getFilters();				
			}
			if (type != null && value != null && TypeUtils.isAssignable(type, value.getClass())) {
				javaType = getJavaType(type, null);
			}
			ObjectWriter objectWriter;
			if (serializationView != null) {
				objectWriter = this.objectMapper.writerWithView(serializationView);
			}
			else if (filters != null) {
				objectWriter = this.objectMapper.writer(filters);
			}
			else {
				objectWriter = this.objectMapper.writer();
			}
			if (javaType != null && javaType.isContainerType()) {
				objectWriter = objectWriter.forType(javaType);
			}

			SerializationConfig config = objectWriter.getConfig();
			if (contentType != null && contentType.isCompatibleWith(TEXT_EVENT_STREAM) &&
					config.isEnabled(SerializationFeature.INDENT_OUTPUT)) {
				objectWriter = objectWriter.with(this.ssePrettyPrinter);
			}

			
			//获取要加密的对象
			Object cipherObject = null;			
			Method getCipherObjectMethod = null;
			try {				
				getCipherObjectMethod = value.getClass().getDeclaredMethod("getCipherObject");
			} catch (Exception e) {
					try {
						getCipherObjectMethod = value.getClass().getSuperclass().getDeclaredMethod("getCipherObject");
					} catch (Exception e1) {
					}
			}
			if(getCipherObjectMethod!=null) {
				try {
					cipherObject = getCipherObjectMethod.invoke(value);
				} catch (Exception e) {
					logger.error(e);
				}
			} else {
				logger.error("enc_json==>调用response的getCipherObject方法出错");
			}	
			
			
//			ByteArrayOutputStream baosmForCipherText = new ByteArrayOutputStream();
//			JsonGenerator generatorForCipherText = this.objectMapper.getFactory().createGenerator(baosmForCipherText, encoding);
//			writePrefix(generatorForCipherText, cipherObject);
//			objectWriter.writeValue(generatorForCipherText, cipherObject);
//			writeSuffix(generatorForCipherText, cipherObject);
//			generatorForCipherText.flush();
//			byte[] bytesForCipherText = baosmForCipherText.toByteArray();
//			String ciphertext = new String(bytesForCipherText);
//			ciphertext = ciphertext+"ddd";
			
			//要加密的json不要自动换行
			objectMapper.configure(SerializationFeature.INDENT_OUTPUT, false);
			String cipherJson = objectMapper.writeValueAsString(cipherObject);
			
			//执行response定义的加密方法，并获取密文
			Object cipherText = null;
			Method getEncryptMethod = null;
			try {				
				getEncryptMethod = value.getClass().getDeclaredMethod("encrypt", String.class);				
			} catch (Exception e) {
					try {
						getEncryptMethod = value.getClass().getSuperclass().getDeclaredMethod("encrypt", String.class);
					} catch (Exception e1) {
					}
			}
			if(getEncryptMethod!=null) {
				try {
					cipherText = getEncryptMethod.invoke(value, cipherJson);
				} catch (Exception e) {
					logger.error(e);
				}
			} else {
				logger.error("enc_json==>调用response的encrypt方法出错");
			}
			
			//将密文设置到response中
			Method setCiphertextMethod = null;
			try {				
				setCiphertextMethod = value.getClass().getDeclaredMethod("setCipherText", String.class);				
			} catch (Exception e) {
					try {
						setCiphertextMethod = value.getClass().getSuperclass().getDeclaredMethod("setCipherText", String.class);
					} catch (Exception e1) {
					}
			}
			if(setCiphertextMethod!=null) {
				try {
					setCiphertextMethod.invoke(value, cipherText);
				} catch (Exception e) {
					logger.error(e);
				}
			} else {
				logger.error("enc_json==>调用response的setCipherText方法出错");
			}
			
			
			if(config.isEnabled(SerializationFeature.INDENT_OUTPUT)) {
				objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
			}
			
			objectWriter.writeValue(generator, value);
			
			writeSuffix(generator, value);
			generator.flush();
			byte[] bytes = baosm.toByteArray();
			String jsonString = new String(bytes);
			outputMessage.getBody().write(jsonString.getBytes());		

		}
		catch (JsonProcessingException ex) {
			throw new HttpMessageNotWritableException("Could not write content: " + ex.getMessage(), ex);
		}
	}

}


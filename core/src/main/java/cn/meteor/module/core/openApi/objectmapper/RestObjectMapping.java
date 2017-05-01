package cn.meteor.module.core.openApi.objectmapper;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;

import cn.meteor.module.core.openApi.annotation.RestJacksonAnnotationIntrospector;

public class RestObjectMapping extends ObjectMapper {

	/**
	* 
	*/
	private static final long serialVersionUID = -8076874164249288040L;

	public RestObjectMapping() {
		super();
		// 允许单引号
		this.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
		// 字段和值都加引号
		this.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
		// 数字也加引号
//		this.configure(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS, true);
		this.configure(JsonGenerator.Feature.QUOTE_NON_NUMERIC_NUMBERS, true);
		// 启用环绕根元素
		this.configure(SerializationFeature.WRAP_ROOT_VALUE, true);//是否环绕根元素，默认false，如果为true，则默认以类名作为根元素，你也可以通过@JsonRootName来自定义根元素名称
//		this.configure(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES, true);
//		this.setPropertyNamingStrategy(new PropertyNamingStrategy.LowerCaseStrategy());
//		// 定义属性命名规则
//		this.setPropertyNamingStrategy(new PropertyNamingStrategy.SnakeCaseStrategy());
		// 自定义注解内省机制, 可控制根元素命名规则
		this.setAnnotationIntrospector(new RestJacksonAnnotationIntrospector());
//		this.setAnnotationIntrospector(new RestJacksonAnnotationIntrospector(RestJacksonAnnotationIntrospector.NamingStrategy.SNAKE_CASE));
		
//		val introspectorPair = new AnnotationIntrospector.Pair(
//				  new JacksonAnnotationIntrospector(),
//				  new JaxbAnnotationIntrospector()
//				)
//				mapper.getSerializationConfig().withAnnotationIntrospector(introspectorPair)

		
		// 空值处理为空串
		this.getSerializerProvider().setNullValueSerializer(new JsonSerializer<Object>() {

			@Override
			public void serialize(Object value, JsonGenerator jg, SerializerProvider sp) throws IOException, JsonProcessingException {
				jg.writeString("");
			}
		});

	}
}  

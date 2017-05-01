package cn.meteor.module.core.openApi.objectmapper.jsonserializer;

import java.io.IOException;

import javax.xml.stream.XMLStreamException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ctc.wstx.sw.BaseStreamWriter;
import com.ctc.wstx.sw.RepairingNsStreamWriter;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;


public class CDATASerializer<T> extends JsonSerializer<T> {
	
	
//	public CDATASerializer() {
//		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
//	}
	
	
	@Autowired
	@Qualifier("objectMapperForXml")
	private ObjectMapper objectMapper;
	
	@Override
	public void serialize(T value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {		
//		ObjectMapper objectMapper = messageConverter.getObjectMapper();
//		XmlMapper xmlMapper = (XmlMapper) objectMapper;
//		xmlMapper.configure(com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator.Feature.WRITE_XML_1_1, true);
//		String objectValue = objectMapper.writeValueAsString(value);
		String objectValue = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(value);
		
		gen.writeStartObject();		
		Object object=gen.getOutputTarget();
		if(object instanceof BaseStreamWriter) {
//			RepairingNsStreamWriter writer = (RepairingNsStreamWriter) object;
			BaseStreamWriter writer = (BaseStreamWriter) object;
			try {
				writer.writeCData(objectValue);
			} catch (XMLStreamException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		gen.writeEndObject();
	}

}

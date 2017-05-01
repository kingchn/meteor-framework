package cn.meteor.module.core.openApi.objectmapper.jsonserializer;

import java.io.IOException;

import org.springframework.data.domain.Page;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import cn.meteor.module.core.openApi.DataTablesOutput;

public class DataTablesResponseJsonSerializer extends JsonSerializer<Page> {

	@Override
	public void serialize(Page value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
//		gen.writeObjectField("data", value.getContent());
		gen.writeObject(value.getContent());
		
	}

//	@Override
//	public void serialize(DataTablesOutput value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
////		gen.writeString("");
//		if(value.getHasSerialized()==false) {
//			gen.writeObject(value);
//			value.setHasSerialized(true);
//		}
//	}

}

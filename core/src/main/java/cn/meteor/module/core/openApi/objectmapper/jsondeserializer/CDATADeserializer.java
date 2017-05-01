package cn.meteor.module.core.openApi.objectmapper.jsondeserializer;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.springframework.http.converter.json.Jackson2ObjectMapperFactoryBean;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;


public class CDATADeserializer<T> extends JsonDeserializer<T> {

	@Override
	public T deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		Jackson2ObjectMapperFactoryBean jackson2ObjectMapperFactoryBean = new Jackson2ObjectMapperFactoryBean();
		jackson2ObjectMapperFactoryBean.setCreateXmlMapper(true);
		jackson2ObjectMapperFactoryBean.afterPropertiesSet();
		ObjectMapper objectMapper = jackson2ObjectMapperFactoryBean.getObject();
		
		String content = p.getText();
		
		 //1.得到该泛型类的子类对象的Class对象
        Class clz = this.getClass();
        //2.得到子类对象的泛型父类类型（也就是BaseDaoImpl<T>）
        ParameterizedType type = (ParameterizedType) clz.getGenericSuperclass();
        Type[] types = type.getActualTypeArguments();        
        Class <T> entityClass = (Class<T>) types[0];
//        System.out.println(entityClass.getSimpleName());		
//		Class <T> entityClass = (Class <T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
		T t = objectMapper.readValue(content, entityClass);
		return t;
	}

}

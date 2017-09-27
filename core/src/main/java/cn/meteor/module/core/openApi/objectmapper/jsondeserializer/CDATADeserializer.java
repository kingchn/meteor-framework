package cn.meteor.module.core.openApi.objectmapper.jsondeserializer;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.json.Jackson2ObjectMapperFactoryBean;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;


public class CDATADeserializer<T> extends JsonDeserializer<T> {
	
	private static final Logger logger = LoggerFactory.getLogger(CDATADeserializer.class);

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
		try {
			T t = objectMapper.readValue(content, entityClass);
			return t;
		} catch (Exception e) {//当CDATADeserializer如果反序列化时，尝试设置到该实体的content字段中，以便再别处进行解码
			T bean = null;
			try {
				bean = entityClass.newInstance();
				Method translateFieldNameMethod = entityClass.getDeclaredMethod("setContent", String.class);
				translateFieldNameMethod.invoke(bean, content);//调用该类的字段名称转换方法得到字段名称
				return bean;
			} catch (Exception e1) {
				logger.error("执行CDATADeserializer setContent时出错", e1);
			}
			return bean;
		}
	}

}

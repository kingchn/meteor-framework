package cn.meteor.module.core.openApi.objectmapper;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.FatalBeanException;
import org.springframework.http.converter.json.Jackson2ObjectMapperFactoryBean;

import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;

/**
 * @author shenjc
 *
 *
 *
 *
						<util:constant static-field="com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_SINGLE_QUOTES" /><!-- 允许单引号 -->
						<util:constant static-field="com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES" /><!-- 字段和值都加引号 -->
						<util:constant static-field="com.fasterxml.jackson.core.JsonGenerator.Feature.QUOTE_NON_NUMERIC_NUMBERS" /><!-- 数字也加引号 -->
 */
public class RestJackson2ObjectMapperFactoryBean extends Jackson2ObjectMapperFactoryBean {
	
	/**
	 * 空值序列器
	 */
	private JsonSerializer<Object> nullValueSerializer;
	
	public JsonSerializer<Object> getNullValueSerializer() {
		return nullValueSerializer;
	}

	public void setNullValueSerializer(JsonSerializer<Object> ser) {
		this.nullValueSerializer = ser;
	}
	
	private final Map<Object, Boolean> xmlFeatures = new HashMap<Object, Boolean>();
	
	public void setXmlFeaturesToEnable(Object... xmlFeaturesToEnable) {
		if (xmlFeaturesToEnable != null) {
			for (Object feature : xmlFeaturesToEnable) {
				this.xmlFeatures.put(feature, Boolean.TRUE);
			}
		}
	}
	
	public void setXmlFeaturesToDisable(Object... xmlFeaturesToDisable) {
		if (xmlFeaturesToDisable != null) {
			for (Object feature : xmlFeaturesToDisable) {
				this.xmlFeatures.put(feature, Boolean.FALSE);
			}
		}
	}
	
	//org.springframework.http.converter.json.Jackson2ObjectMapperBuilder.configureFeature(ObjectMapper, Object, boolean)
	private void configureXmlFeature(XmlMapper xmlMapper, Object feature, boolean enabled) {
		if (feature instanceof ToXmlGenerator.Feature) {
			xmlMapper.configure((ToXmlGenerator.Feature) feature, enabled);
		}
		else {
			throw new FatalBeanException("Unknown feature class: " + feature.getClass().getName());
		}
	}
	
	@Override
	public void afterPropertiesSet() {//afterPropertiesSet之后objectMapper才有值，即this.getObject()才有值，
		super.afterPropertiesSet();
		if(nullValueSerializer!=null) {//如果空值序列器有设置对象，则通过SerializerProvider来设置空值序列化处理
			this.getObject().getSerializerProvider().setNullValueSerializer(nullValueSerializer);
		}
//		this.getObject().setSerializationInclusion(Include.NON_NULL);
		if(this.getObject() instanceof XmlMapper) {//Jackson2ObjectMapperBuilder没有xml特性支持，这里新增xml特性支持
			XmlMapper xmlMapper = (XmlMapper) this.getObject();
			for (Object feature : this.xmlFeatures.keySet()) {
				configureXmlFeature(xmlMapper, feature, this.xmlFeatures.get(feature));
			}
		}
	}
}

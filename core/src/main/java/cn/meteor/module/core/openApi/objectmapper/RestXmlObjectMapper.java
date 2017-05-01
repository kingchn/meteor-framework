package cn.meteor.module.core.openApi.objectmapper;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

public class RestXmlObjectMapper extends XmlMapper {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6763202267808674416L;
	
	public RestXmlObjectMapper() {
		this.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
	}

}

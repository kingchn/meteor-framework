package cn.meteor.module.core.openApi.objectmapper.jsonserializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * url前缀附加序列器
 * @author shenjc
 *
 */
public abstract class JsonUrlPrefixAbstractSerializer extends JsonSerializer<String> {
	
//	private String prefixUrl;
//
//	public JsonUrlPrefixSerializer(String prefixUrl){
//	    this.prefixUrl = prefixUrl;
//	}
	
	protected abstract String getPrefixUrl();

    @Override 
    public void serialize(String path, JsonGenerator gen, SerializerProvider provider) 
            throws IOException, JsonProcessingException {
    	String url=path;
    	String prefixUrl = getPrefixUrl();
    	if(prefixUrl!=null&&!"".equals(prefixUrl)) {
    		if(path!=null&&!"".equals(path)&&!path.startsWith("http://")) {
        		url = prefixUrl + "/" + path;
        	}
    	}
        
        gen.writeString(url);
    } 

} 

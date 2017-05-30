package cn.meteor.module.core.openApi.objectmapper.jsonserializer;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * url前缀附加序列器
 * @author shenjc
 * JaxbUrlSerializer已经不需要了，因为JsonUrlSerializer兼容json和xml
 */
public class JaxbUrlPrefixSerializer extends XmlAdapter<String, String>{
    @Override 
    public String marshal(String path) throws Exception {
//    	String staticUrl=PropertiesMaps.getUrlProperties().get("url.static");
    	String staticUrl="http://localhost:8092";
    	String url=path;
    	if(staticUrl!=null&&!"".equals(staticUrl)) {
    		if(path!=null&&!"".equals(path)&&!path.startsWith("http://")) {
        		url = staticUrl + "/" + path;
        	}
    	}
        return url;
    }
    @Override 
    public String unmarshal(String url) throws Exception {
//    	String staticUrl=PropertiesMaps.getUrlProperties().get("url.static");
    	String staticUrl="http://localhost:8092";
    	String path=url;
    	if(url.startsWith(staticUrl)) {
    		path = url.substring(staticUrl.length(), url.length());
    	}
        return path;
    } 
}

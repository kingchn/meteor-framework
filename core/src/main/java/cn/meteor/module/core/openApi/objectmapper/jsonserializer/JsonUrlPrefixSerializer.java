package cn.meteor.module.core.openApi.objectmapper.jsonserializer;

public class JsonUrlPrefixSerializer extends JsonUrlPrefixAbstractSerializer {
	
	private String prefixUrl;
	
	public JsonUrlPrefixSerializer(){
		this.prefixUrl = "http://localhost:8092";
	}

	public JsonUrlPrefixSerializer(String prefixUrl){
	    this.prefixUrl = prefixUrl;
	}

	@Override
	protected String getPrefixUrl() {
		
		return prefixUrl;
	}

}

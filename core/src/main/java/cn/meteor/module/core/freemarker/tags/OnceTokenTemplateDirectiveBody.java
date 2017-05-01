package cn.meteor.module.core.freemarker.tags;

import java.io.IOException;
import java.io.Writer;

import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;

public class OnceTokenTemplateDirectiveBody implements TemplateDirectiveBody {
	
	private String tokenNameField;
	
	private String tokenValue;

	public OnceTokenTemplateDirectiveBody(String tokenNameField, String tokenValue) {
		this.tokenNameField = tokenNameField;
		this.tokenValue = tokenValue;
	}
	
	@Override
	public void render(Writer out) throws TemplateException, IOException {
		out.append("<input type=\"hidden\" name=\"" + MTags.TOKEN_NAME_FILED_FILED + "\" value=\"" + tokenNameField + "\" />");
		out.append("<input type=\"hidden\" name=\"" + tokenNameField + "\" value=\"" + tokenValue + "\" />");
		

	}

}

package cn.meteor.module.core.freemarker.tags;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.shiro.SecurityUtils;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

public class OnceTokenTag  implements TemplateDirectiveModel {

	@Override
	public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
		String tokenNameField = "" + params.get("name");
		String tokenValue =  RandomStringUtils.random(6, false, true);
		SecurityUtils.getSubject().getSession().setAttribute(tokenNameField + "_session", tokenValue);//设置session
		body = new OnceTokenTemplateDirectiveBody(tokenNameField, tokenValue);
		body.render(env.getOut());
	}

	
	

}

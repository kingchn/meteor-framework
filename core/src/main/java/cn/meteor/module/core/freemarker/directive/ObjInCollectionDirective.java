package cn.meteor.module.core.freemarker.directive;

import java.io.IOException;
import java.util.Map;

import cn.meteor.module.core.auth.service.AuthFunctions;
import freemarker.core.Environment;
import freemarker.template.SimpleNumber;
import freemarker.template.SimpleScalar;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

public class ObjInCollectionDirective implements TemplateDirectiveModel {

	@Override
	public void execute(Environment env, Map params, TemplateModel[] loopVars,
			TemplateDirectiveBody body) throws TemplateException, IOException {
		
		Iterable iterable = null;
		if(params.get("iterable") != null) {
//			iterable=((SimpleSequence)params.get("iterable")).toList();
			iterable=(Iterable) ((freemarker.template.DefaultListAdapter)params.get("iterable")).getWrappedObject();
        }
		Object element = null;
		if(params.get("element") != null) {
			element=((SimpleNumber)params.get("element")).getAsNumber();
        }
		
		String trueReturnString= "true";
		if(params.get("trueReturn") != null) {
			trueReturnString=((SimpleScalar)params.get("trueReturn")).getAsString();
        }
		
		String falseReturnString= "false";
		if(params.get("falseReturn") != null) {
			falseReturnString=((SimpleScalar)params.get("falseReturn")).getAsString();
        }
		
		boolean isIn = AuthFunctions.in(iterable, element);
		String isInString = isIn?trueReturnString:falseReturnString;
		env.getOut().write(isInString);
	}

}

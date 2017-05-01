package cn.meteor.module.core.freemarker.directive;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import cn.meteor.module.core.auth.service.AuthFunctions;
import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

public class ResourceNamesDirective implements TemplateDirectiveModel {

	@Override
	public void execute(Environment env, Map params, TemplateModel[] loopVars,
			TemplateDirectiveBody body) throws TemplateException, IOException {
		
		List<Long> resourceIds = null;
		if(params.get("resourceIdList") != null){  
//			resourceIds=((SimpleSequence)params.get("resourceIds")).toList();
//			(DefaultListAdapter)params.get("resourceIds");
			resourceIds=(List<Long>) ((freemarker.template.DefaultListAdapter)params.get("resourceIdList")).getWrappedObject();
        }
		
//		String resourceNames = TagFunctions.getResourceNamesByResourceIdList(resourceIds);
		String resourceNames = AuthFunctions.getResourceNamesByResourceIdList(resourceIds);
		env.getOut().write(resourceNames);
	}

}

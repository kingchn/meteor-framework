package cn.meteor.module.util.spring;

import org.springframework.context.EmbeddedValueResolverAware;
import org.springframework.util.StringValueResolver;

public class SpringPropertiesUtils implements EmbeddedValueResolverAware {

	private static StringValueResolver stringValueResolver;
	
	@Override
	public void setEmbeddedValueResolver(StringValueResolver resolver) {
		stringValueResolver = resolver;
	}
	
	public static String getPropertiesValue(String name){
        return stringValueResolver.resolveStringValue(name);
    }

}

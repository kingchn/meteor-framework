package cn.meteor.module.core.openApi.annotation;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;

public class RestJacksonAnnotationIntrospector extends JacksonAnnotationIntrospector {
	
	private NamingStrategy rootNamingStrategy;
	
	public enum NamingStrategy {
		LOWER_CAMEL_CASE,	//驼峰式
		SNAKE_CASE	//蛇式
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1057172452561899455L;
	
	public RestJacksonAnnotationIntrospector() {
		this.rootNamingStrategy = NamingStrategy.LOWER_CAMEL_CASE;//默认驼峰式
	}
	
	public RestJacksonAnnotationIntrospector(NamingStrategy rootNamingStrategy) {
		this.rootNamingStrategy = rootNamingStrategy;
	}
	
	// 参考com.fasterxml.jackson.databind.PropertyNamingStrategy.UpperCamelCaseStrategy.translate(String)修改
	public String translateForLowerCamelCase(String input) {
        if (input == null || input.length() == 0){
            return input; // garbage in, garbage out
        }
        // Replace first lower-case letter with upper-case equivalent
        char c = input.charAt(0);
//        char uc = Character.toUpperCase(c);
        char uc = Character.toLowerCase(c);
        if (c == uc) {
            return input;
        }
        StringBuilder sb = new StringBuilder(input);
        sb.setCharAt(0, uc);
        return sb.toString();
    }
	

	//参考com.fasterxml.jackson.databind.PropertyNamingStrategy.SnakeCaseStrategy.translate(String)
	public String translateForSnakeCase(String input) {
        if (input == null) return input; // garbage in, garbage out
        int length = input.length();
        StringBuilder result = new StringBuilder(length * 2);
        int resultLength = 0;
        boolean wasPrevTranslated = false;
        for (int i = 0; i < length; i++)
        {
            char c = input.charAt(i);
            if (i > 0 || c != '_') // skip first starting underscore
            {
                if (Character.isUpperCase(c))
                {
                    if (!wasPrevTranslated && resultLength > 0 && result.charAt(resultLength - 1) != '_')
                    {
                        result.append('_');
                        resultLength++;
                    }
                    c = Character.toLowerCase(c);
                    wasPrevTranslated = true;
                }
                else
                {
                    wasPrevTranslated = false;
                }
                result.append(c);
                resultLength++;
            }
        }
        return resultLength > 0 ? result.toString() : input;
    }

	@Override
	public PropertyName findRootName(AnnotatedClass ac) {

		//
		//上级调用方法com.fasterxml.jackson.databind.util.RootNameLookup.findRootName(Class<?>, MapperConfig<?>)
		//自定义注解处理--start
		RestJsonRootName apiAnnotation = _findAnnotation(ac, RestJsonRootName.class);
		JsonRootName ann = _findAnnotation(ac, JsonRootName.class);
		boolean isUseApiAnnotation = true;//是否使用RestJsonRootName
		if(apiAnnotation != null) {
			if(ann!=null) {//如果JsonRootName也存在，则要判断RestJsonRootName是否优先
				if(apiAnnotation.isPrior()==false) {//如果不优先，则不要使用
					isUseApiAnnotation = false;
				}
			}
			if(isUseApiAnnotation==true) {
				String serializationName = "";
				if(StringUtils.isNotBlank(apiAnnotation.value()) ) {//如果注解有值，则使用注解的值
					serializationName = apiAnnotation.value();
				} else {//如果注解没值，则根据命名规则命名
					String fullClassName = ac.getName();
					String className = fullClassName.substring(fullClassName.lastIndexOf(".")+1, fullClassName.length());
					serializationName = translateForLowerCamelCase(className);//
					if(this.rootNamingStrategy==NamingStrategy.SNAKE_CASE) {//如果是蛇式
						serializationName = translateForSnakeCase(className);
					}
				}
				
				
		        String ns = apiAnnotation.namespace();
		        if (ns != null && ns.length() == 0) {
		            ns = null;
		        }
		        
		        return PropertyName.construct(serializationName, ns);
			}
			
		}
		//自定义注解处理--end
		
		
//		JsonRootName ann = _findAnnotation(ac, JsonRootName.class);
        if (ann == null) {
            return null;
        }
        String ns = ann.namespace();
        if (ns != null && ns.length() == 0) {
            ns = null;
        }
        return PropertyName.construct(ann.value(), ns);
	}
	
	

//	@SuppressWarnings("unchecked")
//    private final static Class<? extends Annotation>[] ANNOTATIONS_TO_INFER_SER = (Class<? extends Annotation>[])
//            new Class<?>[] {
//        JsonSerialize.class,
//        JsonView.class,
//        JsonFormat.class,
//        JsonTypeInfo.class,
//        JsonRawValue.class,
//        JsonUnwrapped.class,
//        JsonBackReference.class,
//        JsonManagedReference.class
//    };
//   
	
//	com.fasterxml.jackson.databind.introspect.POJOPropertiesCollector._addFields(Map<String, POJOPropertyBuilder>)
//	@Override
//    public PropertyName findNameForSerialization(Annotated a)
//    {
//        JsonGetter jg = _findAnnotation(a, JsonGetter.class);
//        if (jg != null) {
//            return PropertyName.construct(jg.value());
//        }
//        JsonProperty pann = _findAnnotation(a, JsonProperty.class);
//        if (pann != null) {
//            return PropertyName.construct(pann.value());
//        }
//        if (_hasOneOf(a, ANNOTATIONS_TO_INFER_SER)) {
//            return PropertyName.USE_DEFAULT;
//        }
//        return null;
//    }
	
	
//	  @Override
//	  public boolean isAnnotationBundle(Annotation ann) {
////	    if (ann.annotationType().equals(MaskField.class)) {
////	      return false;
////	    } else {
////	      return super.isAnnotationBundle(ann);
////	    }
//	  }

}

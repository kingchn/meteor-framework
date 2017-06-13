package cn.meteor.module.core.openApi.validation;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import cn.meteor.module.core.openApi.validation.impl.ApiPermissionValidator;

@Documented
@Constraint(validatedBy=ApiPermissionValidator.class)
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE})
@Retention(RUNTIME)  
public @interface ApiPermission {
	
	Class value();
	
	String charset() default "UTF-8";
	
	boolean isValidatePermission() default true;
	
	boolean isEnableLogger() default true;
	
	String message() default "{cn.meteor.module.core.openApi.validation.ApiPermission.message}";

	Class<?>[] groups() default {};  
	  
    Class<? extends Payload>[] payload() default {};
    
    

	
}

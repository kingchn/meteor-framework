package cn.meteor.module.core.rest.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 方法设置<br/>
 * 方法名 value = "get", 空时则使用对应方法的方法名<br/>
 * 响应实体格式 responseBodyFormat = "base64"<br/>
 */
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RestMethod {
	
	/**
	 * 方法名
	 * @return
	 */
	String value() default "";
	
	String responseBodyFormat() default "";
}

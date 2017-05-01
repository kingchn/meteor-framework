package cn.meteor.module.core.openApi.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RestRequestBody {

	/**
	 * Whether body content is required.
	 * <p>Default is {@code true}, leading to an exception thrown in case
	 * there is no body content. Switch this to {@code false} if you prefer
	 * {@code null} to be passed when the body content is {@code null}.
	 * @since 3.2
	 */
	boolean required() default true;
}

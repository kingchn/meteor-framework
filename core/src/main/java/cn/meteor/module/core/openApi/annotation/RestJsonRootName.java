package cn.meteor.module.core.openApi.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.ANNOTATION_TYPE, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@com.fasterxml.jackson.annotation.JacksonAnnotation
public @interface RestJsonRootName {
	
    /**
     * Root name to use if root-level wrapping is enabled. For data formats
     * that use composite names (XML), this is the "local part" of the name
     * to use.
     */
    public String value() default "";

    /**
     * Optional namespace to use with data formats that support such
     * concept (specifically XML); if so, used with {@link #value} to
     * construct fully-qualified name.
     *
     * @since 2.4
     */
    public String namespace() default "";
    
    public boolean isPrior() default false;
    
}

package cn.meteor.module.core.openApi.advice;

import org.springframework.web.servlet.mvc.method.annotation.AbstractJsonpResponseBodyAdvice;

//@ControllerAdvice
/**
 * 单传参包含指定关键词时，执行下列切面
 * org.springframework.web.servlet.mvc.method.annotation.AbstractJsonpResponseBodyAdvice.beforeBodyWriteInternal(MappingJacksonValue, MediaType, MethodParameter, ServerHttpRequest, ServerHttpResponse)
 * @author shenjc
 *
 */
public class JsonpAdvice extends AbstractJsonpResponseBodyAdvice {

    public JsonpAdvice() {
        super("callback");
    }
    
    public JsonpAdvice(String paramName) {
        super(paramName);
    }
}

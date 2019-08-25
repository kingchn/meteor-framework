package cn.meteor.module.core.rest.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import cn.meteor.module.util.spring.SpringWebUtils;

//https://www.cnblogs.com/kevin-yuan/p/9208429.html
//http://slackspace.de/articles/log-request-body-with-spring-boot/
//https://blog.csdn.net/j080624/article/details/80747669
//https://blog.csdn.net/java_gchsh/article/details/79207460

/**
* A filter which logs web requests that lead to an error in the system.
*
*/
//@Component
public class RestLogFilter extends OncePerRequestFilter implements Ordered {

//    private final Log logger = LogFactory.getLog(getClass());
//	private static final Logger logger = LogManager.getLogger(RestApiController.class);
	private final Logger logger = LogManager.getLogger(getClass());

    // put filter at the end of all other filters to make sure we are processing after all others
    private int order = Ordered.LOWEST_PRECEDENCE - 8;
//    private ErrorAttributes errorAttributes;

    @Override
    public int getOrder() {
        return order;
    }
    
    @Value("${core.rest.rootPath}")
	private String restRootPath;

    public void setRestRootPath(String restRootPath) {
		this.restRootPath = restRootPath;
	}

	@Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {
    	
    	String requestBody = null;
        String responseBody = null;
        String traceString = null;
        ContentCachingRequestWrapper requestWrapper = null;
        ContentCachingResponseWrapper responseWrapper = null;
        requestWrapper = new ContentCachingRequestWrapper(request);
        
        String servletPath = requestWrapper.getServletPath();
        
        boolean isRestRequest = false;
		if (servletPath.startsWith(restRootPath)) {
			isRestRequest = true;
		}    	
        
        if(isRestRequest) {
            responseWrapper =  new ContentCachingResponseWrapper(response);
        	
        	// pass through filter chain to do the actual request handling
            filterChain.doFilter(requestWrapper, responseWrapper);

            requestBody = SpringWebUtils.getRequestBody(requestWrapper);
			responseBody = SpringWebUtils.getResponseBody(responseWrapper);
            
			if (!responseBody.startsWith("{\"code\":\"1\"")  || responseWrapper.getStatus()==HttpStatus.INTERNAL_SERVER_ERROR.value()) {//rest接口返回非成功状态
				Object traceObject = requestWrapper.getAttribute("traceString");
				if (traceObject != null) {
					traceString = (String) traceObject;
				}

				String logString = "";
				logString += "调用报错-->\n";
				logString += "请求报文: " + requestBody + "\n";
				if (responseBody != null) {
					logString += "响应报文: " + responseBody + "\n";
				}
				if (traceString != null) {
					logString += "错误信息: " + traceString;
				}
				logger.info(logString);
			}
			responseWrapper.copyBodyToResponse();//确保response输出    		       
        } else {
        	// pass through filter chain to do the actual request handling
            filterChain.doFilter(requestWrapper, response);
        }
        
    }


//    protected Map<String, Object> getTrace(HttpServletRequest request, int httpStatusCode) {
////        Principal principal = request.getUserPrincipal();
////      trace.put("principal", principal.getName());
//
//        Map<String, Object> trace = new LinkedHashMap<String, Object>();
//        trace.put("method", request.getMethod());
//        trace.put("path", request.getRequestURI());
//        trace.put("query", request.getQueryString());
//        trace.put("httpStatusCode", httpStatusCode);
//        
//        String traceString = null;
//        Object traceObject = request.getAttribute("traceString"); 
//        if(traceObject!=null) {
//        	traceString = (String) traceObject;
//        	trace.put("traceString", traceString);
//        }
//        
//        
////        Throwable exception = (Throwable) request.getAttribute("javax.servlet.error.exception");
////        if (exception != null && this.errorAttributes != null) {
////            trace.put("error", this.errorAttributes
////                .getErrorAttributes(new ServletRequestAttributes(request), true));
////        }
//
//        return trace;
//    }

}
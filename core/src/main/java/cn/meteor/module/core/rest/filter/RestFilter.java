package cn.meteor.module.core.rest.filter;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;

import cn.meteor.module.core.openApi.servlet.BodyReaderHttpServletRequestWrapper;
import cn.meteor.module.core.rest.container.RestContainer;
import cn.meteor.module.core.rest.request.RestCommonRequest;
import cn.meteor.module.core.rest.response.RestCommonResponse;
import cn.meteor.module.core.rest.utils.RestRequestValidUtils;
import cn.meteor.module.util.spring.SpringWebUtils;

//@Component
public class RestFilter extends OncePerRequestFilter implements Ordered {

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
    
    @Autowired
	private RestContainer restContainer;

	public void setRestContainer(RestContainer restContainer) {
		this.restContainer = restContainer;
	}
	
	@Autowired
	private RestExceptionHandler restExceptionHandler;

	public void setRestExceptionHandler(RestExceptionHandler restExceptionHandler) {
		this.restExceptionHandler = restExceptionHandler;
	}

	@Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {
    	
    	String requestBody = null;
        String responseBody = null;
//        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
        ServletRequest requestWrapper = new BodyReaderHttpServletRequestWrapper((HttpServletRequest) request);//保证request.getInputStream()可以重复调用
        ContentCachingResponseWrapper responseWrapper = null;
        
        String servletPath = request.getServletPath();
        
        boolean isRestRequest = false;
		if (servletPath.startsWith(restRootPath)) {
			isRestRequest = true;
		}    	
        
        if(isRestRequest) {
            try {
				responseWrapper =  new ContentCachingResponseWrapper(response);

				requestBody = IOUtils.toString(requestWrapper.getInputStream(), UTF_8);
				RestCommonRequest restCommonRequest = restContainer.getObjectMapper().readValue(requestBody, RestCommonRequest.class);
				
				//验证时间戳
				RestRequestValidUtils.validRequestTimestamp(restCommonRequest);
				
				//获取请求报文中body的原始字符串。base64则为base64字符串；json则为json字符串
				String requestBodyJsonString = RestRequestValidUtils.getRequestJsonBodyString(restCommonRequest, restContainer.getObjectMapper());
				
				//验证请求签名
				RestRequestValidUtils.validRequestSign(restCommonRequest, requestBodyJsonString, restContainer.getAppSecretManager());
				
				// pass through filter chain to do the actual request handling
				filterChain.doFilter(requestWrapper, responseWrapper);
				
				responseBody = SpringWebUtils.getResponseBody(responseWrapper);
				
				if (!responseBody.startsWith("{\"code\":\"1\"")  || responseWrapper.getStatus()==HttpStatus.INTERNAL_SERVER_ERROR.value()) {//rest接口返回非成功状态
					printLog(request, requestBody, responseBody);
				}
				responseWrapper.copyBodyToResponse();//确保response输出
			} catch (Exception ex) {//filter 发生的异常，则不会走doFilter，这里直接处理异常并输出响应报文、打印日志
				RestCommonResponse responseObject = restExceptionHandler.handleException(request, response, ex);
				
				response.setContentType("application/json;charset=UTF-8");
		        response.setCharacterEncoding("UTF-8");
		        PrintWriter out = response.getWriter();        
		        responseBody = restContainer.getObjectMapper().writeValueAsString(responseObject);
		        out.println(responseBody);
		        out.flush();
		        out.close();
		        
		        printLog(request, requestBody, responseBody);
			}
            
			
        } else {
        	// pass through filter chain to do the actual request handling
            filterChain.doFilter(requestWrapper, response);
        }
        
    }
	
	/**
	 * 打印日志
	 */
	public void printLog(HttpServletRequest request, String requestBody, String responseBody) throws IOException {
		String traceString = null;
		Object traceObject = request.getAttribute("traceString");
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
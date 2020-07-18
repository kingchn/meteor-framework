package cn.meteor.module.core.openApi.logger;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ResponseLoggerFilter implements Filter {
	
	private  static final Logger logger = LogManager.getLogger(ResponseLoggerFilter.class);

    @Override
    public void init(FilterConfig config) throws ServletException {
        // NOOP.
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
    	
        if (response.getCharacterEncoding() == null) {
            response.setCharacterEncoding("UTF-8"); // Or whatever default. UTF-8 is good for World Domination.
        }

        HttpServletResponseCopier responseCopier = new HttpServletResponseCopier((HttpServletResponse) response);

        try {
            chain.doFilter(request, responseCopier);
            responseCopier.flushBuffer();
        } finally {
            byte[] copy = responseCopier.getCopy();
            
            try {
				HttpServletRequest httpServletRequest = (HttpServletRequest) request;
				String requestURI = httpServletRequest.getRequestURI();
				String requestQueryString = httpServletRequest.getQueryString();
				String msg = "\nrequestURI:" + requestURI + " requestQueryString:" + requestQueryString + "\n";
				String contentType = response.getContentType();
				if(contentType!=null) {
					if(contentType.contains("application/json") || contentType.contains("application/javascript") 
							|| contentType.contains("application/xml") || contentType.contains("text/xml")) {				
						if (copy.length > 4 * 1024) {// 大于4k则不打印响应内容
							msg = msg + "response: to large, length:" + copy.length + "\n";
						} else {
							msg = msg + "response:" + new String(copy, response.getCharacterEncoding()) + "\n";
						}			            
					}
					msg = msg + "contentType:" + contentType;
				} else {
					msg = msg + "contentType:";
				}				
				logger.debug(msg);
			} catch (Exception e) {
				e.printStackTrace();
			}
            
        }
    }

    @Override
    public void destroy() {
        // NOOP.
    }

}

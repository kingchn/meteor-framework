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

public class ResponseLoggerFilter implements Filter {

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
				String content = "requestURI:" + requestURI + " requestQueryString:" + requestQueryString + "\n";
				content = content + "response:" + new String(copy, response.getCharacterEncoding());
				System.out.println(content); // Do your logging job here. This is just a basic example.
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

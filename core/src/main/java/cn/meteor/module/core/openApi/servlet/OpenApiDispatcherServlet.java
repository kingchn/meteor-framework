package cn.meteor.module.core.openApi.servlet;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.DispatcherServlet;

public class OpenApiDispatcherServlet extends DispatcherServlet {
	
	/**
	 * Process the actual dispatching to the handler.
	 * <p>The handler will be obtained by applying the servlet's HandlerMappings in order.
	 * The HandlerAdapter will be obtained by querying the servlet's installed HandlerAdapters
	 * to find the first that supports the handler class.
	 * <p>All HTTP methods are handled by this method. It's up to HandlerAdapters or handlers
	 * themselves to decide which methods are acceptable.
	 * @param request current HTTP request
	 * @param response current HTTP response
	 * @throws Exception in case of any kind of processing failure
	 */
	protected void doDispatch(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String contentType = request.getHeader("Content-Type");
		if ("text/xml".equals(contentType)) {//TODO: contentType判断？ 乱码问题
			ServletRequest requestWrapper = new BodyReaderHttpServletRequestWrapper((HttpServletRequest) request);//保证request.getInputStream()可以重复调用
			super.doDispatch((HttpServletRequest) requestWrapper, response);
		} else {
			super.doDispatch((HttpServletRequest) request, response);
		}
	}
}

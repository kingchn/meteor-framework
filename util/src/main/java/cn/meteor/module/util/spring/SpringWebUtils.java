package cn.meteor.module.util.spring;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;
import org.springframework.web.util.WebUtils;

public class SpringWebUtils {
	
	public static String getRequestBody(ContentCachingRequestWrapper wrappedRequest) {
		String payload = null;
//		wrap request to make sure we can read the body of the request (otherwise it will be consumed by the actual request handler)
		ContentCachingRequestWrapper wrapper = WebUtils.getNativeRequest(wrappedRequest, ContentCachingRequestWrapper.class);
		if (wrapper != null) {
			byte[] buf = wrapper.getContentAsByteArray();
			if (buf.length > 0) {
				try {
					payload = new String(buf, 0, buf.length, wrapper.getCharacterEncoding());
				} catch (UnsupportedEncodingException ex) {
					payload = "[UnsupportedEncoding]";
				}
			}
		}
		return payload;
	}
	
	
	public static String getResponseBody(ContentCachingResponseWrapper wrapperResponse) {
		String payload = null;
		ContentCachingResponseWrapper wrapper = WebUtils.getNativeResponse(wrapperResponse, ContentCachingResponseWrapper.class);
		if (wrapper != null) {
			byte[] buf = wrapper.getContentAsByteArray();
			if (buf.length > 0) {
				try {
					payload = new String(buf, 0, buf.length, wrapper.getCharacterEncoding());
				} catch (UnsupportedEncodingException ex) {
					payload = "[UnsupportedEncoding]";
				}
			}
		}
		return payload;
	}
	
	
}

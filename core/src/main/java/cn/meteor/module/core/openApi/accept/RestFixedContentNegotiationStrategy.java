package cn.meteor.module.core.openApi.accept;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.MediaType;
import org.springframework.web.accept.ContentNegotiationStrategy;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.ServletWebRequest;

public class RestFixedContentNegotiationStrategy implements ContentNegotiationStrategy {

	private static final Log logger = LogFactory.getLog(RestFixedContentNegotiationStrategy.class);

//	private final List<MediaType> contentType;
	
	private Map<String, MediaType> defaultContentTypes= new HashMap<String, MediaType>();
	
//	public RestFixedContentNegotiationStrategy() {
//	}


	/**
	 * Create an instance with the given content type.
	 */
	public RestFixedContentNegotiationStrategy(Map<String, MediaType> defaultContentTypes) {
		this.defaultContentTypes = defaultContentTypes;
//		contentType= Collections.singletonList(defaultContentTypes.get(""));
	}
	
//	public void setDefaultContentTypes(Properties mediaTypes) {
//		if (!CollectionUtils.isEmpty(mediaTypes)) {
//			for (Entry<Object, Object> entry : mediaTypes.entrySet()) {
//				String url = ((String)entry.getKey()).toLowerCase(Locale.ENGLISH);
//				MediaType mediaType = MediaType.valueOf((String) entry.getValue());
//				this.defaultContentTypes.put(url, mediaType);
//			}
//		}
//	}


	@Override
	public List<MediaType> resolveMediaTypes(NativeWebRequest request) {
		List<MediaType> contentType= Collections.singletonList(defaultContentTypes.get(""));
		if(request instanceof ServletWebRequest) {
			ServletWebRequest servletWebRequest = (ServletWebRequest) request;			
			for (String key : defaultContentTypes.keySet()) {
				if(StringUtils.isNotEmpty(key) && servletWebRequest.getRequest().getRequestURI().startsWith(key) ) {
					contentType= Collections.singletonList(defaultContentTypes.get(key));
				}
			}
			System.out.println( servletWebRequest.getRequest().getRequestURI() +" Requested media types: " + contentType);
		}
		 
		if (logger.isDebugEnabled()) {
			logger.debug("Requested media types: " + contentType);
		}
		return contentType;
	}

}

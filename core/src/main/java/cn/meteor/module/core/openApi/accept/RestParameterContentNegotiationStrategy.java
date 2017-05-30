package cn.meteor.module.core.openApi.accept;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.accept.AbstractMappingContentNegotiationStrategy;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.ServletWebRequest;

public class RestParameterContentNegotiationStrategy extends AbstractMappingContentNegotiationStrategy {

	private static final Log logger = LogFactory.getLog(RestParameterContentNegotiationStrategy.class);

	private String parameterName = "format";


	/**
	 * Create an instance with the given map of file extensions and media types.
	 */
	public RestParameterContentNegotiationStrategy(Map<String, MediaType> mediaTypes) {
		super(mediaTypes);
	}


	/**
	 * Set the name of the parameter to use to determine requested media types.
	 * <p>By default this is set to {@code "format"}.
	 */
	public void setParameterName(String parameterName) {
		Assert.notNull(parameterName, "'parameterName' is required");
		this.parameterName = parameterName;
	}

	public String getParameterName() {
		return this.parameterName;
	}
	
	
	//add by shenjc start
	private String defaultMediaTypeKey;


	public String getDefaultMediaTypeKey() {
		return defaultMediaTypeKey;
	}


	public void setDefaultMediaTypeKey(String defaultMediaTypeKey) {
		this.defaultMediaTypeKey = defaultMediaTypeKey;
	}
	
	private String startUrl;


	public String getStartUrl() {
		return startUrl;
	}


	public void setStartUrl(String startUrl) {
		this.startUrl = startUrl;
	}
	//add by shenjc end


	@Override
	protected String getMediaTypeKey(NativeWebRequest request) {
//		return request.getParameter(getParameterName());
		//add by shenjc start
		String mediaTypeKey = null;
		if(request instanceof ServletWebRequest) {
			ServletWebRequest servletWebRequest = (ServletWebRequest) request;
			if(servletWebRequest.getRequest().getRequestURI().startsWith(startUrl)) {
				mediaTypeKey = request.getParameter(getParameterName());
				if(mediaTypeKey==null) {
					mediaTypeKey = defaultMediaTypeKey;
				}
			}
		}
		return mediaTypeKey;
		//add by shenjc end
	}

	@Override
	protected void handleMatch(String mediaTypeKey, MediaType mediaType) {
		if (logger.isDebugEnabled()) {
			logger.debug("Requested media type: '" + mediaType + "' based on '" +
					getParameterName() + "'='" + mediaTypeKey + "'");
		}
	}

	@Override
	protected MediaType handleNoMatch(NativeWebRequest request, String key)
			throws HttpMediaTypeNotAcceptableException {

		throw new HttpMediaTypeNotAcceptableException(getAllMediaTypes());
	}
}

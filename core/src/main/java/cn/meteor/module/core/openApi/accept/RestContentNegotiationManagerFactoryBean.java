package cn.meteor.module.core.openApi.accept;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.accept.ContentNegotiationStrategy;
import org.springframework.web.accept.FixedContentNegotiationStrategy;
import org.springframework.web.accept.HeaderContentNegotiationStrategy;
import org.springframework.web.accept.ParameterContentNegotiationStrategy;
import org.springframework.web.accept.PathExtensionContentNegotiationStrategy;
import org.springframework.web.accept.ServletPathExtensionContentNegotiationStrategy;
import org.springframework.web.context.ServletContextAware;

/**
 * @author shenjc
 * org.springframework.web.accept.ContentNegotiationManagerFactoryBean 
 */
public class RestContentNegotiationManagerFactoryBean implements FactoryBean<ContentNegotiationManager>, ServletContextAware, InitializingBean {
	
	private boolean favorPathExtension = true;

	private boolean favorParameter = false;

	private boolean ignoreAcceptHeader = false;

	private Map<String, MediaType> mediaTypes = new HashMap<String, MediaType>();

	private boolean ignoreUnknownPathExtensions = true;

	private Boolean useJaf;

	private String parameterName = "format";

	private ContentNegotiationStrategy defaultNegotiationStrategy;

	private ContentNegotiationManager contentNegotiationManager;

	private ServletContext servletContext;


	/**
	 * Whether the path extension in the URL path should be used to determine
	 * the requested media type.
	 * <p>By default this is set to {@code true} in which case a request
	 * for {@code /hotels.pdf} will be interpreted as a request for
	 * {@code "application/pdf"} regardless of the 'Accept' header.
	 */
	public void setFavorPathExtension(boolean favorPathExtension) {
		this.favorPathExtension = favorPathExtension;
	}

	/**
	 * Add a mapping from a key, extracted from a path extension or a query
	 * parameter, to a MediaType. This is required in order for the parameter
	 * strategy to work. Any extensions explicitly registered here are also
	 * whitelisted for the purpose of Reflected File Download attack detection
	 * (see Spring Framework reference documentation for more details on RFD
	 * attack protection).
	 * <p>The path extension strategy will also try to use
	 * {@link ServletContext#getMimeType} and JAF (if present) to resolve path
	 * extensions. To change this behavior see the {@link #useJaf} property.
	 * @param mediaTypes media type mappings
	 * @see #addMediaType(String, MediaType)
	 * @see #addMediaTypes(Map)
	 */
	public void setMediaTypes(Properties mediaTypes) {
		if (!CollectionUtils.isEmpty(mediaTypes)) {
			for (Entry<Object, Object> entry : mediaTypes.entrySet()) {
				String extension = ((String)entry.getKey()).toLowerCase(Locale.ENGLISH);
				MediaType mediaType = MediaType.valueOf((String) entry.getValue());
				this.mediaTypes.put(extension, mediaType);
			}
		}
	}

	/**
	 * An alternative to {@link #setMediaTypes} for use in Java code.
	 * @see #setMediaTypes
	 * @see #addMediaTypes
	 */
	public void addMediaType(String fileExtension, MediaType mediaType) {
		this.mediaTypes.put(fileExtension, mediaType);
	}

	/**
	 * An alternative to {@link #setMediaTypes} for use in Java code.
	 * @see #setMediaTypes
	 * @see #addMediaType
	 */
	public void addMediaTypes(Map<String, MediaType> mediaTypes) {
		if (mediaTypes != null) {
			this.mediaTypes.putAll(mediaTypes);
		}
	}

	/**
	 * Whether to ignore requests with path extension that cannot be resolved
	 * to any media type. Setting this to {@code false} will result in an
	 * {@code HttpMediaTypeNotAcceptableException} if there is no match.
	 * <p>By default this is set to {@code true}.
	 */
	public void setIgnoreUnknownPathExtensions(boolean ignore) {
		this.ignoreUnknownPathExtensions = ignore;
	}

	/**
	 * When {@link #setFavorPathExtension favorPathExtension} is set, this
	 * property determines whether to allow use of JAF (Java Activation Framework)
	 * to resolve a path extension to a specific MediaType.
	 * <p>By default this is not set in which case
	 * {@code PathExtensionContentNegotiationStrategy} will use JAF if available.
	 */
	public void setUseJaf(boolean useJaf) {
		this.useJaf = useJaf;
	}

	private boolean isUseJafTurnedOff() {
		return (this.useJaf != null && !this.useJaf);
	}

	/**
	 * Whether a request parameter ("format" by default) should be used to
	 * determine the requested media type. For this option to work you must
	 * register {@link #setMediaTypes media type mappings}.
	 * <p>By default this is set to {@code false}.
	 * @see #setParameterName
	 */
	public void setFavorParameter(boolean favorParameter) {
		this.favorParameter = favorParameter;
	}

	/**
	 * Set the query parameter name to use when {@link #setFavorParameter} is on.
	 * <p>The default parameter name is {@code "format"}.
	 */
	public void setParameterName(String parameterName) {
		Assert.notNull(parameterName, "parameterName is required");
		this.parameterName = parameterName;
	}

	/**
	 * Whether to disable checking the 'Accept' request header.
	 * <p>By default this value is set to {@code false}.
	 */
	public void setIgnoreAcceptHeader(boolean ignoreAcceptHeader) {
		this.ignoreAcceptHeader = ignoreAcceptHeader;
	}
	
	//add by shenjc start
	private MediaType defaultContentType;

	public MediaType getDefaultContentType() {
		return defaultContentType;
	}
	//add by shenjc end

	/**
	 * Set the default content type to use when no content type is requested.
	 * <p>By default this is not set.
	 * @see #setDefaultContentTypeStrategy
	 */
	public void setDefaultContentType(MediaType contentType) {
//		this.defaultNegotiationStrategy = new FixedContentNegotiationStrategy(contentType);
		this.defaultContentType = contentType;//modify by shenjc
	}

	/**
	 * Set a custom {@link ContentNegotiationStrategy} to use to determine
	 * the content type to use when no content type is requested.
	 * <p>By default this is not set.
	 * @see #setDefaultContentType
	 * @since 4.1.2
	 */
	public void setDefaultContentTypeStrategy(ContentNegotiationStrategy strategy) {
		this.defaultNegotiationStrategy = strategy;
	}

	/**
	 * Invoked by Spring to inject the ServletContext.
	 */
	@Override
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}


	@Override
	public void afterPropertiesSet() {
		List<ContentNegotiationStrategy> strategies = new ArrayList<ContentNegotiationStrategy>();

		if (this.favorPathExtension) {
			PathExtensionContentNegotiationStrategy strategy;
			if (this.servletContext != null && !isUseJafTurnedOff()) {
				strategy = new ServletPathExtensionContentNegotiationStrategy(
						this.servletContext, this.mediaTypes);
			}
			else {
				strategy = new PathExtensionContentNegotiationStrategy(this.mediaTypes);
			}
			strategy.setIgnoreUnknownExtensions(this.ignoreUnknownPathExtensions);
			if (this.useJaf != null) {
				strategy.setUseJaf(this.useJaf);
			}
			strategies.add(strategy);
		}

		if (this.favorParameter) {
			ParameterContentNegotiationStrategy strategy =
					new ParameterContentNegotiationStrategy(this.mediaTypes);
			strategy.setParameterName(this.parameterName);
			strategies.add(strategy);
		}

		if (!this.ignoreAcceptHeader) {
			strategies.add(new HeaderContentNegotiationStrategy());
		}
		
		//add by shenjc start
		if(this.defaultContentType!=null) {//如果有指定defaultContentType，则defaultNegotiationStrategy由defaultContentType决定
			this.defaultNegotiationStrategy = new FixedContentNegotiationStrategy(this.defaultContentType);
		}
		//add by shenjc end

		if (this.defaultNegotiationStrategy != null) {
			strategies.add(this.defaultNegotiationStrategy);
		}

		this.contentNegotiationManager = new ContentNegotiationManager(strategies);
	}

	@Override
	public ContentNegotiationManager getObject() {
		return this.contentNegotiationManager;
	}

	@Override
	public Class<?> getObjectType() {
		return ContentNegotiationManager.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}
	
	

//	private ContentNegotiationStrategy otherContentNegotiationStrategy;
//	
//	public ContentNegotiationStrategy getOtherContentNegotiationStrategy() {
//		return otherContentNegotiationStrategy;
//	}
//
//
//	public void setOtherContentNegotiationStrategy(ContentNegotiationStrategy otherContentNegotiationStrategy) {
//		this.otherContentNegotiationStrategy = otherContentNegotiationStrategy;
//	}
}

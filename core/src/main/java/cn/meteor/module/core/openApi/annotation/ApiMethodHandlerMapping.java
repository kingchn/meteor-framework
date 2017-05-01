package cn.meteor.module.core.openApi.annotation;

import java.io.IOException;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.condition.RequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

public class ApiMethodHandlerMapping extends RequestMappingHandlerMapping {
	
	private RequestMappingInfo.BuilderConfiguration config = new RequestMappingInfo.BuilderConfiguration();
	
	
	@Override
	public void afterPropertiesSet() {//add and modify by shenjc
//		this.config = new RequestMappingInfo.BuilderConfiguration();
//		this.config.setPathHelper(getUrlPathHelper());
//		this.config.setPathMatcher(getPathMatcher());
//		this.config.setSuffixPatternMatch(this.useSuffixPatternMatch);
//		this.config.setTrailingSlashMatch(this.useTrailingSlashMatch);
//		this.config.setRegisteredSuffixPatternMatch(this.useRegisteredSuffixPatternMatch);
		this.config.setContentNegotiationManager(getContentNegotiationManager());

		super.afterPropertiesSet();
	}
	
	/**
	 * {@inheritDoc}
	 * Expects a handler to have a type-level @{@link Controller} annotation.
	 */
//	@Override
//	protected boolean isHandler(Class<?> beanType) {
//		return ((AnnotationUtils.findAnnotation(beanType, Controller.class) != null) ||
//				(AnnotationUtils.findAnnotation(beanType, RequestMapping.class) != null));
//	}
	@Override
	protected boolean isHandler(Class<?> beanType) {
		return ((AnnotationUtils.findAnnotation(beanType, Controller.class) != null) ||
				(AnnotationUtils.findAnnotation(beanType, RequestMapping.class) != null) ||
				(AnnotationUtils.findAnnotation(beanType, ApiMethod.class) != null) );
	}
	
	
	/**
	 * Uses method and type-level @{@link RequestMapping} annotations to create
	 * the RequestMappingInfo.
	 * @return the created RequestMappingInfo, or {@code null} if the method
	 * does not have a {@code @RequestMapping} annotation.
	 * @see #getCustomMethodCondition(Method)
	 * @see #getCustomTypeCondition(Class)
	 */
	@Override
	protected RequestMappingInfo getMappingForMethod(Method method, Class<?> handlerType) {
		RequestMappingInfo info = createRequestMappingInfo(method);
		if (info != null) {
			RequestMappingInfo typeInfo = createRequestMappingInfo(handlerType);
			if (typeInfo != null) {
				info = typeInfo.combine(info);
			}
		}
		return info;
	}

	/**
	 * Delegates to {@link #createRequestMappingInfo(RequestMapping, RequestCondition)},
	 * supplying the appropriate custom {@link RequestCondition} depending on whether
	 * the supplied {@code annotatedElement} is a class or method.
	 * @see #getCustomTypeCondition(Class)
	 * @see #getCustomMethodCondition(Method)
	 */
//	private RequestMappingInfo createRequestMappingInfo(AnnotatedElement element) {
//		RequestMapping requestMapping = AnnotatedElementUtils.findMergedAnnotation(element, RequestMapping.class);
//		RequestCondition<?> condition = (element instanceof Class<?> ?
//				getCustomTypeCondition((Class<?>) element) : getCustomMethodCondition((Method) element));
//		return (requestMapping != null ? createRequestMappingInfo(requestMapping, condition) : null);
//	}
	private RequestMappingInfo createRequestMappingInfo(AnnotatedElement element) {//modify by shenjc
		boolean isRequestMapping = element.isAnnotationPresent(RequestMapping.class);
		if(isRequestMapping==true) {
			RequestMapping requestMapping = AnnotatedElementUtils.findMergedAnnotation(element, RequestMapping.class);
			RequestCondition<?> condition = (element instanceof Class<?> ?
					getCustomTypeCondition((Class<?>) element) : getCustomMethodCondition((Method) element));
			return (requestMapping != null ? createRequestMappingInfo(requestMapping, condition) : null);
		}
		boolean isApiMethod = element.isAnnotationPresent(ApiMethod.class);
		if(isApiMethod==true) {
			ApiMethod apiMethod = AnnotatedElementUtils.findMergedAnnotation(element, ApiMethod.class);
			RequestCondition<?> condition = (element instanceof Class<?> ?
					getCustomTypeCondition((Class<?>) element) : getCustomMethodCondition((Method) element));
			return (apiMethod != null ? createApiMethodInfo(apiMethod, condition, element) : null);
		}
		return null;
	}
	
	
	/**
	 * Create a {@link RequestMappingInfo} from the supplied
	 * {@link RequestMapping @RequestMapping} annotation, which is either
	 * a directly declared annotation, a meta-annotation, or the synthesized
	 * result of merging annotation attributes within an annotation hierarchy.
	 */
	protected RequestMappingInfo createRequestMappingInfo(
			RequestMapping requestMapping, RequestCondition<?> customCondition) {

		return RequestMappingInfo
				.paths(resolveEmbeddedValuesInPatterns(requestMapping.path()))
				.methods(requestMapping.method())
				.params(requestMapping.params())
				.headers(requestMapping.headers())
				.consumes(requestMapping.consumes())
				.produces(requestMapping.produces())
				.mappingName(requestMapping.name())
				.customCondition(customCondition)
				.options(this.config)
				.build();
	}
	
	protected RequestMappingInfo createApiMethodInfo(
			ApiMethod annotation, RequestCondition<?> customCondition, AnnotatedElement element) {//add and modify by shenjc
		
		//add by shenjc start		
		String[] paths;
		String[] params;
		if(element instanceof Class<?>) {//如果是作用在类上
			paths = resolveEmbeddedValuesInPatterns(annotation.path());
			params = annotation.params();
		} else {//否则，即如果是作用在方法上，则去掉路径，并把值转化为参数，即method=xxx
//			paths = new String[]{""};
			paths = resolveEmbeddedValuesInPatterns(annotation.path());
			params = new String[annotation.value().length+annotation.params().length];//RequestMapping
			for (int i = 0; i < annotation.value().length; i++) {
				params[i] = "method="+annotation.value()[i];
			}
			for (int j = 0; j < annotation.params().length; j++) {
				params[annotation.value().length+j] = annotation.params()[j];
			};
		}
		//add by shenjc end

		return RequestMappingInfo
//				.paths(resolveEmbeddedValuesInPatterns(annotation.path()))	//modify by shenjc
				.paths(paths)		//modify by shenjc
				.methods(annotation.method())
//				.params(annotation.params())	//modify by shenjc
				.params(params)						//modify by shenjc
				.headers(annotation.headers())
				.consumes(annotation.consumes())
				.produces(annotation.produces())
				.mappingName(annotation.name())
				.customCondition(customCondition)
				.options(this.config)
				.build();
	}
	
	private Boolean useHandler = false;
	
	
	public Boolean getUseHandler() {
		return useHandler;
	}

	public void setUseHandler(Boolean useHandler) {
		this.useHandler = useHandler;
	}

	@Override
	protected RequestMappingInfo getMatchingMapping(RequestMappingInfo info, HttpServletRequest request) {
		if (useHandler) {
			String contentType = request.getHeader("Content-Type");
			if ("text/xml".equals(contentType)) {
				try {
					SAXReader sax = new SAXReader();
					Document document = sax.read(request.getInputStream());// reader为定义的一个字符串，可以转换为xml
					Element root = document.getRootElement();//
					String handler = root.element("head").element("service").element("handler").getTextTrim();
					request.setAttribute("handler", handler);

				} catch (IOException e) {
					e.printStackTrace();
				} catch (DocumentException e) {
					e.printStackTrace();
				}
			}
		}	
		return info.getMatchingCondition(request);
	}
	
	

	/* 自定义RequestCondition start by shenjc */
	@Override
	protected RequestCondition<?> getCustomTypeCondition(Class<?> handlerType) {
		ApiMethod typeAnnotation = AnnotationUtils.findAnnotation(handlerType, ApiMethod.class);
		return createCondition(typeAnnotation);
	}

	@Override
	protected RequestCondition<?> getCustomMethodCondition(Method method) {
		ApiMethod methodAnnotation = AnnotationUtils.findAnnotation(method, ApiMethod.class);
		return createCondition(methodAnnotation);
	}

	private RequestCondition<?> createCondition(ApiMethod accessMapping) {
		return (accessMapping != null) ? new HandlersRequestCondition(accessMapping.handler()) : null;
	}
	/* 自定义RequestCondition end by shenjc */
}

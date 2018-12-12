package cn.meteor.module.core.web.servlet.handler;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class HandlerInterceptor extends HandlerInterceptorAdapter {
	
//	private static final String REQUEST = "request";
	
	private static final String ROOT_URL = "rootUrl";
	private static final String RES_URL = "resUrl";
	
	private static final String SPRING_VIEW_NAME = "springViewName";
	
//	@Value("${root.url.isAbsolute}")
	private Boolean isRootUrlAbsolute = false;

	public Boolean getIsRootUrlAbsolute() {
		return isRootUrlAbsolute;
	}

	public void setIsRootUrlAbsolute(Boolean isRootUrlAbsolute) {
		this.isRootUrlAbsolute = isRootUrlAbsolute;
	}
	
	/**
	 * ref参数名
	 */
	private String refParamName = "ref";
	
	/**
	 * ref参数值
	 */
	private String refParamValue = "addtabs";
	
	/**
	 * ref目标视图
	 */
	private String refTargetView = "main";
	
	public String getRefParamName() {
		return refParamName;
	}

	public void setRefParamName(String refParamName) {
		this.refParamName = refParamName;
	}

	public String getRefParamValue() {
		return refParamValue;
	}

	public void setRefParamValue(String refParamValue) {
		this.refParamValue = refParamValue;
	}

	public String getRefTargetView() {
		return refTargetView;
	}

	public void setRefTargetView(String refTargetView) {
		this.refTargetView = refTargetView;
	}
	
	
//	@Override
//	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
//			throws Exception {
//		BaseController baseController = (BaseController) handler;
//		baseController.setRequest(request);
//		baseController.setResponse(response);
//		baseController.setSession(request.getSession());
//		return super.preHandle(request, response, handler);
//	}






	@Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
		
		//注解拦截
		if(handler instanceof HandlerMethod) {
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			Method method = handlerMethod.getMethod();
			//处理注解RefererHandle，实现referer，并重定向到指定页面
			RefererHandle refererHandleAnnotation = method.getAnnotation(RefererHandle.class);
			if (refererHandleAnnotation != null) {
				String refPameValue= request.getParameter(refParamName);
				if(StringUtils.isNotBlank(refPameValue) && refParamValue.equals(refPameValue)) {
					modelAndView.addObject("referer", request.getServletPath());
					modelAndView.setViewName(refTargetView);
				}
			}
		}		
		
		if (null != modelAndView && null != modelAndView.getModel()) {
            Object result = modelAndView.getModel().get("result");
		}
//		https://my.oschina.net/freegeek/blog/300879	//注解拦截
//		HandlerMethod handlerMethod = (HandlerMethod) handler;
//		Method method = handlerMethod.GET).getMethod();
		
		//添加属性处理
		if(modelAndView != null && modelAndView.getViewName().startsWith("redirect:")) {
			modelAndView.getModelMap().clear();
		}
		
		//添加rootUrl属性处理
        if (modelAndView != null && !modelAndView.getViewName().startsWith("redirect:")) {
        	String rootUrl="";
        	if(isRootUrlAbsolute==true) {
        		if(request.getServerPort()==80) {
    				rootUrl = request.getScheme() + "://" + request.getServerName();
//    				rootUrl = "//" + request.getServerName();
    			} else {
    				rootUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
//    				rootUrl = "//" + request.getServerName() + ":" + request.getServerPort();
    			}
        	}
        	rootUrl += request.getContextPath();
        	Object rootUrlObject = modelAndView.getModel().get(ROOT_URL);
            if(rootUrlObject==null || "".equals(rootUrlObject)) {//如果rootUrl原来没值，则设置值
            	modelAndView.addObject(ROOT_URL, rootUrl);
            }
            Object resUrlObject = modelAndView.getModel().get(RES_URL);
            if(resUrlObject==null || "".equals(resUrlObject)) {//如果配置留空，则使用ROOT_URL
            	modelAndView.addObject(RES_URL, rootUrl);
            }
        }
		
		//添加viewName属性处理
        if (modelAndView != null && !modelAndView.getViewName().startsWith("redirect:")) {
            modelAndView.addObject(SPRING_VIEW_NAME, modelAndView.getViewName());
        }
        super.postHandle(request, response, handler, modelAndView);
    }
	
}

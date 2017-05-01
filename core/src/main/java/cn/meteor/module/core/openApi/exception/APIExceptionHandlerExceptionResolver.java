package cn.meteor.module.core.openApi.exception;

import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;
import org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

import cn.meteor.module.core.openApi.response.ErrorMsgUtils;
import cn.meteor.module.core.openApi.response.ErrorResponse;
import cn.meteor.module.core.openApi.response.ErrorType;

public class APIExceptionHandlerExceptionResolver extends ExceptionHandlerExceptionResolver {

	@Override
	protected ModelAndView doResolveHandlerMethodException(HttpServletRequest request,
			HttpServletResponse response, HandlerMethod handlerMethod, Exception exception) {

		ServletInvocableHandlerMethod exceptionHandlerMethod = getExceptionHandlerMethod(handlerMethod, exception);
		if (exceptionHandlerMethod == null) {
			return null;
		}

//		exceptionHandlerMethod.setHandlerMethodArgumentResolvers(this.argumentResolvers);
//		exceptionHandlerMethod.setHandlerMethodReturnValueHandlers(this.returnValueHandlers);		
		exceptionHandlerMethod.setHandlerMethodArgumentResolvers(this.getArgumentResolvers());
		exceptionHandlerMethod.setHandlerMethodReturnValueHandlers(this.getReturnValueHandlers());

		ServletWebRequest webRequest = new ServletWebRequest(request, response);
		ModelAndViewContainer mavContainer = new ModelAndViewContainer();

		try {
			if (logger.isDebugEnabled()) {
				logger.debug("Invoking @ExceptionHandler method: " + exceptionHandlerMethod);
			}
			exceptionHandlerMethod.invokeAndHandle(webRequest, mavContainer, exception, handlerMethod);
		}
		catch (Exception invocationEx) {
			if (logger.isDebugEnabled()) {
				logger.debug("Failed to invoke @ExceptionHandler method: " + exceptionHandlerMethod, invocationEx);
			}
			return null;
		}

		if (mavContainer.isRequestHandled()) {
			return new ModelAndView();
		}
		else {
			ModelAndView mav = new ModelAndView().addAllObjects(mavContainer.getModel());
			mav.setViewName(mavContainer.getViewName());
			if (!mavContainer.isViewReference()) {
				mav.setView((View) mavContainer.getView());
			}
			return mav;
		}
	}
	
	
//	protected ModelAndView getModelAndView(String viewName, Exception ex, HttpServletRequest request) {
////		return super.getModelAndView(viewName, ex, request);
//		
////		ModelAndView mv = new ModelAndView(viewName);
////		if (this.exceptionAttribute != null) {
////			if (logger.isDebugEnabled()) {
////				logger.debug("Exposing Exception as model attribute '" + this.exceptionAttribute + "'");
////			}
////			mv.addObject(this.exceptionAttribute, ex);
////		}
////		return mv;
//		
//		Locale locale = request.getLocale();
//		if(locale==null) {
//			locale = Locale.ENGLISH;
//		}
//		ModelAndView mv = new ModelAndView(viewName);
//		ErrorResponse errorResponse = getErrorResponseAnalysisException(viewName, ex, request, locale);
//		
//		if(errorResponse.getCode()==null) {//未匹配到异常信息
//			if (ex instanceof NoSuchRequestHandlingMethodException) {
//				String method=request.getParameter("method");
//				if(method==null||"".equals(method)) {
//					errorResponse.setCode(ErrorMsgUtils.getErrorCode(ErrorType.MISSING_METHOD));
//					errorResponse.setMsg(ErrorMsgUtils.getErrorMsg(ErrorType.MISSING_METHOD, locale));
//				} else {
//					errorResponse.setCode(ErrorMsgUtils.getErrorCode(ErrorType.INVALID_METHOD));
//					errorResponse.setMsg(ErrorMsgUtils.getErrorMsg(ErrorType.INVALID_METHOD, locale));
//				}				
//			} else if(ex instanceof BindException) {//参数错误
//				errorResponse.setCode(ErrorMsgUtils.getErrorCode(ErrorType.PARAMETER_ERROR));
//				errorResponse.setMsg(ErrorMsgUtils.getErrorMsg(ErrorType.PARAMETER_ERROR, locale));
//				
//				List<FieldError> fieldErrors =  ((BindException) ex).getBindingResult().getFieldErrors();
//				for (FieldError fieldError : fieldErrors) {
//					
//					if("ApiPermission".equals(fieldError.getCode())) {
//						String jsrMsg=fieldError.getDefaultMessage();
//						ErrorType errorType = getErrorTypeByJsrMsg(jsrMsg);
//						if(errorType!=null) {
//							errorResponse.setDetailCode(ErrorMsgUtils.getErrorCode(errorType));
//							errorResponse.setDetailMsg(ErrorMsgUtils.getErrorMsg(errorType, new Object[] {fieldError.getField(), fieldError.getRejectedValue()}, locale));
//							break;//如果不符合授权请求，则只返回该异常信息
//						}
//					} else {
//						ErrorType errorType = getErrorTypeByJsrMsg(fieldError.getCode());
//						if(errorType!=null) {
//							errorResponse.setDetailCode(ErrorMsgUtils.getErrorCode(errorType));
//							errorResponse.setDetailMsg(ErrorMsgUtils.getErrorMsg(errorType, new Object[] {fieldError.getField(), fieldError.getRejectedValue()}, locale));
//						}
//						
//					}
//				}				
//			} else if(ex instanceof IsvException) {
//				IsvException isvException = (IsvException) ex;
//				errorResponse.setCode(isvException.getCode());
//				errorResponse.setMsg(isvException.getMsg());
//				errorResponse.setDetailCode(isvException.getDetailCode());
//				errorResponse.setDetailMsg(isvException.getDetailMsg());
//			} else if(ex instanceof IspException) {
//				IspException ispException = (IspException) ex;
//				errorResponse.setCode(ispException.getCode());
//				errorResponse.setMsg(ispException.getMsg());
//				errorResponse.setDetailCode(ispException.getDetailCode());
//				errorResponse.setDetailMsg(ispException.getDetailMsg());
//				
////				IspException ispServiceUnavailableException = (IspException) ex;
////				errorResponse.setCode(ErrorMsgUtils.getErrorCode(ErrorType.SERVICE_CURRENTLY_UNAVAILABLE));
////				errorResponse.setMsg(ErrorMsgUtils.getErrorMsg(ErrorType.SERVICE_CURRENTLY_UNAVAILABLE, locale));
////				errorResponse.setDetailCode(ErrorMsgUtils.getErrorCode(ErrorType.ISP_SERVICE_UNAVAILABLE).replace("***", ispServiceUnavailableException.getServiceName()));
////				errorResponse.setDetailMsg(ErrorMsgUtils.getErrorMsg(ErrorType.ISP_SERVICE_UNAVAILABLE, new Object[] { ispServiceUnavailableException.getServiceName(), ispServiceUnavailableException.getMsg() }, locale));
//			}
////			if(ex instanceof HibernateException) {
////				String errorMsg=ex.getMessage();
////				errorResponse.setCode(ErrorMsgUtils.getErrorCode(ErrorType.SERVICE_CURRENTLY_UNAVAILABLE));
////				errorResponse.setMsg(ErrorMsgUtils.getErrorMsg(ErrorType.SERVICE_CURRENTLY_UNAVAILABLE, locale));
////				errorResponse.setDetailCode(ErrorMsgUtils.getErrorCode(ErrorType.ISP_HIBERNATE_ERROR));
////				errorResponse.setDetailMsg(ErrorMsgUtils.getErrorMsg(ErrorType.ISP_HIBERNATE_ERROR, new Object[] { errorMsg }, locale));
////			} else if(ex instanceof SolrServerException || ex instanceof SolrException) {
////				String errorMsg=ex.getMessage();
////				errorResponse.setCode(ErrorMsgUtils.getErrorCode(ErrorType.SERVICE_CURRENTLY_UNAVAILABLE));
////				errorResponse.setMsg(ErrorMsgUtils.getErrorMsg(ErrorType.SERVICE_CURRENTLY_UNAVAILABLE, locale));
////				errorResponse.setDetailCode(ErrorMsgUtils.getErrorCode(ErrorType.ISP_SEARCH_ENGINE));
////				errorResponse.setDetailMsg(ErrorMsgUtils.getErrorMsg(ErrorType.ISP_SEARCH_ENGINE, new Object[] { errorMsg }, locale));
////			}
//		}
//		if(errorResponse!=null&&errorResponse.getCode()==null) {//未知错误
//			errorResponse.setCode(ErrorMsgUtils.getErrorCode(ErrorType.SERVICE_CURRENTLY_UNAVAILABLE));
//			errorResponse.setMsg(ErrorMsgUtils.getErrorMsg(ErrorType.SERVICE_CURRENTLY_UNAVAILABLE, locale));
//			errorResponse.setDetailCode(ErrorMsgUtils.getErrorCode(ErrorType.ISP_UNKNOWN_ERROR));
//			errorResponse.setDetailMsg(ErrorMsgUtils.getErrorMsg(ErrorType.ISP_UNKNOWN_ERROR, locale));
//		}
//		
//		
////		mv.getModelMap().addAttribute("response", errorResponse);
//		mv.addObject(errorResponse);
//		return mv;
//	}
//	
//	protected ErrorResponse getErrorResponseAnalysisException(String viewName, Exception ex, HttpServletRequest request, Locale locale) {
//		logger.error(ex.toString());		
//		
//		ErrorResponse errorResponse = new ErrorResponse();		
//		
//		if (this.exceptionAttribute != null) {
//			if (logger.isDebugEnabled()) {
//				logger.debug("Exposing Exception as model attribute '" + this.exceptionAttribute + "'");
//			}
//			
//			if (ex instanceof NoSuchRequestHandlingMethodException) {
//				String method=request.getParameter("method");
//				if(method==null||"".equals(method)) {
//					errorResponse.setCode(ErrorMsgUtils.getErrorCode(ErrorType.MISSING_METHOD));
//					errorResponse.setMsg(ErrorMsgUtils.getErrorMsg(ErrorType.MISSING_METHOD, locale));
//				} else {
//					errorResponse.setCode(ErrorMsgUtils.getErrorCode(ErrorType.INVALID_METHOD));
//					errorResponse.setMsg(ErrorMsgUtils.getErrorMsg(ErrorType.INVALID_METHOD, locale));
//				}				
//			} else if(ex instanceof BindException) {//参数错误
//				errorResponse.setCode(ErrorMsgUtils.getErrorCode(ErrorType.PARAMETER_ERROR));
//				errorResponse.setMsg(ErrorMsgUtils.getErrorMsg(ErrorType.PARAMETER_ERROR, locale));
//				
//				List<FieldError> fieldErrors =  ((BindException) ex).getBindingResult().getFieldErrors();
//				for (FieldError fieldError : fieldErrors) {
//					
//					if("ApiPermission".equals(fieldError.getCode())) {
//						String jsrMsg=fieldError.getDefaultMessage();
//						ErrorType errorType = getErrorTypeByJsrMsg(jsrMsg);
//						if(errorType!=null) {
//							errorResponse.setDetailCode(ErrorMsgUtils.getErrorCode(errorType));
//							errorResponse.setDetailMsg(ErrorMsgUtils.getErrorMsg(errorType, new Object[] {fieldError.getField(), fieldError.getRejectedValue()}, locale));
//							break;//如果不符合授权请求，则只返回该异常信息
//						}
//					} else {
//						ErrorType errorType = getErrorTypeByJsrMsg(fieldError.getCode());
//						if(errorType!=null) {
//							errorResponse.setDetailCode(ErrorMsgUtils.getErrorCode(errorType));
//							errorResponse.setDetailMsg(ErrorMsgUtils.getErrorMsg(errorType, new Object[] {fieldError.getField(), fieldError.getRejectedValue()}, locale));
//						}
//						
//					}
//				}
//			} else if(ex instanceof NullPointerException) {
//				errorResponse.setCode(ErrorMsgUtils.getErrorCode(ErrorType.SERVICE_CURRENTLY_UNAVAILABLE));
//				errorResponse.setMsg(ErrorMsgUtils.getErrorMsg(ErrorType.SERVICE_CURRENTLY_UNAVAILABLE, locale));
//				errorResponse.setDetailCode(ErrorMsgUtils.getErrorCode(ErrorType.ISP_NULL_POINTER_EXCEPTION));
//				errorResponse.setDetailMsg(ErrorMsgUtils.getErrorMsg(ErrorType.ISP_NULL_POINTER_EXCEPTION, locale));
//			} else if(ex instanceof HttpRequestMethodNotSupportedException) {
//				errorResponse.setCode(ErrorMsgUtils.getErrorCode(ErrorType.HTTP_ACTION_NOT_ALLOWED));
//				errorResponse.setMsg(ErrorMsgUtils.getErrorMsg(ErrorType.HTTP_ACTION_NOT_ALLOWED, locale));
//			}
//			
//		}		
//		
//		
//		return errorResponse;
//	}
//	
//	private ErrorType getErrorTypeByJsrMsg(String jsrMsg) {
//		if(ErrorMsgUtils.JSR_MSG_CONSTRAINT_ERROR_TYPE_MAPPINGS.containsKey(jsrMsg)) {
//			ErrorType errorType = ErrorMsgUtils.JSR_MSG_CONSTRAINT_ERROR_TYPE_MAPPINGS.get(jsrMsg);//通过JSR信息得到错误类型
//			return errorType;
//		} else {
//			return null;
//		}
//	}
}

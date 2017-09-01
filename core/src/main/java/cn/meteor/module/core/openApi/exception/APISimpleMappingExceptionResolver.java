package cn.meteor.module.core.openApi.exception;

import java.text.MessageFormat;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.UnsatisfiedServletRequestParameterException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

import cn.meteor.module.core.openApi.response.ErrorMsgUtils;
import cn.meteor.module.core.openApi.response.ErrorResponse;
import cn.meteor.module.core.openApi.response.ErrorType;

public class APISimpleMappingExceptionResolver extends SimpleMappingExceptionResolver {
	
	private String exceptionAttribute = DEFAULT_EXCEPTION_ATTRIBUTE;
	
	/**
	 * Set the name of the model attribute as which the exception should be exposed.
	 * Default is "exception".
	 * <p>This can be either set to a different attribute name or to {@code null}
	 * for not exposing an exception attribute at all.
	 * @see #DEFAULT_EXCEPTION_ATTRIBUTE
	 */
	public void setExceptionAttribute(String exceptionAttribute) {
		this.exceptionAttribute = exceptionAttribute;
	}
	
//	@Override
//	protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response,
//			Object handler, Exception ex) {
//
//		// Expose ModelAndView for chosen error view.
//		String viewName = determineViewName(ex, request);
//		if (viewName != null) {
//			// Apply HTTP status code for error views, if specified.
//			// Only apply it if we're processing a top-level request.
//			Integer statusCode = determineStatusCode(request, viewName);
//			if (statusCode != null) {
//				applyStatusCodeIfPossible(request, response, statusCode);
//			}
//			return getModelAndView(viewName, ex, request);
//		}
//		else {
//			return null;
//		}
//	}

	@Override
	protected ModelAndView getModelAndView(String viewName, Exception ex, HttpServletRequest request) {
//		return super.getModelAndView(viewName, ex, request);
		
//		ModelAndView mv = new ModelAndView(viewName);
//		if (this.exceptionAttribute != null) {
//			if (logger.isDebugEnabled()) {
//				logger.debug("Exposing Exception as model attribute '" + this.exceptionAttribute + "'");
//			}
//			mv.addObject(this.exceptionAttribute, ex);
//		}
//		return mv;
		
		Locale locale = request.getLocale();
		if(locale==null) {
			locale = Locale.ENGLISH;
		}
		ModelAndView mv = new ModelAndView(viewName);		
		ErrorResponse errorResponse = getErrorResponse(viewName, ex, request, locale);//
//		mv.getModelMap().addAttribute("response", errorResponse);
		mv.addObject(errorResponse);
		return mv;
	}
	
	protected ErrorResponse getErrorResponse(String viewName, Exception ex, HttpServletRequest request, Locale locale) {
		ErrorResponse errorResponse = getErrorResponseAnalysisByKnownException(viewName, ex, request, locale);//对一些已知典型异常进行处理转换为errorResponse
		if(errorResponse.getCode()==null) {//未匹配到异常信息
			if (ex instanceof IspException) {// 平台级子错误
				IspException ispException = (IspException) ex;
				errorResponse.setCode(ispException.getCode());
				errorResponse.setMsg(ispException.getMsg());
				errorResponse.setDetailCode(ispException.getDetailCode());
				errorResponse.setDetailMsg(ispException.getDetailMsg());
			} else if (ex instanceof IsvException) {// 业务级子错误
				IsvException isvException = (IsvException) ex;
//				errorResponse.setCode(isvException.getCode());
//				errorResponse.setMsg(isvException.getMsg());
				errorResponse.setCode(ErrorMsgUtils.getErrorCode(ErrorType.BUSSINESS_ERROR));
				errorResponse.setMsg(ErrorMsgUtils.getErrorMsg(ErrorType.BUSSINESS_ERROR, locale));
				errorResponse.setDetailCode(isvException.getDetailCode());
				errorResponse.setDetailMsg(isvException.getDetailMsg());
			}
		}
		if(errorResponse!=null&&errorResponse.getCode()==null) {//未知错误
			errorResponse.setCode(ErrorMsgUtils.getErrorCode(ErrorType.SERVICE_CURRENTLY_UNAVAILABLE));
			errorResponse.setMsg(ErrorMsgUtils.getErrorMsg(ErrorType.SERVICE_CURRENTLY_UNAVAILABLE, locale));
			errorResponse.setDetailCode(ErrorMsgUtils.getErrorCode(ErrorType.ISP_UNKNOWN_ERROR));
//			errorResponse.setDetailMsg(ErrorMsgUtils.getErrorMsg(ErrorType.ISP_UNKNOWN_ERROR, locale));
			String errorMsg = ErrorMsgUtils.getErrorMsg(ErrorType.ISP_UNKNOWN_ERROR, locale);
			String stackTrace=ExceptionUtils.getFullStackTrace(ex);
			errorResponse.setDetailMsg(errorMsg + ":" + stackTrace);
		}
		return errorResponse;
	}
	
	
	protected void analysisFiledErrorToErrorResponse(ErrorResponse errorResponse, List<FieldError> fieldErrors, Locale locale) {
		for (FieldError fieldError : fieldErrors) {
			
			if("ApiPermission".equals(fieldError.getCode())) {
				String jsrMsg=fieldError.getDefaultMessage();
				ErrorType errorType = getErrorTypeByJsrMsg(jsrMsg);
				if(errorType!=null) {
					errorResponse.setDetailCode(ErrorMsgUtils.getErrorCode(errorType));
					errorResponse.setDetailMsg(ErrorMsgUtils.getErrorMsg(errorType, new Object[] {fieldError.getField(), fieldError.getRejectedValue()}, locale));
					break;//如果不符合授权请求，则只返回该异常信息
				}
			} else {
				ErrorType errorType = getErrorTypeByJsrMsg(fieldError.getCode());
				if(errorType!=null) {
					String detailCode = MessageFormat.format(ErrorMsgUtils.getErrorCode(errorType), new Object[] { fieldError.getField() });
					errorResponse.setDetailCode(detailCode);
					String defaultMessage = fieldError.getDefaultMessage();
//					if(defaultMessage.startsWith("~")) {
					if(StringUtils.isNoneBlank(defaultMessage)) {
//						defaultMessage = defaultMessage.substring(1);
						String detailMsg = MessageFormat.format(defaultMessage, new Object[] {fieldError.getField(), fieldError.getRejectedValue()});
						errorResponse.setDetailMsg(detailMsg);
					} else {
						errorResponse.setDetailMsg(ErrorMsgUtils.getErrorMsg(errorType, new Object[] {fieldError.getField(), fieldError.getRejectedValue()}, locale));
					}
				} else {
					String detailCode = MessageFormat.format(ErrorMsgUtils.getErrorCode(ErrorType.ISV_INVALID_PARAMETER), new Object[] { fieldError.getField() });
					errorResponse.setDetailCode(detailCode);
					String defaultMessage = fieldError.getDefaultMessage();
					errorResponse.setDetailMsg(defaultMessage);
				}
				
			}
		}
	}
	
	protected ErrorResponse getErrorResponseAnalysisByKnownException(String viewName, Exception ex, HttpServletRequest request, Locale locale) {
		logger.error(ex.toString(),ex);	
		
		ErrorResponse errorResponse = new ErrorResponse();		
		
		if (this.exceptionAttribute != null) {
			if (logger.isDebugEnabled()) {
				logger.debug("Exposing Exception as model attribute '" + this.exceptionAttribute + "'");
			}
			
			if (ex instanceof UnsatisfiedServletRequestParameterException) {// || ex instanceof NoSuchRequestHandlingMethodException
				String method=request.getParameter("method");
				if(method==null||"".equals(method)) {
					errorResponse.setCode(ErrorMsgUtils.getErrorCode(ErrorType.MISSING_METHOD));
					errorResponse.setMsg(ErrorMsgUtils.getErrorMsg(ErrorType.MISSING_METHOD, locale));
				} else {
					errorResponse.setCode(ErrorMsgUtils.getErrorCode(ErrorType.INVALID_METHOD));
					errorResponse.setMsg(ErrorMsgUtils.getErrorMsg(ErrorType.INVALID_METHOD, locale));
				}				
			} else if(ex instanceof BindException) {//参数错误
				errorResponse.setCode(ErrorMsgUtils.getErrorCode(ErrorType.PARAMETER_ERROR));
				errorResponse.setMsg(ErrorMsgUtils.getErrorMsg(ErrorType.PARAMETER_ERROR, locale));
				
				//设置详细错误信息
				List<FieldError> fieldErrors =  ((BindException) ex).getBindingResult().getFieldErrors();
				analysisFiledErrorToErrorResponse(errorResponse,fieldErrors,locale);
			} else if(ex instanceof MethodArgumentNotValidException) {
				errorResponse.setCode(ErrorMsgUtils.getErrorCode(ErrorType.PARAMETER_ERROR));
				errorResponse.setMsg(ErrorMsgUtils.getErrorMsg(ErrorType.PARAMETER_ERROR, locale));
				
				//设置详细错误信息
				List<FieldError> fieldErrors =  ((MethodArgumentNotValidException) ex).getBindingResult().getFieldErrors();
				analysisFiledErrorToErrorResponse(errorResponse,fieldErrors,locale);
			} else if(ex instanceof IllegalStateException) {
				errorResponse.setCode(ErrorMsgUtils.getErrorCode(ErrorType.PARAMETER_ERROR));
				errorResponse.setMsg(ErrorMsgUtils.getErrorMsg(ErrorType.PARAMETER_ERROR, locale));
				IllegalStateException illegalStateException = (IllegalStateException) ex;
				int i;
				//设置详细错误信息
				//TODO: 设置详细错误信息
//				List<FieldError> fieldErrors =  ((IllegalStateException) ex).getBindingResult().getFieldErrors();
//				analysisFiledErrorToErrorResponse(errorResponse,fieldErrors,locale);
			} else if(ex instanceof NullPointerException) {
				errorResponse.setCode(ErrorMsgUtils.getErrorCode(ErrorType.SERVICE_CURRENTLY_UNAVAILABLE));
				errorResponse.setMsg(ErrorMsgUtils.getErrorMsg(ErrorType.SERVICE_CURRENTLY_UNAVAILABLE, locale));
				
				//设置详细错误信息
				errorResponse.setDetailCode(ErrorMsgUtils.getErrorCode(ErrorType.ISP_NULL_POINTER_EXCEPTION));
				errorResponse.setDetailMsg(ErrorMsgUtils.getErrorMsg(ErrorType.ISP_NULL_POINTER_EXCEPTION, locale));
			} else if(ex instanceof HttpRequestMethodNotSupportedException) {
				errorResponse.setCode(ErrorMsgUtils.getErrorCode(ErrorType.HTTP_ACTION_NOT_ALLOWED));
				errorResponse.setMsg(ErrorMsgUtils.getErrorMsg(ErrorType.HTTP_ACTION_NOT_ALLOWED, locale));
				
				//设置详细错误信息
				//TODO: 设置详细错误信息
			}
			
		}		
		
		
		return errorResponse;
	}
	
	private ErrorType getErrorTypeByJsrMsg(String jsrMsg) {
		if(ErrorMsgUtils.JSR_MSG_CONSTRAINT_ERROR_TYPE_MAPPINGS.containsKey(jsrMsg)) {
			ErrorType errorType = ErrorMsgUtils.JSR_MSG_CONSTRAINT_ERROR_TYPE_MAPPINGS.get(jsrMsg);//通过JSR信息得到错误类型
			return errorType;
		} else {
			return null;
		}
	}

}

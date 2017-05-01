package cn.meteor.module.core.openApi.aop;

import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import cn.meteor.module.core.openApi.annotation.ApiMethod;
import cn.meteor.module.core.openApi.exception.IspException;
import cn.meteor.module.core.openApi.exception.IsvException;
import cn.meteor.module.core.openApi.response.ErrorMsgUtils;
import cn.meteor.module.core.openApi.response.ErrorType;

//@Aspect  
//@Component
public class ApiAspectManager {

	// Controller层切点
	@Pointcut("@annotation(cn.meteor.module.core.openApi.annotation.ApiMethod)")
	public void apiControllerAspect() {
	}

//	// Service层切点
//	@Pointcut("@annotation(cn.meteor.module.core.openApi.aop.ApiServiceAspect)")
//	public void serviceAspect() {
//	}
    
//    /** 
//     * 前置通知 用于拦截Controller层记录用户的操作 
//     *  
//     * @param joinPoint 
//     *            切点 
//     */  
//    @Before("controllerAspect()")  
//    public void doBefore(JoinPoint joinPoint) {
//    	HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder .getRequestAttributes()).getRequest();
//    	HttpSession session = request.getSession();
//    }
    
//    /** 
//     * 异常通知 用于拦截Controller层记录异常日志 
//     *  
//     * @param joinPoint 
//     * @param e 
//     */  
//    @AfterThrowing(pointcut = "controllerAspect()", throwing = "e")  
//    public void doAfterControllerThrowing(JoinPoint joinPoint, Throwable e) {
//    	HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder .getRequestAttributes()).getRequest();
//    	HttpSession session = request.getSession();
//    	
//    	 /*========控制台输出=========*/  
//        System.out.println("=====异常通知开始=====");  
//        System.out.println("异常代码:" + e.getClass().getName());  
//        System.out.println("异常信息:" + e.getMessage());  
//        System.out.println("异常方法:" + (joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName() + "()"));  
////        System.out.println("方法描述:" + getServiceMthodDescription(joinPoint));  
////        System.out.println("请求人:" + user.getName());  
////        System.out.println("请求IP:" + ip);  
////        System.out.println("请求参数:" + params); 
//        
//       
//    	
//    }
    
//    /** 
//     * 异常通知 用于拦截Service层记录异常日志 
//     *  
//     * @param joinPoint 
//     * @param e 
//     */  
//    @AfterThrowing(pointcut = "serviceAspect()", throwing = "e")  
//    public void doAfterServiceThrowing(JoinPoint joinPoint, Throwable e) throws Exception {
//    	HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
//    	HttpSession session = request.getSession();
//    	
//    	 /*========控制台输出=========*/  
//        System.out.println("=====异常通知开始=====");  
//        System.out.println("异常代码:" + e.getClass().getName());  
//        System.out.println("异常信息:" + e.getMessage());  
//        System.out.println("异常方法:" + (joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName() + "()"));  
////        System.out.println("方法描述:" + getServiceMthodDescription(joinPoint));  
////        System.out.println("请求人:" + user.getName());  
////        System.out.println("请求IP:" + ip);  
////        System.out.println("请求参数:" + params);
//        
//        
//        Locale locale = request.getLocale();
//		if(locale==null) {
//			locale = Locale.ENGLISH;
//		}
//        if(e instanceof EmptyResultDataAccessException) {
//        	String detailCode = ErrorMsgUtils.getErrorCode(ErrorType.ISV_NOT_EXIST);
//        	String detailMsg = ErrorMsgUtils.getErrorMsg(ErrorType.ISV_NOT_EXIST, new Object[] { "", "数据" }, locale);
//
//            IsvException isvException = new IsvException(detailCode, detailMsg);
//    		throw isvException;
//        }
//    	
//    }

	
	
	private  static String getApiMethodValues(String targetName, String methodName, Object[] arguments)  throws Exception {
//        String targetName = joinPoint.getTarget().getClass().getName();
//        String methodName = joinPoint.getSignature().getName();
//        Object[] arguments = joinPoint.getArgs();
        Class targetClass = Class.forName(targetName);
        Method[] methods = targetClass.getMethods();
        String[] values = null;
         for (Method method : methods) {
             if (method.getName().equals(methodName)) {
                Class[] clazzs = method.getParameterTypes();
                 if (clazzs.length == arguments.length) {
                	 values = method.getAnnotation(ApiMethod.class).value();
                     break;
                }
            }
        }
         return values[0];
    }
    
    
	@Around("apiControllerAspect()")
	public Object arroundApiController(ProceedingJoinPoint pjp) throws Throwable {
		Object result = null;
		
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
//		HttpSession session = request.getSession();
		Locale locale = request.getLocale();
		if (locale == null) {
			locale = Locale.ENGLISH;
		}
		try {
			result = pjp.proceed();
		} catch(IspException ispException) {
			String annotationMethodName =  getApiMethodValues(pjp.getTarget().getClass().getName(),pjp.getSignature().getName(), pjp.getArgs());
			ispException.setCode(ErrorMsgUtils.getErrorCode(ErrorType.SERVICE_CURRENTLY_UNAVAILABLE));
			ispException.setMsg(ErrorMsgUtils.getErrorMsg(ErrorType.SERVICE_CURRENTLY_UNAVAILABLE, locale));
			ispException.setDetailCode(ErrorMsgUtils.getErrorCode(ErrorType.ISP_SERVICE_UNAVAILABLE).replace("***", annotationMethodName));
//			ispException.setDetailMsg(detailMsg);
			throw ispException;
		} catch (EmptyResultDataAccessException emptyResultDataAccessException) {
			String annotationMethodName =  getApiMethodValues(pjp.getTarget().getClass().getName(),pjp.getSignature().getName(), pjp.getArgs());
			String errorMsg=emptyResultDataAccessException.getMessage();
			String dataNotFoundErrorMsg = errorMsg.substring(errorMsg.indexOf("with")+4,errorMsg.indexOf("exists"));//字符串截取
			String code = ErrorMsgUtils.getErrorCode(ErrorType.INVALID_BUSINESS_PARAMETER);
			String msg = ErrorMsgUtils.getErrorMsg(ErrorType.INVALID_BUSINESS_PARAMETER, locale);
//			String detailCode = ErrorMsgUtils.getErrorCode(ErrorType.ISV_NOT_EXIST).replace("###", annotationMethodName).replace("***", dataNotFoundErrorMsg);
			String detailCode = MessageFormat.format(ErrorMsgUtils.getErrorCode(ErrorType.ISV_NOT_EXIST), new Object[] { dataNotFoundErrorMsg, annotationMethodName });
			String detailMsg = ErrorMsgUtils.getErrorMsg(ErrorType.ISV_NOT_EXIST, new Object[] { dataNotFoundErrorMsg, annotationMethodName }, locale);

			IsvException isvException = new IsvException(code, msg, detailCode, detailMsg);
			throw isvException;
		} catch (DataIntegrityViolationException dataIntegrityViolationException) {//IntegrityConstraintViolation 违反-完整性约束	
			//这里我们预期前面已做了入参验证，剩下的问题都是后端处理问题，故这种异常我们归类为IspException
			String annotationMethodName =  getApiMethodValues(pjp.getTarget().getClass().getName(),pjp.getSignature().getName(), pjp.getArgs());
			String rootCauseMessage = dataIntegrityViolationException.getRootCause().getMessage();
			IspException ispException = new IspException();
			ispException.setCode(ErrorMsgUtils.getErrorCode(ErrorType.SERVICE_CURRENTLY_UNAVAILABLE));
			ispException.setMsg(ErrorMsgUtils.getErrorMsg(ErrorType.SERVICE_CURRENTLY_UNAVAILABLE, locale));
			ispException.setDetailCode(ErrorMsgUtils.getErrorCode(ErrorType.ISP_SERVICE_UNAVAILABLE).replace("***", annotationMethodName));
			ispException.setDetailMsg(rootCauseMessage);
			throw ispException;
		}
		
		return result;
    	
//    	 /*========控制台输出=========*/  
//        System.out.println("=====异常通知开始=====");  
//        System.out.println("异常代码:" + e.getClass().getName());  
//        System.out.println("异常信息:" + e.getMessage());  
//        System.out.println("异常方法:" + (joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName() + "()"));  
////        System.out.println("方法描述:" + getServiceMthodDescription(joinPoint));  
////        System.out.println("请求人:" + user.getName());  
////        System.out.println("请求IP:" + ip);  
////        System.out.println("请求参数:" + params);
    	
    }
    
}

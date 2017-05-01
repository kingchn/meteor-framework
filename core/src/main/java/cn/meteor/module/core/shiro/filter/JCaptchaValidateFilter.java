package cn.meteor.module.core.shiro.filter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;

public class JCaptchaValidateFilter extends AccessControlFilter {
    private boolean jcaptchaEnabled = true;//是否开启验证码支持

	private String jcaptchaParam = "captcha";//前台提交的验证码参数名
    private String jcaptchaSessionAttribute = "jcaptchaSession";//验证码设置的session属性名


	private String failureKeyAttribute = "shiroLoginFailure"; //验证失败后存储到的属性名

    public void setJcaptchaEnabled(boolean jcaptchaEnabled) {
		this.jcaptchaEnabled = jcaptchaEnabled;
	}
    
    public void setJcaptchaParam(String jcaptchaParam) {
        this.jcaptchaParam = jcaptchaParam;
    }

	public void setJcaptchaSessionAttribute(String jcaptchaSessionAttribute) {
		this.jcaptchaSessionAttribute = jcaptchaSessionAttribute;
	}    
    
    public void setFailureKeyAttribute(String failureKeyAttribute) {
        this.failureKeyAttribute = failureKeyAttribute;
    }
    
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        //1、设置验证码是否开启属性，页面可以根据该属性来决定是否显示验证码
        request.setAttribute("jcaptchaEbabled", jcaptchaEnabled);

        HttpServletRequest httpServletRequest = WebUtils.toHttp(request);
        //2、判断验证码是否禁用 或 同步form表单提交（如果没开启验证码 或 非ajax请求且get提交 允许访问），这就要求登录必须post提交
//      return JCaptcha.validateResponse(httpServletRequest, httpServletRequest.getParameter(jcaptchaParam));
        boolean isAjaxRequest =  "XMLHttpRequest".equalsIgnoreCase(httpServletRequest.getHeader("X-Requested-With"));
        boolean isGetRequest = "get".equalsIgnoreCase(httpServletRequest.getMethod());
        if (jcaptchaEnabled == false || (!isAjaxRequest &&  isGetRequest)  ) {
            return true;
        }
        
        //3、此时是表单提交，验证验证码是否正确
        String requestCaptcha = httpServletRequest.getParameter(jcaptchaParam);
        String sessionCaptcha = (String) httpServletRequest.getSession().getAttribute(jcaptchaSessionAttribute);
        if(sessionCaptcha!=null && sessionCaptcha.equals(requestCaptcha.toLowerCase())) {
        	return true;//验证码相等，通过
        } else {
        	return false;
        }
    }
    
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        //如果验证码失败了，存储失败key属性
        request.setAttribute(failureKeyAttribute, "jCaptcha.error");
        return true;
    }
}


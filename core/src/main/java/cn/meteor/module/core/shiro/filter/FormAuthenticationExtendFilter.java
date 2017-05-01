package cn.meteor.module.core.shiro.filter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;

public class FormAuthenticationExtendFilter extends FormAuthenticationFilter {
	

	@Override
	protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request, ServletResponse response) throws Exception {
//		 issueSuccessRedirect(request, response);
		
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
//		HttpServletResponse httpServletResponse = (HttpServletResponse) response;
		if ("XMLHttpRequest".equalsIgnoreCase(httpServletRequest.getHeader("X-Requested-With"))) {//如果是ajax请求，则返回到controller
			return true;
		} else {//否则执行重定向
			issueSuccessRedirect(request, response);
		}
		 
//		 String username = (String)SecurityUtils.getSubject().getPrincipal();
//			if(username!=null) {
//				MemberUser memberUser= memberUserService.selectOneByUsername(username);
//				request.setAttribute(Constants.CURRENT_USER, memberUser);
//				
//				HttpServletRequest httpServletRequest = (HttpServletRequest) request;
//				httpServletRequest.getSession().setAttribute(Constants.CURRENT_USER_NICK_NAME, memberUser.getNickName());
//			}
		 
	        //we handled the success redirect directly, prevent the chain from continuing:
		 return false;
	}

	@Override
	protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {
//		if (log.isDebugEnabled()) {
//            log.debug( "Authentication exception", e );
//        }
        setFailureAttribute(request, e);
		
        //login failed, let request continue back to the login page:
        return true;
	}

	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
		if(request.getAttribute(getFailureKeyAttribute()) != null) {//当验证码验证失败时不再走身份认证拦截器
            return true;
        }
		return super.onAccessDenied(request, response);
	}

}

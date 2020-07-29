package cn.meteor.module.core.security.csrf.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

/**
 * CSRF（Cross-site request forgery）跨站请求伪造 漏洞处理过滤器
 * @author shenjc
 *
 */
//@WebFilter(filterName="csrfFilter",urlPatterns="/*")
public class CSRFFilter implements Filter {

	/**
	 * 可信的referer的host，格式为域名或者ip 多个以分号隔开
	 */
//	@Value("${core.security.csrf.trust.hosts}")
	private String csrfTrustHosts = "127.0.0.1;localhost";

	public void setCsrfTrustHosts(String csrfTrustHosts) {
		this.csrfTrustHosts = csrfTrustHosts;
	}

	/**
	 * 忽略的url前缀，格式如/xxx (不包含contextPath) 多个以分号隔开
	 */
	//	@Value("${core.security.csrf.ignore.urls}")
	private String csrfIgnoreUrls;

	public void setCsrfIgnoreUrls(String csrfIgnoreUrls) {
		this.csrfIgnoreUrls = csrfIgnoreUrls;
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;

		boolean isPass = false;// 是否通过

		//若有配置忽略url，且当前请求url匹配，则标记忽略
		boolean isIgnoreUrl = false;// 是否当前请求的url是忽略不处理
		String contextPath = httpServletRequest.getContextPath();
		if (StringUtils.isNotBlank(csrfIgnoreUrls)) {
			String[] csrfIgnoreUrlList = csrfIgnoreUrls.split(";");
			if (csrfIgnoreUrlList != null) {
				for (String ignoreUrl : csrfIgnoreUrlList) {
					ignoreUrl = contextPath + ignoreUrl;
					if (httpServletRequest.getRequestURI().startsWith(ignoreUrl)) {
						isIgnoreUrl = true;
						break;
					}
				}
			}
		}	

		String referer = httpServletRequest.getHeader("Referer");
		if (StringUtils.isBlank(csrfTrustHosts) || StringUtils.isBlank(referer) || isIgnoreUrl) {// 如果没有配置csrfTrustHosts 或者 referer为空、或者当前请求的url是忽略不处理的，则直接通过
			isPass = true;
		} else {// 如果有配置csrfTrustHosts 且 referer在这个范围内，则直接通过
			String[] csrfTrustHostList = csrfTrustHosts.split(";");
			if (csrfTrustHostList != null) {
				for (String host : csrfTrustHostList) {
					if (referer.contains(host)) {
						isPass = true;
						break;
					}
				}
			}
		}
		
		if (isPass == true) {
			chain.doFilter(request, response);
		}
	}

}
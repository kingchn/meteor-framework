package cn.meteor.module.core.rest.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.meteor.module.core.rest.response.RestCommonResponse;

public interface RestExceptionHandler {

	RestCommonResponse handleException(HttpServletRequest req, HttpServletResponse httpServletResponse, Exception ex);
	
}

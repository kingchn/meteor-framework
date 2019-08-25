package cn.meteor.module.core.rest.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.meteor.module.core.rest.response.RestCommonResponse;

public interface RestExceptionHandler {

	RestCommonResponse handleException(HttpServletRequest req, HttpServletResponse httpServletResponse, Exception ex);
	
}

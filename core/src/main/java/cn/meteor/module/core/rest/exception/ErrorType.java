package cn.meteor.module.core.rest.exception;

public enum ErrorType {
	
	//错误码小于100的平台级错误
	SERVICE_CURRENTLY_UNAVAILABLE,
	PARAMETER_ERROR,
	
	INVALID_PARAM_SERVICE_ID,	//无效serviceId
	INSUFFICIENT_USER_PERMISSIONS,	//用户权限不足
	INVALID_PARAM_REQUEST_BODY_FORMAT,	//参数指定无效请求体数据格式
	INVALID_REQUEST_MAIN_DATA_FORMAT,		//完整请求报文数据格式不正确
	INVALID_REQUEST_BODY_DATA_FORMAT,	//请求实体body报文数据格式不正确
	
	BUSSINESS_ERROR,//业务级错误(父级)
	
	
	//平台级子错误
	ISP_SERVICE_UNAVAILABLE,	//调用后端服务抛异常，服务不可用
	ISP_NULL_POINTER_EXCEPTION,	//空指针异常错误
	ISP_UNKNOWN_ERROR,	//未知异常信息
	
	

	//独立软件开发商 业务级子错误
	ISV_NOT_EXIST,
    ISV_MISSING_PARAMETER,
    ISV_PARAMETER_BLANK,
    ISV_INVALID_PARAMETER,
    ISV_REQUEST_DATA_PARSE_ERROR,	//请求数据解析错误
    ISV_INVALID_PERMISSION,	//权限不够、非法访问
    
}

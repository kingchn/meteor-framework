package cn.meteor.module.core.rest.controller;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

import cn.meteor.module.core.rest.annotation.RestMethod;
import cn.meteor.module.core.rest.container.RestContainer;
import cn.meteor.module.core.rest.request.RestBodyRequest;
import cn.meteor.module.core.rest.request.RestCommonRequest;
import cn.meteor.module.core.rest.response.RestCommonResponse;
import cn.meteor.module.core.rest.utils.AuthorizingUtils;
import cn.meteor.module.core.rest.utils.RestRequestUtils;
import cn.meteor.module.core.rest.vo.RestApiVo;

@RequestMapping("${core.rest.rootPath}")
@RestController
public class RestApiController {
	
	private static final Logger logger = LogManager.getLogger(RestApiController.class);

	@Autowired
	private RestContainer restContainer;

	@RequestMapping(value="")
	@ResponseBody
//	public RestCommonResponse doService(@Valid @RequestBody RestCommonRequest restCommonRequest) throws Exception {
	public RestCommonResponse doService(HttpServletRequest request) throws Exception {
		RestCommonResponse restCommonResponse = new RestCommonResponse();
		
		RestCommonRequest restCommonRequest = null;
		Object restCommonRequestObject = request.getAttribute("restCommonRequest");
		if (restCommonRequestObject != null) {
			restCommonRequest = (RestCommonRequest) restCommonRequestObject;
			request.removeAttribute("restCommonRequest");
		}
		
		RestApiVo restApiVo = null;
		Object restApiVoObject = request.getAttribute("restApiVo");
		if (restCommonRequestObject != null) {
			restApiVo = (RestApiVo) restApiVoObject;
			request.removeAttribute("restApiVo");
		}
		
		String methodKey = restApiVo.getMethodKey();
		Object service = restApiVo.getService();
		Method method = restApiVo.getMethod();
		
		//获取请求报文中body的原始字符串。base64则为base64字符串；json则为json字符串
		String requestBodyJsonString = RestRequestUtils.getRequestJsonBodyString(restCommonRequest, restContainer.getObjectMapper());				
		
		//用户权限控制 PermissionAnnotationMethodInterceptor
		RequiresPermissions requiresPermissions = restContainer.getRequiresPermissionsAnnotationMap().get(methodKey);		
		AuthorizingUtils.assertAuthorized(requiresPermissions);		
		
		RestMethod restMethod = restContainer.getRestMethodAnnotationMap().get(methodKey);
		
		
		Class<?>[] cls = method.getParameterTypes();
		Object result = null;
	
		if (cls == null || cls.length == 0) {
			result = method.invoke(service);
		} else {
			Class<?> paramClazz = method.getParameterTypes()[0];
			Object param = null;
			
			//根据请求参数body的格式类型，通过请求body的数据转化来设置param的值
			if(restCommonRequest.getBody()!=null) {//_reqBodyFormat 为base64
				if("base64".equals(restCommonRequest.getRequestBodyFormat())) {
					String bodyJsonBase64 = requestBodyJsonString;
					
					boolean isRestBodyRequest = false;
					if(StringUtils.isNotBlank(bodyJsonBase64)) {
						String bodyJson = new String(Base64.decodeBase64(bodyJsonBase64));
						if (paramClazz.isAssignableFrom(List.class)) {
//							Class<? extends Type> clz = m.getGenericParameterTypes()[0].getClass();
//							param = JSON.parseArray(restCommonRequestBodyContent, clz);
							//TODO:
						} else {							
							//获取参数类型
//							Class<?>[] parameterTypes =m.getParameterTypes();
							Type[] typeArr =method.getGenericParameterTypes();
							Type type = typeArr[0];
							
							//判断是否参数类型是RestBodyRequest<T>，并做相应处理(如果是，则json直接反序列化到RestBodyRequest<T>的data字段)
							if (type instanceof ParameterizedType) {
								ParameterizedType pt = (ParameterizedType) type;
								Type rawType = pt.getRawType();
								Type[] actualTypeArr = pt.getActualTypeArguments();
								Type actualType = actualTypeArr[0];
								if(rawType.getTypeName().equals(RestBodyRequest.class.getCanonicalName())) {//参数类型是RestBodyRequest<T>
									isRestBodyRequest = true;
									JavaType actualJavaType = TypeFactory.defaultInstance().constructType(actualType);//RestBodyRequest<T>的T的JavaType
									Object data = restContainer.getObjectMapper().readValue(bodyJson, actualJavaType);
									RestBodyRequest restBodyRequest = new RestBodyRequest();
									restBodyRequest.setData(data);
									param = restBodyRequest;
								}
							}
							
							if (isRestBodyRequest == false) {// 如果参数类型不是RestBodyRequest<T>，则按一般处理，json反序列化对应整个RestBodyRequest<T>
								JavaType javaType = TypeFactory.defaultInstance().constructType(type);
								param = restContainer.getObjectMapper().readValue(bodyJson, javaType);
							}
						}
					}
				} else {//默认json对象（明文）
//					if( !(restCommonRequest.getBody() instanceof LinkedHashMap) ) {//如果不是LinkedHashMap，即body节点的报文不是json对象结构数据的情况
//						ErrorMsgUtils.throwIsvException(ErrorType.INVALID_REQUEST_BODY_DATA_FORMAT, new Object[] { "body不是json对象结构数据" });
//					}
					
					//获取参数类型
//					Class<?>[] parameterTypes =m.getParameterTypes();
					Type[] typeArr = method.getGenericParameterTypes();
					Type type = typeArr[0];
					
					//将body转为json，再将json转为对象赋值给param
//					byte[] bodyJsonBytes = objectMapper.writeValueAsBytes(restCommonRequest.getBody());
//					String bodyJsonString = objectMapper.writeValueAsString(restCommonRequest.getBody());
					String bodyJsonString = requestBodyJsonString;
					
					boolean isRestBodyRequest = false;
					//判断是否参数类型是RestBodyRequest<T>，并做相应处理(如果是，则json直接反序列化到RestBodyRequest<T>的data字段)
					if (type instanceof ParameterizedType) {
						ParameterizedType pt = (ParameterizedType) type;
						Type rawType = pt.getRawType();
						Type[] actualTypeArr = pt.getActualTypeArguments();
						Type actualType = actualTypeArr[0];
						if(rawType.getTypeName().equals(RestBodyRequest.class.getCanonicalName())) {//参数类型是RestBodyRequest<T>
							isRestBodyRequest = true;
							JavaType actualJavaType = TypeFactory.defaultInstance().constructType(actualType);//RestBodyRequest<T>的T的JavaType
							Object data = restContainer.getObjectMapper().readValue(bodyJsonString, actualJavaType);
							RestBodyRequest restBodyRequest = new RestBodyRequest();
							restBodyRequest.setData(data);
							param = restBodyRequest;
						}
					}
					
					if(isRestBodyRequest == false) {//如果参数类型不是RestBodyRequest<T>，则按一般处理，json反序列化对应整个RestBodyRequest<T>
						JavaType javaType = TypeFactory.defaultInstance().constructType(type);					
						param = restContainer.getObjectMapper().readValue(bodyJsonString, javaType);
					}
				}
			}
			
			//校验业务请求实体
			if(param !=null)
				RestRequestUtils.validBusinessRequest(param);
			
			//执行业务接口
			result = method.invoke(service, param);
		}
		
		//响应处理
		//响应的body的格式： json、base64；如果没指定响应的body的格式，则使用请求中指定的；请求中指定的格式默认为base64
		String responseBodyFormat = restMethod.responseBodyFormat();
		if("".contentEquals(restMethod.responseBodyFormat())) {//如果responseBodyFormat没设置值，则使用requestBodyFormat的值
			responseBodyFormat = restCommonRequest.getRequestBodyFormat();
		}
		
		if("json".equals(responseBodyFormat)) {
			restCommonResponse.setBody(result);
		} else {
			if(result instanceof byte[]) {
				byte[] bytes = (byte[]) result;
				String contentBase64 =Base64.encodeBase64String(bytes);
				restCommonResponse.setBody(contentBase64);
			} else {
				ObjectMapper objectMapper = new ObjectMapper();
				objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);//只显示非空字段
				String contentJson = objectMapper.writeValueAsString(result);
				String contentBase64 =Base64.encodeBase64String(contentJson.getBytes());
				restCommonResponse.setBody(contentBase64);
			}
		}
		
		restCommonResponse.setCode("1");
		restCommonResponse.setMsg("success");
		restCommonResponse.setResponseBodyFormat(responseBodyFormat);
		if("base64".equals(restCommonResponse.getResponseBodyFormat())) {//如果是默认的base64，则返回null，表示默认值，即base64
			restCommonResponse.setResponseBodyFormat(null);
		}
		restCommonResponse.setTimestamp((new Date()).getTime());
				
		return restCommonResponse;
	}
}

package cn.meteor.module.core.openApi.handler;

/*
 * Copyright 2002-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

//package org.springframework.web.servlet.mvc.method.annotation;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.Conventions;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.annotation.ModelFactory;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import com.fasterxml.jackson.databind.SerializationFeature;

import cn.meteor.module.core.openApi.DataTablesOutput;
import cn.meteor.module.core.openApi.annotation.RestRequestBody;
import cn.meteor.module.core.openApi.annotation.RestResponseBody;


public class RestRequestResponseBodyMethodProcessor extends RequestResponseBodyMethodProcessor {
	
	
	/**
	 * Basic constructor with converters only. Suitable for resolving
	 * {@code @RequestBody}. For handling {@code @ResponseBody} consider also
	 * providing a {@code ContentNegotiationManager}.
	 */
	public RestRequestResponseBodyMethodProcessor(List<HttpMessageConverter<?>> converters) {
		super(converters);
	}

	/**
	 * Basic constructor with converters and {@code ContentNegotiationManager}.
	 * Suitable for resolving {@code @RequestBody} and handling
	 * {@code @ResponseBody} without {@code Request~} or
	 * {@code ResponseBodyAdvice}.
	 */
	public RestRequestResponseBodyMethodProcessor(List<HttpMessageConverter<?>> converters,
			ContentNegotiationManager manager) {

		super(converters, manager);
	}

	/**
	 * Complete constructor for resolving {@code @RequestBody} method arguments.
	 * For handling {@code @ResponseBody} consider also providing a
	 * {@code ContentNegotiationManager}.
	 * @since 4.2
	 */
	public RestRequestResponseBodyMethodProcessor(List<HttpMessageConverter<?>> converters,
			List<Object> requestResponseBodyAdvice) {

		super(converters, null, requestResponseBodyAdvice);
	}

	/**
	 * Complete constructor for resolving {@code @RequestBody} and handling
	 * {@code @ResponseBody}.
	 */
	public RestRequestResponseBodyMethodProcessor(List<HttpMessageConverter<?>> converters,
			ContentNegotiationManager manager, List<Object> requestResponseBodyAdvice) {

		super(converters, manager, requestResponseBodyAdvice);
	}


	@Override
	public boolean supportsParameter(MethodParameter parameter) {
//		return parameter.hasParameterAnnotation(RequestBody.class);
		return parameter.hasParameterAnnotation(RestRequestBody.class);		//匹配RestRequestBody注解		modify by shenjc
	}

	@Override
	public boolean supportsReturnType(MethodParameter returnType) {
		return (AnnotationUtils.findAnnotation(returnType.getContainingClass(), RestResponseBody.class) != null ||
				returnType.getMethodAnnotation(RestResponseBody.class) != null);	//匹配RestResponseBody注解		modify by shenjc
	}

	/**
	 * Throws MethodArgumentNotValidException if validation fails.
	 * @throws HttpMessageNotReadableException if {@link RequestBody#required()}
	 * is {@code true} and there is no body content or if there is no suitable
	 * converter to read the content with.
	 */
	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

		Object arg = readWithMessageConverters(webRequest, parameter, parameter.getGenericParameterType());
		String name = Conventions.getVariableNameForParameter(parameter);

		WebDataBinder binder = binderFactory.createBinder(webRequest, arg, name);
		if (arg != null) {
			validateIfApplicable(binder, parameter);
			if (binder.getBindingResult().hasErrors() && isBindExceptionRequired(binder, parameter)) {
				throw new MethodArgumentNotValidException(parameter, binder.getBindingResult());
			}
		}
		mavContainer.addAttribute(BindingResult.MODEL_KEY_PREFIX + name, binder.getBindingResult());

		return arg;
	}

	@Override
	protected <T> Object readWithMessageConverters(NativeWebRequest webRequest, MethodParameter methodParam,
			Type paramType) throws IOException, HttpMediaTypeNotSupportedException, HttpMessageNotReadableException {

		HttpServletRequest servletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
		ServletServerHttpRequest inputMessage = new ServletServerHttpRequest(servletRequest);

		Object arg = readWithMessageConverters(inputMessage, methodParam, paramType);
		if (arg == null) {
			if (methodParam.getParameterAnnotation(RequestBody.class).required()) {
				throw new HttpMessageNotReadableException("Required request body is missing: " +
						methodParam.getMethod().toGenericString());
			}
		}
		return arg;
	}

	@Override
	public void handleReturnValue(Object returnValue, MethodParameter returnType,
			ModelAndViewContainer mavContainer, NativeWebRequest webRequest)
			throws IOException, HttpMediaTypeNotAcceptableException, HttpMessageNotWritableException {

		mavContainer.setRequestHandled(true);
		
//		//在启用SerializationFeature.WRAP_ROOT_VALUE, true不用这块代码了
//		if(returnType.getMethodAnnotation(RestResponseBody.class) != null) {//特殊处理，如果是rest响应对象，则返回rest的数据结构 add by shenjc			
////			RestResponseBody restResponseBody = returnType.getMethodAnnotation(RestResponseBody.class);
////			String restResponseBodyValue = restResponseBody.value();//取得注解的值，用于序列化成json或者xml的响应标签名称
////			HashMap<String, Object> restOutput = new HashMap<String, Object>();
////			restOutput.put(restResponseBodyValue, returnValue);			
//////			Class<?> returnValueClass = getReturnValueType(restOutput, returnType);			
////			writeWithMessageConverters(restOutput, returnType, webRequest);
//			writeWithMessageConverters(returnValue, returnType, webRequest);
//			return;
//		}
//		if(returnType.getMethodAnnotation(RestResponseBody.class) != null) {//特殊处理，如果是rest响应对象，则返回rest的数据结构 add by shenjc
////			ModelAndView modelAndView = new ModelAndView();
////			modelAndView.addObject(attributeValue)
//			ModelMap modelMap = new ModelMap();
//			modelMap.addAttribute(returnValue);
//			writeWithMessageConverters(modelMap, returnType, webRequest);
//			return;
//		}
		
//		if(returnValue instanceof Page<?>) {//特殊处理，如果是spring分页对象，则返回兼容dataTable的数据结构 add by shenjc
//			Page<?> page = (Page<?>) returnValue;
////			Map<String,Object> dataTableMap = new HashMap<String,Object>();
////	        dataTableMap.put("aaData",page.getContent());
////	        dataTableMap.put("iTotalRecords",page.getTotalElements());
////	        dataTableMap.put("iTotalDisplayRecords", page.getTotalElements());
////			dataTableMap.put("sEcho",sEcho);
////			dataTableMap.put("data",page.getContent());
////	        dataTableMap.put("recordsTotal",page.getTotalElements());
////	        dataTableMap.put("recordsFiltered", page.getTotalElements());
////			https://datatables.net/manual/server-side#Returned-data
//			
//			DataTablesOutput dataTablesOutput = new DataTablesOutput();
//			dataTablesOutput.setData(page.getContent());
//			dataTablesOutput.setRecordsTotal(page.getTotalElements());
//			dataTablesOutput.setRecordsFiltered(page.getTotalElements());
//			writeWithMessageConverters(dataTablesOutput, returnType, webRequest);
//			
////			HashMap<String, Object> restOutput = new HashMap<String, Object>();
////			restOutput.put("data", page.getContent());
////			restOutput.put("recordsTotal", page.getTotalElements());
////			restOutput.put("recordsFiltered", page.getTotalElements());
////			writeWithMessageConverters(restOutput, returnType, webRequest);
//	        
//	        return;
//		}
		

		// Try even with null return value. ResponseBodyAdvice could get involved.
		writeWithMessageConverters(returnValue, returnType, webRequest);
	}

}


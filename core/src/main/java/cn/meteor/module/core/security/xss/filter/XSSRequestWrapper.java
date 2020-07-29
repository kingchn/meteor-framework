package cn.meteor.module.core.security.xss.filter;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import cn.meteor.module.core.openApi.servlet.BodyReaderHttpServletRequestWrapper;

public class XSSRequestWrapper extends HttpServletRequestWrapper {	
	

	private HttpServletRequest httpServletRequest;
	
    public XSSRequestWrapper(HttpServletRequest servletRequest) {
        super(servletRequest);
        this.httpServletRequest = servletRequest;
    }

    @Override
    public String[] getParameterValues(String parameter) {
        String[] values = super.getParameterValues(parameter);
        if (values == null) {
            return null;
        }
        int count = values.length;
        String[] encodedValues = new String[count];
        for (int i = 0; i < count; i++) {
            encodedValues[i] = XSSUtils.stripXSS(values[i]);
        }

        return encodedValues;
    }

    @Override
    public String getParameter(String parameter) {
        String value = super.getParameter(parameter);
        if (value == null) {
            return null;
        }
        return XSSUtils.stripXSS(value);
    }

    @Override
    public String getHeader(String name) {
        String value = super.getHeader(name);
        if("Referer".equals(name)) {//如果是获取引用页url，则不做处理
            return value;
        } else {
            return XSSUtils.stripXSS(value);
        }
    }    
    
	@Override
	public ServletInputStream getInputStream() throws IOException {
		String contentType = httpServletRequest.getContentType();
		if (StringUtils.isNotEmpty(contentType) && contentType.toLowerCase().contains("json")) {
			String encoding = "utf-8";
			ServletRequest requestWrapper = new BodyReaderHttpServletRequestWrapper(httpServletRequest, encoding);// 保证request.getInputStream()可以重复调用

			String requestBody = IOUtils.toString(requestWrapper.getInputStream(), encoding);
			requestBody = XSSUtils.stripXSSForJson(requestBody);

			byte[] body = requestBody.getBytes(encoding);

			final ByteArrayInputStream bais = new ByteArrayInputStream(body);
			return new ServletInputStream() {

				@Override
				public int read() throws IOException {
					return bais.read();
				}
			};
		} else {
			return super.getInputStream();
		}
	}
}

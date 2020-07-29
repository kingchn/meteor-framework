package cn.meteor.module.core.security.xss.filter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.regex.Pattern;

import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;

import cn.meteor.module.core.openApi.servlet.BodyReaderHttpServletRequestWrapper;

public class XSSRequestWrapper extends HttpServletRequestWrapper {
	
	public static Pattern[] patterns = new Pattern[]{
            // Script fragments
            Pattern.compile("<script>.*</script>", Pattern.CASE_INSENSITIVE),
            Pattern.compile("<script>(.*?)</script>", Pattern.CASE_INSENSITIVE),
            // src='...'
            Pattern.compile("src[\r\n]*=[\r\n]*\\\'(.*?)\\\'", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            Pattern.compile("src[\r\n]*=[\r\n]*\\\"(.*?)\\\"", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            Pattern.compile("<(.*?)src(.*?)>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE	| Pattern.DOTALL),//新增
            // lonely script tags
            Pattern.compile("</script>", Pattern.CASE_INSENSITIVE),
            Pattern.compile("<script(.*?)>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            // img
            Pattern.compile("<img(.*?)>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),//新增
            // eval(...)
            Pattern.compile("eval\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            // expression(...)
            Pattern.compile("expression\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            // javascript:...
            Pattern.compile("javascript:", Pattern.CASE_INSENSITIVE),
            // vbscript:...
            Pattern.compile("vbscript:", Pattern.CASE_INSENSITIVE),
            // onload(...)=...
            Pattern.compile("onload(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL)
    };

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
            encodedValues[i] = stripXSS(values[i]);
        }

        return encodedValues;
    }

    @Override
    public String getParameter(String parameter) {
        String value = super.getParameter(parameter);
        if (value == null) {
            return null;
        }
        return stripXSS(value);
    }

    @Override
    public String getHeader(String name) {
        String value = super.getHeader(name);
        if("Referer".equals(name)) {//如果是获取引用页url，则不做处理
            return value;
        } else {
            return stripXSS(value);
        }
    }

    private String stripXSS(String value) {
        if (value != null) {
            // NOTE: It's highly recommended to use the ESAPI library and uncomment the following line to
            // avoid encoded attacks.
            //value = ESAPI.encoder().canonicalize(value);
//        	value = ESAPI.encoder().encodeForHTMLAttribute(value);

//        	StringEscapeUtils.escapeHtml(value);     

            // Avoid null characters
            value = value.replaceAll("\0", "");

            value = value.replaceAll("/\\*(.*?)\\*/", "");//	/* */避免这种攻击：{{url}}/toRegisterThree?id=" STYLE="xss:e/**/xpression(try{a=firstTime}catch(e){firstTime=1;alert(5730)});
//            
//
//            value = value.replaceAll("\"(.*?)=", "");//		{{url}}/toRegisterThree?id="onmouseover="alert(15)"      
//            
//            value = value.replaceAll("<img(.*?)>", "");//			{{url}}/toRegisterThree?id="/><img src=x onerror=alert(5794)>
//            value = value.replaceAll(" src=", "");
//            value = value.replaceAll(" onerror=", "");
//            
//            if(value.startsWith("\"")) {
//            	 value = value.replaceFirst("\"", "");
//            }
//            if(value.startsWith("\"/>")) {
//           	 value = value.replaceFirst("\"/>", "");
//            }


//            Pattern scriptPattern = Pattern.compile("[\"|<](.*?)src(.*?)>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE	| Pattern.DOTALL);
//            Pattern scriptPattern = Pattern.compile("<(.*?)src(.*?)>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE	| Pattern.DOTALL);
//			value = scriptPattern.matcher(value).replaceAll("");

            // Remove all sections that match a pattern
            for (Pattern scriptPattern : patterns){
                value = scriptPattern.matcher(value).replaceAll("");
            }

            Pattern scriptPattern = Pattern.compile("(.*?)[<|</](.*?)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE	| Pattern.DOTALL);// <  </
            Pattern scriptPattern2 = Pattern.compile("(.*?)=[\"|'](.*?)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE	| Pattern.DOTALL);//  ="	='
            if(value.startsWith("\"") || scriptPattern.matcher(value).matches() || scriptPattern2.matcher(value).matches()
                    || ( value.contains("\"") && ( value.contains(" ") || value.contains("\n") || value.contains("\t") ) )		//包括引号与空格 引号与换行  引号与tab即%09
                //skp%22A%0Aonmouseover%0A%3D%60437%2B%7BvalueOf%3Aalert%7D%60
                //skp%22A%09onmouseover%3D450%2B%7BvalueOf%3Aalert%7D%3B
            ) {
                value = StringEscapeUtils.escapeHtml4(value);//除了空格符可以闭合当前属性外，
                //这些符号也可以：%     *     +     ,     –     /     ;     <     =     >     ^     |     `(反单引号，IE会认为它是单引号)
            }

        }
        return value;
    }

    private String stripXSSForJson(String value) {
        if (value != null) {
            // Avoid null characters
            value = value.replaceAll("\0", "");

            value = value.replaceAll("/\\*(.*?)\\*/", "");//	/* */避免这种攻击：{{url}}/toRegisterThree?id=" STYLE="xss:e/**/xpression(try{a=firstTime}catch(e){firstTime=1;alert(5730)});
//
            // Remove all sections that match a pattern
            for (Pattern scriptPattern : patterns){
                value = scriptPattern.matcher(value).replaceAll("");
            }
        }
        return value;
    }
    
	@Override
	public ServletInputStream getInputStream() throws IOException {
		String contentType = httpServletRequest.getContentType();
		if (StringUtils.isNotEmpty(contentType) && contentType.toLowerCase().contains("json")) {
			String encoding = "utf-8";
			ServletRequest requestWrapper = new BodyReaderHttpServletRequestWrapper(httpServletRequest, encoding);// 保证request.getInputStream()可以重复调用

			String requestBody = IOUtils.toString(requestWrapper.getInputStream(), encoding);
			requestBody = stripXSSForJson(requestBody);

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

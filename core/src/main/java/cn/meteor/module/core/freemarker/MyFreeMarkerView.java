package cn.meteor.module.core.freemarker;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerView;

import cn.meteor.module.core.freemarker.constants.HTMLConstants;
import cn.meteor.module.util.security.MD5Utils;
import freemarker.template.SimpleHash;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class MyFreeMarkerView extends FreeMarkerView {
	
	private static Logger logger = LoggerFactory.getLogger(MyFreeMarkerView.class);
	
	private static final String REQUEST = "request";
	
	private static final String ROOT_URL = "rootUrl";

	@Override
	protected void exposeHelpers(Map<String, Object> model,
			HttpServletRequest request) throws Exception {
		model.put(REQUEST, request);
		
//		<#if request.serverPort==80>
//		<#assign rootUrl=request.scheme+"://"+request.serverName+request.contextPath />
//		<#else>
//		<#assign rootUrl=request.scheme+"://"+request.serverName+":"+request.serverPort+request.contextPath />
//		</#if>
		
		
		String rootUrl="";
		if(Boolean.TRUE.equals(model.get(HTMLConstants.REQUEST_HEADER_NAME_IS_CREATE_HTML))) {
			rootUrl = model.get(HTMLConstants.REQUEST_HEADER_NAME_ROOT_URL).toString();
			model.put(ROOT_URL, rootUrl);//如果是静态化，则rootUrl使用来自请求头root_url的值
		} else {
			if(request.getServerPort()==80) {
				rootUrl = request.getScheme() + "://" + request.getServerName() + request.getContextPath();
//					rootUrl = "//" + request.getServerName() + request.getContextPath();
			} else {
				rootUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
//					rootUrl = "//" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
			}
			if(model.get(ROOT_URL) == null) {
				model.put(ROOT_URL, rootUrl);//如果是非静态化，rootUrl为空时，则初始化rootUrl的值
			}
		}
			
			
		
		super.exposeHelpers(model, request);
	}

	@Override
	protected void doRender(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
//		super.doRender(model, request, response);
		
		// Expose model to JSP tags (as request attributes).
		exposeModelAsRequestAttributes(model, request);
		// Expose all standard FreeMarker hash models.
		SimpleHash fmModel = buildTemplateModel(model, request, response);

		if (logger.isDebugEnabled()) {
			logger.debug("Rendering FreeMarker template [" + getUrl() + "] in FreeMarkerView '" + getBeanName() + "'");
		}
		// Grab the locale-specific version of the template.
		Locale locale = RequestContextUtils.getLocale(request);
//		processTemplate(getTemplate(locale), fmModel, response);
		
		
		if(Boolean.TRUE.equals(model.get(HTMLConstants.REQUEST_HEADER_NAME_IS_CREATE_HTML))) {
			String targetFilePath=model.get(HTMLConstants.REQUEST_HEADER_NAME_HTML_TARGET_FILE_PATH).toString();
        	String targetFilename=model.get(HTMLConstants.HTML_TARGET_FILE_NAME).toString();
        	String targetTempFilename=model.get(HTMLConstants.HTML_TARGET_TEMP_FILE_NAME).toString();
        	Result result = createHTML(getTemplate(locale), fmModel, request, response, targetFilePath, targetTempFilename);
        	if(result.isSuccess == true) {
        		File tempFile = new File(targetFilePath + "/" + targetTempFilename);
        		String tempFileContent = FileUtils.readFileToString(tempFile, "utf-8");
        		boolean hasError = tempFileContent.contains("<!--ERROR_TAG!!!-->");
        		if(hasError==true) {//如果有错误，则不做操作
        			return;
        		}
    			File destFile = new File(targetFilePath + "/" + targetFilename);
    			String tempFileMd5String = "";
    			String destFileMd5String = null;
    			if(tempFile.exists()) {
    				tempFileMd5String = MD5Utils.md5Digest(tempFile);
    			}
    			if(destFile.exists()) {
    				destFileMd5String = MD5Utils.md5Digest(destFile);
    			}    			
    			if( (StringUtils.isNotBlank(tempFileMd5String)&&!tempFileMd5String.equals(destFileMd5String))
    					|| !destFile.exists()) {//如果文件内容有变化 或者 目标文件不存在
    				FileUtils.copyFile(tempFile, destFile);
    			}
        	} else {
        		response.setStatus(500);
        		response.getWriter().println("error:   " );
        	}
            
			
        }else{
            processTemplate(getTemplate(locale), fmModel, response);
        }
	}
	
	
	public Result createHTML(Template template, SimpleHash model, HttpServletRequest request,  
            HttpServletResponse response, String targetFilePath, String targetFilename) throws IOException {
		Result result = new Result();
		File file = new File(targetFilePath);
        if(!file.exists()){
        	file.mkdirs();
        }
        FileOutputStream fileOutputStream=null;    
        Writer out =null;
        try {
        	fileOutputStream=new FileOutputStream(targetFilePath + "/" + targetFilename);
        	out = new OutputStreamWriter(fileOutputStream,"UTF-8");
			template.process(model, out);
			out.flush();
			result.setSuccess(true);
		} catch (TemplateException e) {
			logger.error("创建静态HTML，出现异常："+e.toString());
		} catch (IOException e) {
			logger.error("创建静态HTML，出现异常："+e.toString());
		} finally {
			IOUtils.closeQuietly(out);
			IOUtils.closeQuietly(fileOutputStream);
		}
        return result;
    }
	
	public class Result {
		private boolean isSuccess = false;
		
		private String errorMsg;

		public boolean isSuccess() {
			return isSuccess;
		}

		public void setSuccess(boolean isSuccess) {
			this.isSuccess = isSuccess;
		}

		public String getErrorMsg() {
			return errorMsg;
		}

		public void setErrorMsg(String errorMsg) {
			this.errorMsg = errorMsg;
		}
	}
}

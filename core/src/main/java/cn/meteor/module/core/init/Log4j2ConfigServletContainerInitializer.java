package cn.meteor.module.core.init;

import java.util.Set;

import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

/**
 * @author shenjc
 * 
 * http://www.eclipse.org/jetty/documentation/current/using-annotations.html#servlet-container-initializers
 * SpringServletContainerInitializer
 * Log4jServletContainerInitializer 
 *
 */
public class Log4j2ConfigServletContainerInitializer implements ServletContainerInitializer {

    @Override
    public void onStartup(Set<Class<?>> c, ServletContext ctx) throws ServletException {
    	String log4jConfiguration = System.getProperty("log4jConfiguration");
    	if(log4jConfiguration!=null && !"".equals(log4jConfiguration)) {//如果启动参数有值，则使用启动参数的值
            ctx.setInitParameter("log4jConfiguration", log4jConfiguration);
    	} else {
    		String log4jConfigurationDefault = ctx.getInitParameter("log4jConfigurationDefault");
    		if(log4jConfigurationDefault!=null && !"".equals(log4jConfigurationDefault)) {//如果web.xml有设定默认值，则使用默认值
        		ctx.setInitParameter("log4jConfiguration", log4jConfigurationDefault);
    		} else {//如果没传参，也没默认值，则
    			String log4jConfigurationProduction = "properties/production/log4j2.xml";
    			ctx.setInitParameter("log4jConfiguration", log4jConfigurationProduction);
    		}
    	}
    }
}

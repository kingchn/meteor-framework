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
 * https://logging.apache.org/log4j/2.x/manual/webapp.html
 */
//@HandlesTypes(WebApplicationInitializer.class)
public class Log4j2ConfigServletContainerInitializer implements ServletContainerInitializer {
	
//	private static final Logger LOGGER = StatusLogger.getLogger();
//
//    private ServletContext servletContext;
//    private Log4jWebLifeCycle initializer;

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
    	
    	
//        LOGGER.debug("Log4jServletContextListener ensuring that Log4j starts up properly.");
//
//        this.initializer = WebLoggerContextUtils.getWebLifeCycle(ctx);
//        try {
//            this.initializer.start();
//            this.initializer.setLoggerContext(); // the application is just now starting to start up
//        } catch (final IllegalStateException e) {
//            throw new IllegalStateException("Failed to initialize Log4j properly.", e);
//        }

    }
}

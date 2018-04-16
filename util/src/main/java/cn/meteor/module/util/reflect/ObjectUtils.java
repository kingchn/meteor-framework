package cn.meteor.module.util.reflect;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ObjectUtils {
	
	private static final Logger logger = LogManager.getLogger(ObjectUtils.class);
	
	/**
	 * 清空对象所有字段值，设置为null
	 */
	public static void setObjectFieldsEmpty(Object obj) {  
        // 对obj反射  
        Class objClass = obj.getClass();  
        Method[] objmethods = objClass.getDeclaredMethods();  
        Map objMeMap = new HashMap();  
        for (int i = 0; i < objmethods.length; i++) {  
            Method method = objmethods[i];  
            objMeMap.put(method.getName(), method);  
        }  
        for (int i = 0; i < objmethods.length; i++) {  
            {  
                String methodName = objmethods[i].getName();  
                if (methodName != null && methodName.startsWith("get")) {  
                    try {  
                        Object returnObj = objmethods[i].invoke(obj,  
                                new Object[0]);  
                        Method setmethod = (Method) objMeMap.get("set"  
                                + methodName.split("get")[1]);  
                        if (returnObj != null) {  
                            returnObj = null;  
                        }  
                        setmethod.invoke(obj, returnObj);  
                    } catch (IllegalArgumentException e) {
            			logger.error("ObjectUtils setObjectFieldsEmpty出错：", e);
                    } catch (IllegalAccessException e) {
            			logger.error("ObjectUtils setObjectFieldsEmpty出错：", e);
                    } catch (InvocationTargetException e) {
            			logger.error("ObjectUtils setObjectFieldsEmpty出错：", e);
                    }  
                }  
            }  
  
        }  
    }
}

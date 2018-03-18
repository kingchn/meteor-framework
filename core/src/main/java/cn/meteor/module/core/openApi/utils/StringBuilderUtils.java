package cn.meteor.module.core.openApi.utils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.MultiValueMap;

import cn.meteor.module.util.security.HMACUtils;
import cn.meteor.module.util.security.MD5Utils;

public class StringBuilderUtils {

	private final static Logger logger = LogManager.getLogger(StringBuilderUtils.class);
	
	public static String sign(List<String> paramNames, Map<String, String> paramValues, String secret, String signMethod) {
		if(!"md5".equals(signMethod)&&!"hmac".equals(signMethod)) {
			return null;
		}
		String signResultString="";
        StringBuilder sb = new StringBuilder();
        Collections.sort(paramNames);
        if("md5".equals(signMethod)) {
        	sb.append(secret);
            for (String paramName : paramNames) {
                sb.append(paramName).append(paramValues.get(paramName));
            }
            sb.append(secret);
            
    		try {
    			signResultString = MD5Utils.md5Digest(sb.toString());
    		} catch (Exception e) {
    			logger.error(e.getMessage());
    		}
        } else if("hmac".equals(signMethod)) {
        	 for (String paramName : paramNames) {
                 sb.append(paramName).append(paramValues.get(paramName));
             }
        	try {
				signResultString = HMACUtils.hmacDigest(sb.toString(), secret);
			} catch (UnsupportedEncodingException e) {
				logger.error(e.getMessage());
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
        }
        
        return signResultString.toUpperCase();
    }
	
	public static String sign(MultiValueMap<String, String> form, String secret, String signMethod) {
		return sign(new ArrayList<String>(form.keySet()), form.toSingleValueMap(), secret, signMethod);
	}
}

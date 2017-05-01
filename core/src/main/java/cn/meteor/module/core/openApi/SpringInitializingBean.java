package cn.meteor.module.core.openApi;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.context.support.WebApplicationObjectSupport;

import cn.meteor.module.core.openApi.response.ErrorMsgUtils;

public class SpringInitializingBean extends WebApplicationObjectSupport implements InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
//		ErrorMsgUtils.setMessageSourceAccessor(getMessageSourceAccessor());

	}

}

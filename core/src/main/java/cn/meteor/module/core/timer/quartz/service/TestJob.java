package cn.meteor.module.core.timer.quartz.service;

import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class TestJob implements Job {
	
	private static final Logger logger = LogManager.getLogger(TestJob.class);

	
	
	@Override
	@SuppressWarnings("unchecked")
	public void execute(JobExecutionContext context) throws JobExecutionException {
		//do something...
		
//		Date pushTime=null;
//		try {
//			MsgPushHistoryService msgPushHistoryService=(MsgPushHistoryService) SpringContextUtils.getBean("msgPushHistoryServiceImpl");
//			String pushApiKey=context.getJobDetail().getJobDataMap().getString("pushApiKey");
//			String pushApiSecret=context.getJobDetail().getJobDataMap().getString("pushApiSecret");
//			MsgPushHistory msgPushHistory=(MsgPushHistory) context.getJobDetail().getJobDataMap().get("msgPushHistory");
//			List<Device> deviceList=(List<Device>) context.getJobDetail().getJobDataMap().get("deviceList");
//			msgPushHistoryService.addMsgPushHistory(msgPushHistory,true,deviceList, pushApiKey, pushApiSecret);
//		} catch (Exception e) {
//			logger.error("定时["+pushTime+"]执行消息推送的时候，出现异常：{}",e);
//		}
	}

}
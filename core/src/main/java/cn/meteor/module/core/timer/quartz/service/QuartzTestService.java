package cn.meteor.module.core.timer.quartz.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.JobDataMap;
import org.quartz.SchedulerException;

public class QuartzTestService extends QuartzBaseService {

	private static final Logger logger = LogManager.getLogger(QuartzTestService.class);
	
	public boolean createTestJob(Map<?, ?> dataMap, Date executeDate) {
		JobDataMap newJobDataMap = new JobDataMap(dataMap);		
		SimpleDateFormat format = new SimpleDateFormat("ss mm HH dd MM ? yyyy");
		String cronExpression = format.format(executeDate);		   
		boolean createTestJobResult = createTestJob(newJobDataMap, cronExpression);
		return createTestJobResult;
	}

	public boolean createTestJob(JobDataMap newJobDataMap, String cronExpression) {
		Date now = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss.SSS");
		String dateString = format.format(now);
		String jobName = "testJob_" + dateString + "_" + RandomStringUtils.random(4, false, true);
		String jobDescription = "测试任务!";
		return createTestJob(jobName, jobDescription, newJobDataMap, cronExpression);
	}

	public boolean createTestJob(String jobName, String jobDescription, JobDataMap newJobDataMap, String cronExpression) {
		boolean createTestJobResult = true;
		try {
			createJob(TestJob.class, jobName, jobDescription, newJobDataMap, cronExpression);
		} catch (SchedulerException e) {
			logger.error(e.getMessage());
			createTestJobResult = false;
		}
		return createTestJobResult;
	}
	
}

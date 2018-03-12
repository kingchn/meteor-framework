package cn.meteor.module.core.timer.quartz.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerBuilder;
import org.springframework.beans.factory.annotation.Autowired;

import cn.meteor.module.core.timer.quartz.dao.QuartzDAO;
import cn.meteor.module.core.timer.quartz.domain.Quartz;

public class QuartzBaseService {
	
	private static final Logger logger = LogManager.getLogger(QuartzBaseService.class);

	
	private static Map<String, String> quartzStatus = new HashMap<String, String>(10);

	@Autowired
	private QuartzDAO quartzDAO;

	@Autowired
	private Scheduler quartzScheduler;

	public QuartzDAO getQuartzDAO() {
		return quartzDAO;
	}

	public void setQuartzDAO(QuartzDAO quartzDAO) {
		this.quartzDAO = quartzDAO;
	}

	public Scheduler getQuartzScheduler() {
		return quartzScheduler;
	}

	public void setQuartzScheduler(Scheduler quartzScheduler) {
		this.quartzScheduler = quartzScheduler;
	}

	/**
	 * 查询所有定时任务信息
	 * 
	 * @return
	 */
	public List<Quartz> getQuartzJobList() {
		return quartzDAO.selectAllQuartJob();
	}
	
	public void createJob(Class<? extends Job> jobClass, String jobName, String jobDescription, JobDataMap newJobDataMap, String cronExpression) throws SchedulerException {
		JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(jobName, Scheduler.DEFAULT_GROUP).withDescription(jobDescription).usingJobData(newJobDataMap).build();
		CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(jobName + "trigger", Scheduler.DEFAULT_GROUP).forJob(jobDetail).withSchedule(CronScheduleBuilder.cronSchedule(cronExpression))
				.build();
		quartzScheduler.scheduleJob(jobDetail, trigger);
	}

	/**
	 * 删除定时任务
	 * 
	 * @param jobName
	 * @throws SchedulerException
	 */
	public void deleteJob(String jobName) throws SchedulerException {
		quartzScheduler.deleteJob(new JobKey(jobName, Scheduler.DEFAULT_GROUP));
	}
	
	


}
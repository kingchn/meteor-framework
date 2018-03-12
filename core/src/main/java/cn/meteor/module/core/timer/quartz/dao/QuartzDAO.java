package cn.meteor.module.core.timer.quartz.dao;

import java.util.HashMap;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.meteor.module.core.timer.quartz.domain.Quartz;
import cn.meteor.module.core.timer.quartz.domain.QuartzMapper;

/**
 * 定时任务的DAO
 *
 */
@Repository
public class QuartzDAO {
	
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Autowired
	public void setDataSource(@Qualifier("quartzDataSource") DataSource quartzDataSource) {// 这里注明是配置文件里面的quartzDataSource数据源
		this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(quartzDataSource);
	}

	/**
	 * 查找所有的定时任务
	 * 
	 * @return
	 */
	public List<Quartz> selectAllQuartJob() {
		String sql = "select QRTZ_JOB_DETAILS.JOB_NAME,QRTZ_TRIGGERS.TRIGGER_NAME" + ",NEXT_FIRE_TIME,PREV_FIRE_TIME,TRIGGER_STATE,TRIGGER_TYPE,START_TIME,END_TIME"
				+ ",QRTZ_JOB_DETAILS.DESCRIPTION from QRTZ_TRIGGERS inner join QRTZ_JOB_DETAILS " + " on QRTZ_TRIGGERS.JOB_NAME = QRTZ_JOB_DETAILS.JOB_NAME order by start_time";
		return namedParameterJdbcTemplate.query(sql, new HashMap<String, String>(), new QuartzMapper());
	}
}

package cn.meteor.module.core.timer.quartz.domain;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class QuartzMapper implements RowMapper {

	@Override
	public Quartz mapRow(ResultSet rs, int rowNum) throws SQLException {
		Quartz quartz = new Quartz();
		quartz.setJobName(rs.getString("JOB_NAME"));
		quartz.setTriggerName(rs.getString("TRIGGER_NAME"));
		quartz.setNextFireTime(rs.getLong("NEXT_FIRE_TIME"));
		quartz.setPrevFireTime(rs.getLong("PREV_FIRE_TIME"));
		quartz.setTriggerState(rs.getString("TRIGGER_STATE"));
		quartz.setTriggerType(rs.getString("TRIGGER_TYPE"));
		quartz.setStartTime(rs.getLong("START_TIME"));
		quartz.setEndTime(rs.getLong("END_TIME"));
		quartz.setDescription(rs.getString("DESCRIPTION"));
		return quartz;
	}

}

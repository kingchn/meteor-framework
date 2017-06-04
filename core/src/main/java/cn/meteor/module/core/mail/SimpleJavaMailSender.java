package cn.meteor.module.core.mail;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.util.StringUtils;

/**
 * 纯文本邮件服务类.
 * 
 * @author shenjc
 */
public class SimpleJavaMailSender {
	
	private static final Log logger = LogFactory.getLog(SimpleJavaMailSender.class);

	private JavaMailSender mailSender;

	/**
	 * 发送邮件
	 * @param from
	 * @param to
	 * @param subject
	 * @param text
	 */
	public void sendMail(String from, String to, String subject, String text) {
		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setFrom(from);
		msg.setTo(to);
		msg.setSubject(subject);
		msg.setText(text);
		try {
			mailSender.send(msg);
			logger.info("纯文本邮件已发送至:"+StringUtils.arrayToCommaDelimitedString(msg.getTo()));
		} catch (Exception e) {
			logger.error("发送邮件失败", e);
		}
	}
	
	/**
	 * 群发邮件
	 * @param from
	 * @param to
	 * @param subject
	 * @param text
	 */
	public void sendMail(String from, String[] to, String subject, String text) {
		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setFrom(from);
		msg.setTo(to);
		msg.setSubject(subject);
		msg.setText(text);
		try {
			mailSender.send(msg);
			logger.debug("纯文本邮件已发送至:"+StringUtils.arrayToCommaDelimitedString(msg.getTo()));
		} catch (Exception e) {
			logger.error("发送邮件失败", e);
		}
	}

	/**
	 * Spring的MailSender.
	 */
	public void setMailSender(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}
}

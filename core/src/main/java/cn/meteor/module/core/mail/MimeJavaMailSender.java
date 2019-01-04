package cn.meteor.module.core.mail;

import java.io.File;
import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

/**
 * MIME邮件服务类.
 * 
 * 可发送普通HTML邮件、由Freemarker引擎生成的的html格式邮件、带有附件邮件
 * 
 * @author shenjc
 */
public class MimeJavaMailSender {

	private static final String DEFAULT_ENCODING = "utf-8";

	private static final Log logger = LogFactory.getLog(MimeJavaMailSender.class);

	private JavaMailSender mailSender;
//	private Template template;

	/**
	 * Spring的MailSender.
	 */
	public void setMailSender(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

	
	/**
	 * 发送邮件(指定编码)
	 * @param from 发件人邮箱
	 * @param fromName 发件人名称
	 * @param to 收件人
	 * @param subject 标题
	 * @param text 内容
	 * @param encoding 邮件编码
	 * @param html 是否html页面
	 * @throws MessagingException 
	 * @throws UnsupportedEncodingException 
	 */
	public void sendMail(String from, String fromName, String to, String subject, String text, String encoding, boolean html) throws MessagingException, UnsupportedEncodingException {
			if(encoding==null||"".equals(encoding)) {
				encoding = DEFAULT_ENCODING;
			}
			MimeMessage msg = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(msg, true, encoding);

			helper.setTo(to);
			helper.setFrom(from, fromName);
			helper.setSubject(subject);
			
			helper.setText(text, html);//为true-->发送转义HTML

			mailSender.send(msg);
			logger.debug("邮件已发送至:"+ to);
	}
	
	/**
	 * 发送邮件(指定编码)
	 * @param from 发件人邮箱
	 * @param fromName 发件人名称
	 * @param to 收件人
	 * @param subject 标题
	 * @param text 内容
	 * @param encoding 邮件编码
	 * @throws MessagingException 
	 * @throws UnsupportedEncodingException 
	 */
	public void sendMail(String from, String fromName, String to, String subject, String text, String encoding) throws MessagingException, UnsupportedEncodingException {
		sendMail(from, fromName, to, subject, text, encoding, false);
	}
	
	/**
	 * 发送邮件(默认utf8编码)
	 * @param from 发件人邮箱
	 * @param fromName 发件人名称
	 * @param to 收件人
	 * @param subject 标题
	 * @param text 内容
	 * @throws MessagingException 
	 * @throws UnsupportedEncodingException 
	 */
	public void sendMail(String from, String fromName, String to, String subject, String text) throws UnsupportedEncodingException, MessagingException {
		this.sendMail(from, fromName, to, subject, text, DEFAULT_ENCODING);
	}
	
	/**
	 * 群发邮件(指定编码)
	 * @param from 发件人邮箱
	 * @param fromName 发件人名称
	 * @param to 收件人(多人)
	 * @param subject 标题
	 * @param text 内容
	 * @param encoding 邮件编码
	 * @param html 是否html页面
	 * @throws MessagingException 
	 * @throws UnsupportedEncodingException 
	 */
	public void sendMail(String from, String fromName, String[] to, String subject, String text, String encoding, boolean html) throws MessagingException, UnsupportedEncodingException {
		if(encoding==null||"".equals(encoding)) {
			encoding = DEFAULT_ENCODING;
		}
		MimeMessage msg = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(msg, true, encoding);

		helper.setTo(to);
		helper.setFrom(from, fromName);
		helper.setSubject(subject);
		
		helper.setText(text, html);//为true-->发送转义HTML

		mailSender.send(msg);
		logger.debug("邮件已发送至:"+ to);
	}
	
	/**
	 * 群发邮件(指定编码)
	 * @param from 发件人邮箱
	 * @param fromName 发件人名称
	 * @param to 收件人(多人)
	 * @param subject 标题
	 * @param text 内容
	 * @param encoding 邮件编码
	 * @throws MessagingException 
	 * @throws UnsupportedEncodingException 
	 */
	public void sendMail(String from, String fromName, String[] to, String subject, String text, String encoding) throws MessagingException, UnsupportedEncodingException {
		sendMail(from, fromName, to, subject, text, encoding, false);
	}
	
	/**
	 * 发送邮件(默认utf8编码)
	 * @param from 发件人邮箱
	 * @param fromName 发件人名称
	 * @param to 收件人
	 * @param subject 标题
	 * @param text 内容
	 * @throws MessagingException 
	 * @throws UnsupportedEncodingException 
	 */
	public void sendMail(String from, String fromName, String[] to, String subject, String text) throws UnsupportedEncodingException, MessagingException {
		this.sendMail(from, fromName, to, subject, text, DEFAULT_ENCODING);
	}
	
	/**
	 * 发送带附件邮件(指定编码)
	 * @param from 发件人邮箱
	 * @param fromName 发件人名称
	 * @param to 收件人
	 * @param subject 标题
	 * @param text 内容
	 * @param encoding 邮件编码
	 * @param attachment 附件
	 * @param html 是否html页面
	 * @return 发送结果 1:成功  2:构造邮件失败MessagingException 3:收件地址无效 4:其他错误
	 * @throws MessagingException 
	 * @throws UnsupportedEncodingException 
	 */
	public void sendMail(String from, String fromName, String to, String subject, String text, String encoding, File attachment, boolean html) throws MessagingException, UnsupportedEncodingException {
		if(encoding==null||"".equals(encoding)) {
			encoding = DEFAULT_ENCODING;
		}
		MimeMessage msg = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(msg, true, encoding);

		helper.setTo(to);
		helper.setFrom(from, fromName);
		helper.setSubject(subject);
		
		helper.setText(text, html);//为true-->发送转义HTML
		
//		String attachName = MimeUtility.encodeText(attachment.getName());
//			helper.addInline(attachName, attachment);
		
		helper.addAttachment(attachment.getName(), attachment);

		mailSender.send(msg);
		logger.debug("邮件已发送至:"+ to);
	}
	
	/**
	 * 发送带附件邮件(指定编码)
	 * @param from 发件人邮箱
	 * @param fromName 发件人名称
	 * @param to 收件人
	 * @param subject 标题
	 * @param text 内容
	 * @param encoding 邮件编码
	 * @param attachment 附件
	 * @return 发送结果 1:成功  2:构造邮件失败MessagingException 3:收件地址无效 4:其他错误
	 * @throws MessagingException 
	 * @throws UnsupportedEncodingException 
	 */
	public void sendMail(String from, String fromName, String to, String subject, String text, String encoding, File attachment) throws MessagingException, UnsupportedEncodingException {
		sendMail(from, fromName, to, subject, text, encoding, attachment, false);
	}
	
	/**
	 * 发送带附件邮件
	 * @param from 发件人邮箱
	 * @param fromName 发件人名称
	 * @param to 收件人
	 * @param subject 标题
	 * @param text 内容
	 * @param encoding 邮件编码
	 * @param inputStreamSource 附件输入流
	 * @param attachmentName 附件名称
	 * @param html 是否html页面
	 * @return 发送结果 1:成功  2:构造邮件失败MessagingException 3:收件地址无效 4:其他错误
	 * @throws MessagingException 
	 * @throws UnsupportedEncodingException 
	 */
	public void sendMail(String from, String fromName, String to, String subject, String text, String encoding, InputStreamSource inputStreamSource, String attachmentName, boolean html) throws MessagingException, UnsupportedEncodingException {
		if(encoding==null||"".equals(encoding)) {
			encoding = DEFAULT_ENCODING;
		}
		MimeMessage msg = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(msg, true, encoding);

		helper.setTo(to);
		helper.setFrom(from, fromName);
		helper.setSubject(subject);
		
		helper.setText(text, html);//为true-->发送转义HTML
		
//		String attachName = MimeUtility.encodeText(attachmentName);
//			helper.addInline(attachName, attachment);
		
		helper.addAttachment(attachmentName, inputStreamSource);

		mailSender.send(msg);
		logger.debug("邮件已发送至:"+ to);
	}
	
	/**
	 * 发送带附件邮件
	 * @param from 发件人邮箱
	 * @param fromName 发件人名称
	 * @param to 收件人
	 * @param subject 标题
	 * @param text 内容
	 * @param encoding 邮件编码
	 * @param attachmentName 附件名称
	 * @param inputStreamSource 附件输入流
	 * @return 发送结果 1:成功  2:构造邮件失败MessagingException 3:收件地址无效 4:其他错误
	 * @throws MessagingException 
	 * @throws UnsupportedEncodingException 
	 */
	public void sendMail(String from, String fromName, String to, String subject, String text, String encoding, InputStreamSource inputStreamSource, String attachmentName) throws MessagingException, UnsupportedEncodingException {
		sendMail(from, fromName, to, subject, text, encoding, inputStreamSource, attachmentName, false);
	}
	
//	/**
//	 * 发送邮件(模板形式)
//	 * @param templatePath 模板路径
//	 * @param map 数据map
//	 * @param from 发件人邮箱
//	 * @param fromName 发件人名称
//	 * @param to 收件人
//	 * @param subject 标题
//	 * @param encoding 邮件编码
//	 * @return 发送结果 1:成功  2:构造邮件失败MessagingException 3:收件地址无效 4:其他错误
//	 */
//	public String sendMail(String templatePath, Map map, String from, String fromName, String to, String subject, String  encoding) {
//		String result="0";
//		try {
//			if(encoding==null||"".equals(encoding)) {
//				encoding = DEFAULT_ENCODING;
//			}
//			MimeMessage msg = mailSender.createMimeMessage();
//			MimeMessageHelper helper = new MimeMessageHelper(msg, true, encoding);
//
//			helper.setTo(to);
//			helper.setFrom(from, fromName);
//			helper.setSubject(subject);
//			
//			String text = FreemarkerUtils.getContent(templatePath, map);
//			helper.setText(text, true);
//
//			mailSender.send(msg);
//			logger.info("HTML版邮件已发送至:"+ to);
//			result = "1";
//		} catch (MessagingException e) {
//			logger.error("构造邮件失败", e);
//			result = "2";
//		} catch (Exception e) {
//			logger.error("发送邮件失败", e);
//			if(e.getMessage().indexOf("Invalid Addresses")!=-1) {
//				result = "3";
//			} else {
//				result = "4";
//			}
//		}
//		
//		return result;
//	}
	
	
}

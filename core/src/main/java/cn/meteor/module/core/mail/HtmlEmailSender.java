package cn.meteor.module.core.mail;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

/**
 * 发送邮件工具类
 * @author shenjc
 * @see  htmlEmail.properties:<br/>
	email.smtp.host=ip地址
	email.smtp.port=端口号，默认25	
	email.smtp.isAuth=true
	email.smtp.userName=发送人名称
	email.smtp.password=密码	
	
	email.default.fromEmail=发送人邮箱地址
	email.default.fromName=发送人别名
	email.default.toEmail=默认收件地址
	email.default.toName=默认收件人别名

 */
public class HtmlEmailSender {
	
	private String host;
	private int port;
	private Boolean isAuth;
	private String userName;
	private String password;
	
	private Boolean isSSL;
	
	private String defaultFromEmail;
	private String defaultFromName;
	
	public HtmlEmailSender() throws ConfigurationException {
		Configuration config = new PropertiesConfiguration("email.properties");
//		Configuration config = new PropertiesConfiguration("gmail-email.properties");
		host = config.getString("mail.host");
		port = config.getInt("mail.smtp.port");
		
		isAuth = config.getBoolean("mail.smtp.auth");
		userName = config.getString("mail.username");
		password = config.getString("mail.password");
		
		isSSL = config.getBoolean("mail.smtp.ssl.enable");
		
		defaultFromEmail = config.getString("mail.default.fromEmail");
		defaultFromName = config.getString("mail.default.fromName");
	}
	
	public HtmlEmailSender(String configName) throws ConfigurationException {
		Configuration config = new PropertiesConfiguration(configName);
		host = config.getString("mail.host");
		port = config.getInt("mail.smtp.port");
		
		isAuth = config.getBoolean("mail.smtp.auth");
		userName = config.getString("mail.username");
		password = config.getString("mail.password");
		
		isSSL = config.getBoolean("mail.smtp.starttls.enable");
		
		defaultFromEmail = config.getString("mail.default.fromEmail");
		defaultFromName = config.getString("mail.default.fromName");
	}
	
	public HtmlEmailSender(String host, int port, Boolean isAuth, String userName, String password, Boolean isSSL, String defaultFromEmail, String defaultFromName) {
		this.host = host;
		this.port = port;
		this.isAuth = isAuth;
		this.userName = userName;
		this.password = password;
		
		this.isSSL = isSSL;
		
		this.defaultFromEmail = defaultFromEmail;
		this.defaultFromName = defaultFromName;
	}
	
	/**
	 * 发送邮件<br/>
	 * Sends the email. Internally we build a MimeMessage which is afterwards sent to the SMTP server.
	 * @param fromEmail 发信的邮件帐号地址
	 * @param fromName 发信人
	 * @param toEmail 收件人帐号地址
	 * @param toName 收件人
	 * @param subject 邮件主题
	 * @param htmlMsg 邮件正文
	 * @param charset 字符编码
	 * @return the message id of the underlying MimeMessage
	 * @throws EmailException
	 */
	public String sendHtmlEmail(String fromEmail, String fromName, String toEmail, String toName, String subject, String htmlMsg, String charset) throws EmailException {
		HtmlEmail email = new HtmlEmail();
		email.setHostName(host);		// 设置发信的smtp服务器
		email.setSmtpPort(port);
		if(isAuth==true) {					// 如果smtp服务器需要认证的话，在这里设置帐号、密码
			email.setAuthentication(userName, password);
		}
		
		email.setFrom(fromEmail, fromName);		// 设置发信的邮件帐号和发信人
		email.addTo(toEmail, toName);		// 设置收件人帐号和收件人		
		email.setSubject(subject);				// 设置邮件主题
		
		email.setCharset(charset);				// 设置邮件字符编码
		email.setHtmlMsg(htmlMsg);		// 设置邮件正文和字符编码	
		
		if(isSSL == true) {
			email.setSSL(true);
		}
		
		return email.send();
	}
	
	/**
	 * 发送邮件到配置文件中设置的默认收件人<br/>
	 * Sends the email. Internally we build a MimeMessage which is afterwards sent to the SMTP server.
	 * @param toEmail 收件人帐号地址
	 * @param toName 收件人
	 * @param subject 邮件主题
	 * @param htmlMsg 邮件正文
	 * @param charset 字符编码
	 * @return the message id of the underlying MimeMessage
	 * @throws EmailException
	 */
	public String sendHtmlEmailFromDefaultUser(String toEmail, String toName, String subject, String htmlMsg, String charset) throws EmailException {
		return sendHtmlEmail(defaultFromEmail, defaultFromName, toEmail, toName, subject, htmlMsg, charset);
	}
}

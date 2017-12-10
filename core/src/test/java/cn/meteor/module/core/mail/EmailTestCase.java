package cn.meteor.module.core.mail;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.mail.EmailException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = { "classpath*:/applicationContext.xml",
//			"classpath*:/applicationContext-ws-client.xml" })
@ContextConfiguration(locations = { "classpath*:/applicationContext.xml",
		"classpath*:/applicationContext*.xml",
		"classpath*:conf/**/meteor*applicationContext*.xml" }) 
//@ContextConfiguration(locations = { "classpath*:/applicationContext.xml" }) 
public class EmailTestCase {
	
//	private final static Logger logger = LoggerFactory.getLogger(EmailTestCase.class);
	private  static final Logger logger = LogManager.getLogger(EmailTestCase.class);
	
	@Autowired
	private HtmlEmailSender meteorMail;

	@Test
	public void sendHtmlEmailFromDefaultUserTest() {
		try {
			HtmlEmailSender email=new HtmlEmailSender();
			String toEmail="shenjc@qq.com";
			String toName="shenjc";
			String subject="主题哦";
			String htmlMsg="消息内容";
//			String htmlMsg="<html><body><span>尊敬的用户：</span><br><span>&nbsp;&nbsp;&nbsp;&nbsp;您收到了xxxxx。请下载附件查看。</span><br><br><span>&nbsp;&nbsp;&nbsp;xxxxx集团</span><br><br></body></html>";
			String charset="utf-8";
			email.sendHtmlEmailFromDefaultUser(toEmail, toName, subject, htmlMsg, charset);
		} catch (ConfigurationException e) {
			logger.error(e.getMessage());
		} catch (EmailException e) {
			logger.error(e.getMessage());
		}
	}
	
}

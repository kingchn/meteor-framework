package cn.meteor.module.core.mail;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = { "classpath*:/applicationContext.xml",
//"classpath*:/applicationContext-ws-client.xml" })
@ContextConfiguration(locations = { "classpath*:/applicationContext.xml",
"classpath*:/applicationContext*.xml",
"classpath*:conf/**/*spring*.xml" })
//@ContextConfiguration(locations = { "classpath*:/applicationContext.xml" }) 
public class SimpleMailServiceTestCase {

	@Autowired
	private SimpleJavaMailSender simpleMailService;
	
	@Test
	public void sendMailTest() {
		String from="shenjc.test@gmail.com";
		String to="shenjc@qq.com";
		String subject="主题SimpleMailServiceTest";
		String text="内容SimpleMailServiceTest";
		simpleMailService.sendMail(from, to, subject, text);
	}
}

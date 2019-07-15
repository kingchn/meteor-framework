package cn.meteor.module.core.mail;

import java.io.File;
import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;

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
		"classpath*:conf/**/*spring*.xml" })
//@ContextConfiguration(locations = { "classpath*:/applicationContext.xml" }) 
public class MimeMailServiceTestCase {

	@Autowired
	private MimeJavaMailSender mimeJavaMailSender;
	
	
	@Test
	public void sendMailWithFromNameTest() {
//		String from="shenjc.test@gmail.com";
		String from="dzfpyun@foresee.com.cn";
		String fromName="发送人哦333";
		String subject="主题MimeMailService";
//		String text="内容MimeMailService";
		String text="<div>尊敬的用户：您收到了一张电子发票。请下载附件查看。</div></br>"
				+ "<div>使用支付宝扫描以下二维码推送发票到支付宝发票管家:</div>"
				+ "<img src='https://www.dzfpyun.com/siteui/images/index/jdal03.png' type='image/png' />";
		String to="shenjc@qq.com";
		try {
//			mimeJavaMailSender.sendMail(from, fromName, to, subject, text);
			File file = new File("D:/pdf_test/044001800111_83479496.pdf");
			mimeJavaMailSender.sendMail(from, fromName, to, subject, text, "UTF-8", file, true);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
//	@Test
//	public void sendMailWithTemplateTest() {
//		String from="shenjc.test@gmail.com";
//		String to="shenjc@qq.com";
//		String subject="主题MimeMailService-sendMailWithTemplateTest";
//		String text="内容MimeMailService-sendMailWithTemplateTest";
//		
//		String templatePath="/freemarker_template/sendMailTest.ftl";
//		Map map=new HashMap();
//		map.put("a", "AAA");
//		String fromName="发送人哦";
//		String encoding="utf-8";
//		mimeMailService.sendMail(templatePath, map, from, fromName, to, subject, encoding);
//	}
}

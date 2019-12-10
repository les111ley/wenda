package com.wenda.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;


import javax.mail.internet.*;
import java.util.Map;
import java.util.Properties;

/**
 * Create by xrh
 * 4:43 PM on 12/10/19 2019
 */
@Service
public class MailSender implements InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(MailSender.class);
    private  JavaMailSenderImpl mailSender;

    /*velocity 过期不可用
    @Autowired
    private VelocityEngine velocityEngine;*/


    //发送邮件的相关设置
    @Override
    public void afterPropertiesSet() throws Exception {
        mailSender = new JavaMailSenderImpl();
        mailSender.setUsername("xrh@qq.com");  //假设发送邮箱为xrh@qq.com
        mailSender.setPassword("123456");
        mailSender.setHost("smtp.exmail.qq.com");
        //mailSender.setHost("smtp.qq.com");
        mailSender.setPort(465);
        mailSender.setProtocol("smtps");
        mailSender.setDefaultEncoding("utf8");
        Properties javaMailProperties = new Properties();
        javaMailProperties.put("mail.smtp.ssl.enable", true);
        //javaMailProperties.put("mail.smtp.auth", true);
        //javaMailProperties.put("mail.smtp.starttls.enable", true);
        mailSender.setJavaMailProperties(javaMailProperties);
    }

    public boolean sendWithHTMLTemplate(String to, String subject,
                                        String template, Map<String, Object> model) {
        try {
            String nick = MimeUtility.encodeText("wenda");
            InternetAddress from = new InternetAddress(nick + "<xrh@qq.com>");
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            //Velocity 引擎过期不可用，无法用template渲染model
//            String result = VelocityEngineUtils
//                    .mergeTemplateIntoString(velocityEngine, template, "UTF-8", model);
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setFrom(from);
            mimeMessageHelper.setSubject(subject);
            //本应传入result，但由于velocity不可用所以先传入template使程序编译不报错
            mimeMessageHelper.setText(template, true);
            mailSender.send(mimeMessage);
            return true;
        } catch (Exception e) {
            logger.error("发送邮件失败" + e.getMessage());
            return false;
        }
    }
}

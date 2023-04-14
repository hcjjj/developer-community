package com.coder.community.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

//通用的bean,在哪里层次都可以用
@Component
public class MailClient {

//    用于记录日志
    private static final Logger logger = LoggerFactory.getLogger(MailClient.class);

    // 注入bean
    @Autowired
    private JavaMailSender mailSender;

    // 发件人 直接引入配置文件里面写的
    @Value("${spring.mail.username}")
    private String from;

    /**
     *
     * @param to 接收者
     * @param subject 主题
     * @param content 内容
     */
    public void sendMail(String to, String subject, String content) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
//            支持html文本
            helper.setText(content, true);
            mailSender.send(helper.getMimeMessage());
        } catch (MessagingException e) {
//            日志记录
            logger.error("发送邮件失败:" + e.getMessage());
        }
    }

}
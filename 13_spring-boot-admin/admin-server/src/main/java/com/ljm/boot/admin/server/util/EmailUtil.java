package com.ljm.boot.admin.server.util;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.mail.internet.MimeMessage;


@Component
public class EmailUtil {

    private final Logger logger= LoggerFactory.getLogger(EmailUtil.class);

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String form;
    @Value("${client.notify.mail.to}")
    private String to;

    public  void sendEmail(String subject,String message){
        try {
            if(StringUtils.isEmpty(to)){
                logger.error("email to is Emtry ... send Email stop");
                return;
            }
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            //发件人
            helper.setFrom(form);
            //收件人
            String []toArray=to.split(",");
            helper.setTo(toArray);
            //标题
            helper.setSubject(subject);
            //文本
            helper.setText(message,true);
            mailSender.send(mimeMessage);
        }catch (Exception e){
            logger.info("sendEmail error,message={}",e);
        }
    }

}

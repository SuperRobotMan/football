package com.jiang.football.dao.sendEmail;

import lombok.extern.slf4j.Slf4j;

import com.sun.mail.util.MailSSLSocketFactory;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.security.GeneralSecurityException;
import java.util.Properties;

@Slf4j
public class SendEmailImpl {

    public static void sendEmail(String to, String cc, String bcc, String subject, String text, String file){

        // 发件人电子邮箱
        String from = "34650965@qq.com";

        // 指定发送邮件的主机为 smtp.qq.com
        String host = "smtp.qq.com";  //QQ 邮件服务器

        // 获取系统属性
        Properties properties = System.getProperties();

        // 设置邮件服务器
        properties.setProperty("mail.smtp.host", host);

        properties.put("mail.smtp.auth", "true");

        MailSSLSocketFactory sf = null;
        try {
            sf = new MailSSLSocketFactory();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }

        sf.setTrustAllHosts(true);

        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.ssl.socketFactory", sf);

        // 获取默认session对象
        Session session = Session.getDefaultInstance(properties, new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {     //qq邮箱服务器账户、第三方登录授权码
                return new PasswordAuthentication("34650965@qq.com", "suuujdvqjgcobhgj"); //发件人邮件用户名、密码
            }
        });

        try {
            // 创建默认的 MimeMessage 对象
            MimeMessage message = new MimeMessage(session);
            // Set From: 头部头字段
            message.setFrom(new InternetAddress(from));
            // Set To: 头部头字段
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            if (cc != null && cc.length() != 0) {
                message.setRecipients(javax.mail.Message.RecipientType.CC, InternetAddress.parse(cc));
            }
            if (bcc != null && bcc.length() != 0) {
                message.setRecipients(javax.mail.Message.RecipientType.BCC, InternetAddress.parse(bcc));
            }
            // Set Subject: 主题文字
            message.setSubject(subject);
            // 创建消息部分
            BodyPart messageBodyPart = new MimeBodyPart();
            // 消息
            messageBodyPart.setText(text);
            // 创建多重消息
            Multipart multipart = new MimeMultipart();
            // 设置文本消息部分
            multipart.addBodyPart(messageBodyPart);

            // 附件部分
            messageBodyPart = new MimeBodyPart();
            //设置要发送附件的文件路径
            DataSource source = new FileDataSource(file);

            messageBodyPart.setDataHandler(new DataHandler(source));
            //messageBodyPart.setFileName(filename);
            //处理附件名称中文（附带文件路径）乱码问题
            messageBodyPart.setFileName(MimeUtility.encodeText("我是附件的name.xlsx"));

            multipart.addBodyPart(messageBodyPart);
            // 发送完整消息
            message.setContent(multipart);
            //   发送消息
            Transport.send(message);
            System.out.println("Sent message successfully....");
        } catch (Exception mex) {
            mex.printStackTrace();
        }
    }
}

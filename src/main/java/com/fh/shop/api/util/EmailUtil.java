package com.fh.shop.api.util;

import com.fh.shop.api.common.SystemConst;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailUtil {

    public static void buildEmail(String email,String title,String content)  {

        Properties prop = new Properties();
        prop.setProperty("mail.host",SystemConst.FROM_USER);
        prop.setProperty("mail.transport.protocol", "smtp");
        prop.setProperty("mail.smtp.auth", "true");

        //1、创建session
        Session session = Session.getInstance(prop);
        Transport ts = null;
        try {
            //2、通过session得到transport对象
             ts = session.getTransport();
            //3、使用邮箱的用户名和密码连上邮件服务器，发送邮件时，发件人需要提交邮箱的用户名和密码给smtp服务器，用户名和密码都通过验证之后才能够正常发送邮件给收件人。
            ts.connect(SystemConst.MAIL_HOST, SystemConst.FROM_USER, SystemConst.EMAIL_PASSWORD);
            //创建邮件对象
            MimeMessage message = new MimeMessage(session);
            //指明邮件的发件人
            message.setFrom(new InternetAddress(SystemConst.FROM_USER));
            //指明邮件的收件人，现在发件人和收件人是一样的，那就是自己给自己发
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(email));
            //邮件的标题
            message.setSubject(title);
            //邮件的文本内容
            message.setContent(content, "text/html;charset=UTF-8");
            //5、发送邮件
            ts.sendMessage(message, message.getAllRecipients());
            ts.close();
        } catch (MessagingException e) {
            e.printStackTrace();
        } finally {
            if (ts != null){
                try {
                    ts.close();
                    ts=null;
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}

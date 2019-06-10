package cn.lilq;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.*;
import java.util.Date;
import java.util.Properties;

public class JMailUtil {
    //发件人地址
    private static String senderAddress = "llqzuishuai@163.com";
    //发件人账户名
    private static String senderAccount = "llqzuishuai";
    //发件人账户密码
    private static String senderPassword = "daquan123456";


    public static void sendMessage(String[] recipientAddress,String subject,String content) throws Exception {
        Properties props = new Properties();
        props.setProperty("mail.smtp.auth", "true");
        props.setProperty("mail.transport.protocol", "smtp");
        props.setProperty("mail.smtp.host", "smtp.163.com");
        Session session = Session.getInstance(props);
        //设置调试信息在控制台打印出来
        session.setDebug(true);
        Message msg = getMimeMessage(session,recipientAddress,subject,content);
        Transport transport = session.getTransport();
        transport.connect(senderAccount, senderPassword);
        transport.sendMessage(msg,msg.getAllRecipients());
        transport.close();
    }
    public static MimeMessage getMimeMessage(Session session,String[] recipientAddress,String subject,String content) throws Exception{
        //创建一封邮件的实例对象
        MimeMessage msg = new MimeMessage(session);
        //设置发件人地址
        msg.setFrom(new InternetAddress(senderAddress));
        /*
         * 3.设置收件人地址（可以增加多个收件人、抄送、密送），即下面这一行代码书写多行
         * MimeMessage.RecipientType.TO:发送
         * MimeMessage.RecipientType.CC：抄送
         * MimeMessage.RecipientType.BCC：密送
         */
        for (int i = 0; i < recipientAddress.length; i++) {
            msg.setRecipient(MimeMessage.RecipientType.TO,new InternetAddress(recipientAddress[i]));
        }
        msg.setSubject(subject,"UTF-8");
        msg.setContent(content, "text/html;charset=UTF-8");
        msg.setSentDate(new Date());
        return msg;
    }
}

package cn.lilq.test;

import javax.mail.*;
import javax.mail.internet.MimeUtility;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Properties;

public class RecipientMail {
    //收件人地址
    public static String recipientAddress = "xxx@163.com";
    //收件人账户名
    public static String recipientAccount = "llqzuishuai";
    //收件人账户密码
    public static String recipientPassword = "daquan123456";

    public static void main(String[] args) throws Exception {
        //1、连接邮件服务器的参数配置
        Properties props = new Properties();
        //设置传输协议
        props.setProperty("mail.store.protocol", "pop3");
        //设置收件人的POP3服务器
        props.setProperty("mail.pop3.host", "pop3.163.com");
        //2、创建定义整个应用程序所需的环境信息的 Session 对象
        Session session = Session.getInstance(props);
        //设置调试信息在控制台打印出来
//        session.setDebug(true);

        Store store = session.getStore("pop3");
        //连接收件人POP3服务器
        store.connect("pop3.163.com", recipientAccount, recipientPassword);
        //获得用户的邮件账户，注意通过pop3协议获取某个邮件夹的名称只能为inbox
        Folder folder = store.getFolder("inbox");
        //设置对邮件账户的访问权限
        folder.open(Folder.READ_WRITE);

        //得到邮件账户的所有邮件信息
        Message[] messages = folder.getMessages();
        for(int i = 0 ; i < messages.length ; i++){
            //获得邮件主题
            String subject = messages[i].getSubject();
            System.out.println("主题;"+subject);
            //获得邮件发件人
            Address[] from = messages[i].getFrom();
            System.out.println("发件人;"+from);
            //解析综合数据情况
            getAllMultipart(messages[i]);
            //获取邮件内容（包含邮件内容的html代码）
//            String content = (String) messages[i].getContent();
        }

        //关闭邮件夹对象
        folder.close();
        //关闭连接对象
        store.close();
    }
    /**
     * 解析综合数据
     * @param part
     * @throws Exception
     */
    private static void getAllMultipart(Part part) throws Exception{
        String contentType = part.getContentType();
        int index = contentType.indexOf("name");
        boolean conName = false;
        if(index!=-1){
            conName=true;
        }
        //判断part类型
        if(part.isMimeType("text/plain") && ! conName) {
            System.out.println((String) part.getContent());
        }else if (part.isMimeType("text/html") && ! conName) {
            System.out.println((String) part.getContent());
        }else if (part.isMimeType("multipart/*")) {
            Multipart multipart = (Multipart) part.getContent();
            int counts = multipart.getCount();
            for (int i = 0; i < counts; i++) {
                //递归获取数据
                getAllMultipart(multipart.getBodyPart(i));
                //附件可能是截图或上传的(图片或其他数据)
                if (multipart.getBodyPart(i).getDisposition() != null) {
                    //附件为截图
                    if (multipart.getBodyPart(i).isMimeType("image/*")) {
                        //文件中文解码
                        InputStream is = multipart.getBodyPart(i).getInputStream();
                        String name = multipart.getBodyPart(i).getFileName();
                        name = MimeUtility.decodeText(name);
                        System.out.println("图片文件名称"+name);
                        String fileName;
                        //截图图片
                        if(name.startsWith("=?")){
                            fileName = name.substring(name.lastIndexOf(".") - 1,name.lastIndexOf("?="));
                        }else{
                            //上传图片
                            fileName = name;
                        }

                        FileOutputStream fos = new FileOutputStream("/Users/daquan/Desktop/hello1/"
                                + fileName);
                        int len = 0;
                        byte[] bys = new byte[1024];
                        while ((len = is.read(bys)) != -1) {
                            fos.write(bys,0,len);
                        }
                        fos.close();
                    } else {
                        //其他附件
                        InputStream is = multipart.getBodyPart(i).getInputStream();
                        String name = multipart.getBodyPart(i).getFileName();
                        name = MimeUtility.decodeText(name);
                        System.out.println("文件名称"+name);
                        FileOutputStream fos = new FileOutputStream("/Users/daquan/Desktop/hello1/"
                                + name);
                        int len = 0;
                        byte[] bys = new byte[1024];
                        while ((len = is.read(bys)) != -1) {
                            fos.write(bys,0,len);
                        }
                        fos.close();
                    }
                }
            }
        }else if (part.isMimeType("message/rfc822")) {
            getAllMultipart((Part) part.getContent());
        }
    }
}

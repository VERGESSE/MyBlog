package top.vergessen.blog.util;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

import java.util.concurrent.*;

/**
 * @author Vergessen
 * @date 2020/7/15 13:12.
 */
@Component
public class MailTemplate {

    @Autowired
    private JavaMailSenderImpl mailSender;

    @Value("${spring.mail.username}")
    private String username;

    /**
     * 邮件发送线程池
     */
    private final ExecutorService mailExecutor = new ThreadPoolExecutor(
            3, 3, 0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(50),
            new ThreadFactoryBuilder().setNameFormat("Mail-%d").build(),
            new ThreadPoolExecutor.CallerRunsPolicy());

    /**
     * 是否打开邮件服务
     */
    @Value("${spring.mail.isopen}")
    private Boolean isopen;

    /**
     * 发送邮件
     * @param title 邮件标题
     * @param content 邮件内容
     * @param to 收件人
     */
    public void sendTxtMail(String title, String content, String to) {
        // 如果没有打开邮件服务则不作任何事情
        if (isopen) {
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            // 设置收件人，寄件人
            simpleMailMessage.setTo(to);
            simpleMailMessage.setFrom(username);
            simpleMailMessage.setSubject(title);
            simpleMailMessage.setText(content);
            // 发送邮件，异步发送，不阻塞主线程
            mailExecutor.execute(() -> {
                mailSender.send(simpleMailMessage);
            });
        }
    }

    public String getRootMail(){
        return this.username;
    }
}

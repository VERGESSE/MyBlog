package top.vergessen.blog.controller;

import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import top.vergessen.blog.auth.CheckLogin;
import top.vergessen.blog.domain.Message;
import top.vergessen.blog.exception.BlogException;
import top.vergessen.blog.exception.ExceptionEnum;
import top.vergessen.blog.service.MessageService;
import top.vergessen.blog.util.MailTemplate;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Vergessen
 * @date 2020/7/6 23:01.
 */
@RestController
@RequestMapping("message")
@CrossOrigin
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MessageController {

    private final MessageService messageService;
    private final MailTemplate mailTemplate;

    /**
     * 获取留言总数
     */
    @GetMapping("num")
    public ResponseEntity<Integer> getMessageNum(){
        return ResponseEntity.ok(messageService.selectMessageCount());
    }

    /**
     * 前台添加留言信息
     * @param message 留言信息
     * @param request 用于获取留言者IP
     */
    @PostMapping("msg")
    public ResponseEntity<Void> addMessage(
            @RequestBody Message message,
            HttpServletRequest request){
        // 获取留言者IP
        String ip = request.getHeader("X-Real-IP");
        if (StringUtils.isBlank(ip) || "0.0.0.0".equals(ip)) {
            ip = request.getRemoteAddr();
        }
        message.setIp(ip);
        messageService.addMessage(message);
        // 给自己邮件提醒
        mailTemplate.sendTxtMail(
                "收到新的留言信息",
                "昵称: " + message.getName()
                        + " \n留言信息: " + message.getMessage(),
                mailTemplate.getRootMail());
        return ResponseEntity.ok(null);
    }

    /**
     * 分页获取留言信息 (展示中未展示全部显示)
     * @param page 分页的页码，第一页为1，小于1默认取第一页
     * @param size 每页的条数
     * @return 分页后的数据
     */
    @GetMapping("msg")
    @CheckLogin
    public ResponseEntity<PageInfo<Message>> getMsg(Integer page, Integer size){
        PageInfo<Message> msgs = messageService.selectPageMessage(page, size);
        return ResponseEntity.ok(msgs);
    }

    /**
     * 分页获取留言信息 (只获取展示中)
     * @param page 分页的页码，第一页为1，小于1默认取第一页
     * @param size 每页的条数
     * @return 分页后的数据
     */
    @GetMapping("message")
    public ResponseEntity<PageInfo<Message>> getMsgIsShow(Integer page, Integer size){
        PageInfo<Message> msgs = messageService.selectPageMessageIsshow(page, size);
        return ResponseEntity.ok(msgs);
    }

    /**
     * 根据提供的{@Message}对象更新数据库对应留言id的字段
     * 用于修改留言展示状态
     * @param message 留言信息
     */
    @PutMapping("msg")
    @CheckLogin
    public ResponseEntity<Void> changeShow(@RequestBody Message message){
        Byte isShow = message.getIsShow();
        if (isShow == 1){
            message.setIsShow((byte)0);
        } else {
            message.setIsShow((byte)1);
        }
        if (!messageService.updateByMessage(message)){
            throw new BlogException(ExceptionEnum.MESSAGE_UPDATE_ERROR);
        }
        return ResponseEntity.ok(null);
    }

    /**
     * 根据提供的{@Message}对象删除数据库对应留言id的字段
     * @param message 留言信息
     * @return 是否删除成功
     */
    @DeleteMapping("msg")
    @CheckLogin
    public ResponseEntity<Void> deleteMsg(@RequestBody Message message){
        if(!messageService.deleteMsgById(message)){
            throw new BlogException(ExceptionEnum.MESSAGE_UPDATE_ERROR);
        }
        return ResponseEntity.ok(null);
    }
}

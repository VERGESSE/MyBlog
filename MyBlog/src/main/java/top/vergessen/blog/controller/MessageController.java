package top.vergessen.blog.controller;

import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import top.vergessen.blog.auth.CheckLogin;
import top.vergessen.blog.domain.Message;
import top.vergessen.blog.exception.BlogException;
import top.vergessen.blog.exception.ExceptionEnum;
import top.vergessen.blog.service.MessageService;

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

    /**
     * 获取留言总数
     */
    @GetMapping("messageNum")
    public ResponseEntity<Integer> getMessageNum(){
        return ResponseEntity.ok(messageService.selectMessageCount());
    }

    /**
     * 分页获取留言信息
     * @param page 分页的页码，第一页为1，小于1默认取第一页
     * @param size 每页的条数
     * @return 分页后的数据
     */
    @GetMapping("msg")
    @CheckLogin
    public ResponseEntity<PageInfo<Message>> getMsg(Integer page, Integer size){
        if (page <= 0 ){
            page = 1;
        }
        if (size < 1){
            size = 1;
        }
        PageInfo<Message> msgs = messageService.selectPageMessage(page, size);
        return ResponseEntity.ok(msgs);
    }

    /**
     * 根据提供的{@Message}对象更新数据库对应留言id的字段
     * @param message 留言信息
     * @return 是否修改成功
     */
    @PutMapping("/msg")
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
    @DeleteMapping("/msg")
    @CheckLogin
    public ResponseEntity<Void> deleteMsg(@RequestBody Message message){
        if(!messageService.deleteMsgById(message)){
            throw new BlogException(ExceptionEnum.MESSAGE_UPDATE_ERROR);
        }
        return ResponseEntity.ok(null);
    }
}

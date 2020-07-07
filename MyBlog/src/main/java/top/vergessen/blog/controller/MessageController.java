package top.vergessen.blog.controller;

import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.vergessen.blog.domain.Message;
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
    @GetMapping("getMsg")
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
}

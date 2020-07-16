package top.vergessen.blog.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import top.vergessen.blog.auth.CheckLogin;
import top.vergessen.blog.domain.Friend;
import top.vergessen.blog.service.FriendsService;
import top.vergessen.blog.util.MailTemplate;

import java.util.List;

/**
 * 友链相关API
 * @author Vergessen
 * @date 2020/7/10 20:24.
 */
@RestController
@RequestMapping("friend")
@CrossOrigin
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FriendController {

    private final FriendsService friendsService;
    private final MailTemplate mailTemplate;

    /**
     * 获取全部审核成功的友链列表（乱序）
     */
    @GetMapping("friend")
    public ResponseEntity<List<Friend>> getAllFriends(){
        return ResponseEntity.ok(friendsService.selectAllFriends());
    }

    /**
     * 获取全部未审核成功的友链列表，时间倒序排列
     */
    @GetMapping("friend-n")
    @CheckLogin
    public ResponseEntity<List<Friend>> getAllFriendsNotCheck(){
        return ResponseEntity.ok(friendsService.selectAllFriendsNotCheck());
    }

    /**
     * 后台主动添加友链，不需要审核
     * @param friend 友链信息
     */
    @PostMapping("friend-n")
    @CheckLogin
    public ResponseEntity<Void> addFriendNotCheck(
            @RequestBody Friend friend){
        friend.setState((byte) 1);
        friendsService.addFriend(friend);
        return ResponseEntity.ok(null);
    }

    /**
     * 根据前端提供信息更新友链信息
     * @param friend 新的友链信息
     */
    @PutMapping("friend")
    @CheckLogin
    public ResponseEntity<Void> updateFriend(
            @RequestBody Friend friend ){
        friendsService.updateFriend(friend);
        // 如果前端提供了友链状态信息，则为审核通过友链需给申请人发送邮件
        if (friend.getState() != null && friend.getState() == 1){
            // 给自己邮件提醒
            mailTemplate.sendTxtMail(
                    "友链审核通知",
                    "您申请的友链: " + friend.getName()
                            + " \n地址: " + friend.getUrl()
                            + " \n已经通过审核,已在本博客展示。承蒙厚爱",
                    friend.getEmail());
        }
        return ResponseEntity.ok(null);
    }

    /**
     * 根据友链ID删除友链信息
     * @param id 友链ID
     */
    @DeleteMapping("friend")
    @CheckLogin
    public ResponseEntity<Void> deleteFriend(
            @RequestParam Long id ){
        friendsService.deleteFriendById(id);
        return ResponseEntity.ok(null);
    }

    /**
     * 前台请求动添加友链，需要审核
     * @param friend 友链信息
     */
    @PostMapping("friend")
    public ResponseEntity<Void> addFriendCheck(
            @RequestBody Friend friend){
        friend.setState((byte) 0);
        friendsService.addFriend(friend);
        // 给自己邮件提醒
        mailTemplate.sendTxtMail(
                "收到新的友链申请",
                "友链昵称: " + friend.getName() + " \n地址: " + friend.getUrl(),
                mailTemplate.getRootMail());
        return ResponseEntity.ok(null);
    }
}

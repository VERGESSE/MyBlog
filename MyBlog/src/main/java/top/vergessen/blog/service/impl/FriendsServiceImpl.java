package top.vergessen.blog.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.vergessen.blog.domain.Friend;
import top.vergessen.blog.mapper.FriendMapper;
import top.vergessen.blog.service.FriendsService;
import top.vergessen.blog.util.ImgGoTemplate;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Vergessen
 * @date 2020/7/4 21:42.
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FriendsServiceImpl implements FriendsService {

    private final FriendMapper friendMapper;
    private final ImgGoTemplate imgGoTemplate;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addFriend(Friend friend) {
        // 不是图床图片，先上传
        this.processFriendInfo(friend);
        friend.setId(System.nanoTime());
        friend.setCreateBy(LocalDateTime.now());
        friendMapper.insertSelective(friend);
    }

    @Override
    public List<Friend> selectAllFriends() {
        return friendMapper.selectAllFriends();
    }

    @Override
    public List<Friend> selectAllFriendsNotCheck() {
        return friendMapper.selectAllFriendsNotCheck();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateFriend(Friend friend) {
        // 不是图床图片，先上传
        this.processFriendInfo(friend);
        friendMapper.updateByPrimaryKeySelective(friend);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteFriendById(Long id) {
        friendMapper.deleteByPrimaryKey(id);
    }

    /**
     * 处理友链信息，如果友链头像来源不是图库则上传
     */
    private void processFriendInfo(Friend friend){
        String photo = friend.getPhoto();
        try {
            // 不是图床图片，先上传
            if (!photo.contains(imgGoTemplate.getBaseImgPath())){
                String img = imgGoTemplate.uploadImgByUrl(photo);
                // 防止剪裁失败
                friend.setPhoto(img);
                String thumbImg = imgGoTemplate.getThumbImg(img, 150);
                friend.setPhoto(thumbImg);
            } else if (!photo.contains("150")){
                // 如果是，先剪裁再上传
                String thumbImg = imgGoTemplate.getThumbImg(photo, 150);
                friend.setPhoto(thumbImg);
            }
        } catch (IllegalArgumentException e){
            log.warn("友链头像剪裁失败: " + friend.getPhoto());
        }
    }
}

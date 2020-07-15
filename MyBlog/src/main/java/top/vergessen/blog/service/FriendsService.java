package top.vergessen.blog.service;

import top.vergessen.blog.domain.Friend;

import java.util.List;

/**
 * 博客友链系统服务
 * @author Vergessen
 * @date 2020/7/4 21:42.
 */
public interface FriendsService {

    /**
     * 根据提供的友链信息添加数据库
     * @param friend 友链信息
     */
    void addFriend(Friend friend);

    /**
     * 查询获取全部的友链信息（乱序）
     * @return 友链信息列表
     */
    List<Friend> selectAllFriends();

    /**
     * 查询获取全部的友链信息,时间倒序排列
     * @return 友链信息列表
     */
    List<Friend> selectAllFriendsNotCheck();

    /**
     * 根据前端提供信息更新友链信息
     * @param friend 新的友链信息
     */
    void updateFriend(Friend friend);

    /**
     * 根据友链ID删除友链信息
     * @param id 友链ID
     */
    void deleteFriendById(Long id);
}

package top.vergessen.blog.mapper;

import tk.mybatis.mapper.common.Mapper;
import top.vergessen.blog.domain.Friend;

import java.util.List;

/**
 * @author Vergessen
 * @date 2020/7/4 21:20.
 */
public interface FriendMapper extends Mapper<Friend> {

    /**
     * 查询获取全部的友链信息（乱序）
     * @return 友链信息列表
     */
    List<Friend> selectAllFriends();

    /**
     * 查询所有未审核通过的友链信息（按时间倒序）
     * @return 友链信息列表
     */
    List<Friend> selectAllFriendsNotCheck();
}

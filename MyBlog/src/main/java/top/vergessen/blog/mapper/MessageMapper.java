package top.vergessen.blog.mapper;

import tk.mybatis.mapper.common.Mapper;
import top.vergessen.blog.domain.Message;

import java.util.List;

/**
 * @author Vergessen
 * @date 2020/7/4 21:20.
 */
public interface MessageMapper extends Mapper<Message> {

    /**
     * 根据时间倒序排序查询留言信息
     * @return 留言信息集合
     */
    List<Message> selectAllOrderByTime();

    /**
     * 分页获取展示中的评论
     * @return 评论列表
     */
    List<Message> selectAllOrderByTimeIsShoe();
}

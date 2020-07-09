package top.vergessen.blog.service;

import com.github.pagehelper.PageInfo;
import top.vergessen.blog.domain.Message;

/**
 * 留言相关服务
 * @author Vergessen
 * @date 2020/7/4 21:43.
 */
public interface MessageService {

    /**
     * 获取总的留言数
     * @return 留言数量
     */
    Integer selectMessageCount();

    /**
     * 分页获取评论
     * @param page 页码第一页为1
     * @param size 每页的条数
     * @return 分页信息
     */
    PageInfo<Message> selectPageMessage(Integer page, Integer size);

    /**
     * 根据提供的{@Message}对象更新数据库对应留言id的字段
     * @param message 留言信息
     * @return 是否修改成功
     */
    boolean updateByMessage(Message message);

    /**
     * 根据提供的{@Message}对象删除数据库对应留言id的字段
     * @param message 留言信息
     * @return 是否删除成功
     */
    boolean deleteMsgById(Message message);
}

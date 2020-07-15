package top.vergessen.blog.service;

import com.github.pagehelper.PageInfo;
import top.vergessen.blog.domain.GoodArticle;

/**
 * 优质博文分享服务
 * @author Vergessen
 * @date 2020/7/12 11:21.
 */
public interface GoodArticleService {

    /**
     * 获取全部审核成功的外链列表，时间倒序排列
     * @param page 页码
     * @param size 每页条数
     * @return 分页信息
     */
    PageInfo<GoodArticle> selectAllLinks(Integer page, Integer size);

    /**
     * 获取全部未审核成功的外链列表，时间倒序排列
     * @param page 页码
     * @param size 每页条数
     * @return 分页信息
     */
    PageInfo<GoodArticle> selectAllLinksNotCheck(Integer page, Integer size);

    /**
     * 添加外链
     * @param goodArticle 友链信息
     */
    void addNewLink(GoodArticle goodArticle);

    /**
     * 根据前端提供信息更新外链信息
     * @param goodArticle 新的外链信息
     */
    void updateLink(GoodArticle goodArticle);

    /**
     * 根据友链ID删除外链信息
     * @param id 外链ID
     */
    void deleteLinkById(Long id);
}

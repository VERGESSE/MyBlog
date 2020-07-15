package top.vergessen.blog.mapper;

import tk.mybatis.mapper.common.Mapper;
import top.vergessen.blog.domain.GoodArticle;

import java.util.List;

/**
 * 优质博文分享表表
 * @author Vergessen
 * @date 2020/7/5 15:42.
 */
public interface GoodArticleMapper extends Mapper<GoodArticle> {

    /**
     * 按时间倒序返回所有 state(审核状态）状态 的外链
     * @param state 审核状态
     * @return 外链列表
     */
    List<GoodArticle> selectAllLinks(Byte state);
}

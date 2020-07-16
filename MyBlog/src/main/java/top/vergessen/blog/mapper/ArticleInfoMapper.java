package top.vergessen.blog.mapper;

import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;
import top.vergessen.blog.domain.ArticleInfo;

import java.util.List;

/**
 * @author Vergessen
 * @date 2020/7/4 21:20.
 */
public interface ArticleInfoMapper extends Mapper<ArticleInfo> {

    /**
     * 根据搜索和分类信息从数据库查询数据
     * @param search 搜索信息
     * @param category 分类信息
     * @param isTop 是否获取置顶
     * @return 博文数据集合
     */
    List<ArticleInfo> getArticleByCategoryAndSearchAndPage(
            @Param("search") String search,
            @Param("category") String category,
            @Param("isTop") Boolean isTop);

    /**
     * 根据文章Id增加访问量
     * @param articleId 文章
     */
    void increaseViewNum(Long articleId);

    /**
     * 根据文章Id获取文章标题
     * @param articleId 文章Id
     * @return 文章标题
     */
    String getTitleById(Long articleId);

    /**
     * 获取全部有评论的博文的标题和id
     * @return 博文列表
     */
    List<ArticleInfo> getArticleHasComment();
}

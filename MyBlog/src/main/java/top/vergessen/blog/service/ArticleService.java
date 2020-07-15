package top.vergessen.blog.service;

import com.github.pagehelper.PageInfo;
import top.vergessen.blog.domain.ArticleComment;
import top.vergessen.blog.domain.ArticleInfo;
import top.vergessen.blog.domain.vo.ArticleInfoVO;
import top.vergessen.blog.domain.vo.ArticleVO;

import java.util.List;

/**
 * 博客文章分类系统Service
 * @author Vergessen
 * @date 2020/7/4 21:41.
 */
public interface ArticleService {

    /**
     * 获取总的博文数
     * @return 博文数
     */
    Integer selectArticleCount();

    /**
     * 根据文章ID获取文章信息
     * @param id 文章ID
     * @return ID对应的文章主体信息
     */
    ArticleVO getArticleById(Long id);

    /**
     * 创建新的博文
     * @param articleVO 新博文信息
     */
    void createArticle(ArticleVO articleVO);

    /**
     * 分页获取博文
     * @param page 页码
     * @param size 每页条数
     * @param search 搜索条件
     * @param category 分类
     * @param isTop 是否获取置顶博文
     * @return 搜索获取的结果集
     */
    PageInfo<ArticleInfo> getArticleByCategoryAndSearchAndPage(
            Integer page, Integer size, String search, String category, Boolean isTop);

    /**
     * 根据文章ID删除文章
     * @param id 要删除的文章Id
     */
    void deleteArticleById(Long id);

    /**
     * 根据文章ID更新文章信息
     * @param id 文章ID
     * @param articleVO 更新的文章信息
     */
    void updateArticleById(Long id, ArticleVO articleVO);

    /**
     * 分页获取完成的前端展示的文章信息
     * @param page 页码
     * @param size 每页条数
     * @return 文章信息
     */
    List<ArticleInfoVO> getTopArticle(int page, int size);

    /**
     * 分页获取完成的博客页展示的文章信息
     * @param page 页码
     * @param size 每页条数
     * @param search 搜索条件
     * @param category 分类
     * @return 搜索获取的结果集
     */
    List<ArticleInfoVO> getArticleList(int page, int size,String search, String category);

    /**
     * 根据文章Id增加访问量
     * @param articleId 文章
     */
    void increaseViewNum(Long articleId);

    /**
     * 添加评论信息
     * @param comment 评论信息
     */
    void addComment(ArticleComment comment);

    /**
     * 根据博文id获取评论信息
     * @param articleId 博文id
     * @return 评论信息列表
     */
    List<ArticleComment> getCommentByArticleId(Long articleId);

    /**
     * 根据评论id删除评论
     * @param commentId 评论id
     */
    void deleteCommentById(Long commentId);
}

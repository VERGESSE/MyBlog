package top.vergessen.blog.service;

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
}

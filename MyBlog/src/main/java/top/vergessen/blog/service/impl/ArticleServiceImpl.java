package top.vergessen.blog.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import top.vergessen.blog.domain.ArticleInfo;
import top.vergessen.blog.mapper.ArticleCategoryMapper;
import top.vergessen.blog.mapper.ArticleCommentMapper;
import top.vergessen.blog.mapper.ArticleContentMapper;
import top.vergessen.blog.mapper.ArticleInfoMapper;
import top.vergessen.blog.service.ArticleService;

/**
 * @author Vergessen
 * @date 2020/7/4 21:41.
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ArticleServiceImpl implements ArticleService {

    private final ArticleInfoMapper articleInfoMapper;
    private final ArticleCategoryMapper articleCategoryMapper;
    private final ArticleContentMapper articleContentMapper;
    private final ArticleCommentMapper articleCommentMapper;

    @Override
    public Integer selectArticleCount() {
        return articleInfoMapper.selectCountByExample(new Example(ArticleInfo.class));
    }
}

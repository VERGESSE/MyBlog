package top.vergessen.blog.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import top.vergessen.blog.domain.*;
import top.vergessen.blog.domain.vo.ArticleInfoVO;
import top.vergessen.blog.domain.vo.ArticleVO;
import top.vergessen.blog.mapper.*;
import top.vergessen.blog.service.ArticleService;
import top.vergessen.blog.service.PictureService;
import top.vergessen.blog.util.Markdown2HtmlUtil;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    private final CategoryInfoMapper categoryInfoMapper;
    private final ArticleCommentMapper articleCommentMapper;
    private final PictureService pictureService;

    @Override
    public Integer selectArticleCount() {
        return articleInfoMapper.selectCountByExample(
                new Example(ArticleInfo.class));
    }

    @Override
    public ArticleVO getArticleById(Long id, Boolean markdown) {
        // 根据ID获取文章信息
        ArticleInfo articleInfo = articleInfoMapper.selectByPrimaryKey(id);
        // 获取文章文本
        ArticleContent articleContent = articleContentMapper.selectByPrimaryKey(id);
        // 构造返回给前端的文章信息
        ArticleVO articleVO = ArticleVO.builder()
                .id(id)
                .title(articleInfo.getTitle())
                .summary(articleInfo.getSummary())
                .tags(Arrays.asList(StringUtils.split(articleInfo.getTags(), "|")))
                .traffic(articleInfo.getTraffic())
                .isTop(articleInfo.getIsTop())
                .createBy(articleInfo.getCreateBy())
                .build();
        if (markdown){
            // 将Markdown格式的文本转为HTML
            articleVO.setContent(Markdown2HtmlUtil.markdown2html(
                            articleContent.getContent()));
        } else {
            articleVO.setContent(articleContent.getContent());
        }
        return articleVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createArticle(ArticleVO articleVO) {
        List<String> tags = articleVO.getTags();
        LocalDateTime now = LocalDateTime.now();
        // 以时间戳作为博文id
        Long articleId = System.nanoTime();
        // 构建并向数据库写入博文信息对象
        ArticleInfo articleInfo = ArticleInfo.builder()
                .id(articleId)
                .title(articleVO.getTitle())
                .summary(articleVO.getSummary())
                .isTop(articleVO.getIsTop())
                .tags(StringUtils.join(tags, "|"))
                .createBy(now)
                .build();
        articleInfoMapper.insertSelective(articleInfo);
        ArticleContent articleContent = ArticleContent.builder()
                .articleId(articleId)
                .content(articleVO.getContent())
                .createBy(now)
                .build();
        articleContentMapper.insertSelective(articleContent);

        // 向分类表对应分类文章数加一
        for (String tag : tags) {
            categoryInfoMapper.increaseNumByTag(tag);
        }

        // 向分类文章中间表添加数据
        for (String tag : tags) {
            ArticleCategory articleCategory = ArticleCategory.builder()
                    .categoryName(tag)
                    .articleId(articleId)
                    .build();
            articleCategoryMapper.insertSelective(articleCategory);
        }
    }

    @Override
    public PageInfo<ArticleInfo> getArticleByCategoryAndSearchAndPage(
            Integer page, Integer size, String search, String category, Boolean isTop) {
        // 根据分类和搜索信息获取相关博文信息
        return PageHelper.startPage(page, size).doSelectPageInfo( () ->
            articleInfoMapper.getArticleByCategoryAndSearchAndPage(search, category, isTop));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteArticleById(Long id) {
        // 获取id对应的文章
        ArticleInfo oldArticle = articleInfoMapper.selectByPrimaryKey(id);
        String[] tags = StringUtils.split(oldArticle.getTags(), "|");
        // 对应分类文章数减一
        for (String tag : tags) {
            categoryInfoMapper.decreaseNumByTag(tag);
        }
        // 文章分类中间表根据文章ID删除数据
        articleCategoryMapper.delete(
                ArticleCategory.builder().articleId(id).build());
        // 删除文章信息表数据
        articleInfoMapper.deleteByPrimaryKey(id);
        // 删除文章主体表数据
        articleContentMapper.deleteByPrimaryKey(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateArticleById(Long id, ArticleVO articleVO) {
        // 获取老的博文信息
        ArticleInfo oldArticleInfo = articleInfoMapper.selectByPrimaryKey(id);
        String[] oldTags = StringUtils.split(oldArticleInfo.getTags(), "|");
        List<String> tags = articleVO.getTags();
        // 构建并向数据库写入博文信息对象(更新)
        ArticleInfo articleInfo = ArticleInfo.builder()
                .id(id)
                .title(articleVO.getTitle())
                .summary(articleVO.getSummary())
                .isTop(articleVO.getIsTop())
                .tags(StringUtils.join(tags, "|"))
                .build();
        articleInfoMapper.updateByPrimaryKeySelective(articleInfo);

        // 更新文章主体信息表
        ArticleContent articleContent = ArticleContent.builder()
                .articleId(id)
                .content(articleVO.getContent())
                .build();
        articleContentMapper.updateByPrimaryKeySelective(articleContent);

        // 对分类表中的文章数量进行更新
        // 先把老的信息全减掉
        for (String oldTag : oldTags) {
            categoryInfoMapper.decreaseNumByTag(oldTag);
        }
        // 再把新的分类信息加上
        for (String tag : tags) {
            categoryInfoMapper.increaseNumByTag(tag);
        }

        // 先删除之前所有该Id对应博文的分类信息
        articleCategoryMapper.delete(
                ArticleCategory.builder().articleId(id).build());
        // 向分类文章中间表添加数据
        for (String tag : tags) {
            ArticleCategory articleCategory = ArticleCategory.builder()
                    .categoryName(tag)
                    .articleId(id)
                    .build();
            articleCategoryMapper.insertSelective(articleCategory);
        }
    }

    @Override
    public List<ArticleInfoVO> getTopArticle(int page, int size) {
        PageInfo<ArticleInfo> list = this.getArticleByCategoryAndSearchAndPage(page, size, "", "", true);
        List<ArticleInfo> articleInfos = list.getList();
        return this.processVoFromInfo(articleInfos);
    }

    @Override
    public List<ArticleInfoVO> getArticleList(int page, int size, String search, String category) {
        PageInfo<ArticleInfo> list = this.getArticleByCategoryAndSearchAndPage(page, size, search, category, false);
        List<ArticleInfo> articleInfos = list.getList();
        return this.processVoFromInfo(articleInfos);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void increaseViewNum(Long articleId) {
        articleInfoMapper.increaseViewNum(articleId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addComment(ArticleComment comment) {
        articleCommentMapper.insertSelective(comment);
    }

    @Override
    public List<ArticleComment> getCommentByArticleId(Long articleId) {
        ArticleComment comment = ArticleComment.builder()
                .articleId(articleId).build();
        return articleCommentMapper.select(comment);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCommentById(Long commentId) {
        articleCommentMapper.deleteByPrimaryKey(commentId);
    }

    @Override
    public String getArticleTitleById(Long articleId) {
        return articleInfoMapper.getTitleById(articleId);
    }

    @Override
    public List<ArticleInfo> getArticleHasComment() {
        return articleInfoMapper.getArticleHasComment();
    }

    private List<ArticleInfoVO> processVoFromInfo(List<ArticleInfo> infos){
        List<ArticleInfoVO> vos = new ArrayList<>();
        List<Picture> pics = pictureService.getRandomPic(Picture.ARTICLE_PIC, infos.size());
        for (int i = 0; i < infos.size(); i++) {
            ArticleInfoVO vo = new ArticleInfoVO();
            ArticleInfo articleInfo = infos.get(i);
            BeanUtils.copyProperties(articleInfo, vo);
            vo.setTags(Arrays.asList(
                    StringUtils.split(articleInfo.getTags(), "|")));
            vo.setPictureUrl(pics.get(i).getPictureUrl());
            vo.setPictureUrlSmall(pics.get(i).getPictureUrlSmall());
            vos.add(vo);
        }
        return vos;
    }
}

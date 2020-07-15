package top.vergessen.blog.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.vergessen.blog.domain.GoodArticle;
import top.vergessen.blog.mapper.GoodArticleMapper;
import top.vergessen.blog.service.GoodArticleService;

import java.time.LocalDateTime;

/**
 * @author Vergessen
 * @date 2020/7/12 11:22.
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GoodArticleServiceImpl implements GoodArticleService {

    private final GoodArticleMapper goodArticleMapper;

    @Override
    public PageInfo<GoodArticle> selectAllLinks(Integer page, Integer size) {
        return PageHelper.startPage(page, size).doSelectPageInfo(
                () -> goodArticleMapper.selectAllLinks((byte) 1));
    }

    @Override
    public PageInfo<GoodArticle> selectAllLinksNotCheck(Integer page, Integer size) {
        return PageHelper.startPage(page, size).doSelectPageInfo(
                () -> goodArticleMapper.selectAllLinks((byte) 0));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addNewLink(GoodArticle goodArticle) {
        goodArticle.setId(System.nanoTime());
        goodArticle.setCreateBy(LocalDateTime.now());
        goodArticleMapper.insertSelective(goodArticle);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateLink(GoodArticle goodArticle) {
        goodArticleMapper.updateByPrimaryKeySelective(goodArticle);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteLinkById(Long id) {
        goodArticleMapper.deleteByPrimaryKey(id);
    }
}

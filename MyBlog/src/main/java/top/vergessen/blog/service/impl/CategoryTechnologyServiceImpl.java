package top.vergessen.blog.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import top.vergessen.blog.domain.ArticleCategory;
import top.vergessen.blog.domain.ArticleInfo;
import top.vergessen.blog.domain.CategoryInfo;
import top.vergessen.blog.domain.Technology;
import top.vergessen.blog.exception.BlogException;
import top.vergessen.blog.exception.ExceptionEnum;
import top.vergessen.blog.mapper.*;
import top.vergessen.blog.service.CategoryTechnologyService;
import top.vergessen.blog.util.ImgGoTemplate;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Vergessen
 * @date 2020/7/4 21:42.
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CategoryTechnologyServiceImpl implements CategoryTechnologyService {

    private final CategoryInfoMapper categoryMapper;
    private final TechnologyMapper technologyMapper;
    private final ImgGoTemplate imgGoTemplate;
    private final ArticleCategoryMapper articleCategoryMapper;
    private final ArticleInfoMapper articleInfoMapper;

    @Override
    public List<CategoryInfo> selectAllCategory() {
        return categoryMapper.selectAllByNumber();
    }

    @Override
    public List<CategoryInfo> selectAllCategoryNumNotZero() {
        return categoryMapper.selectAllCategoryNumNotZero();
    }

    @Override
    public List<Technology> selectAllTechnology() {
        return technologyMapper.selectAll();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addCategory(CategoryInfo categoryInfo) {
        // 判断是否已经存在该分类名称
        if(categoryMapper.selectOne(CategoryInfo.builder()
                .name(categoryInfo.getName()).build()) != null) {
            throw new BlogException(ExceptionEnum.INSERT_CATEGORY_ERROR);
        }
        // 判断是否是图床图片，如果是则剪裁，如果不是则先上传图库在剪裁存储
        this.processCategory(categoryInfo);
        categoryInfo.setCreateBy(LocalDateTime.now());
        categoryMapper.insertSelective(categoryInfo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateCategory(CategoryInfo categoryInfo) {
        String newName = categoryInfo.getName();
        // 判断是否已经存在该分类名称
        CategoryInfo oldCategory = categoryMapper.selectByPrimaryKey(categoryInfo.getId());
        if(null == oldCategory) {
            throw new BlogException(ExceptionEnum.UPDATE_CATEGORY_ERROR);
        }
        String oldName = oldCategory.getName();
        // 如果图标信息发生变化删除老的图标, 并上传新的图标
        if (!oldCategory.getPictureUrl().equals(categoryInfo.getPictureUrl())){
            String pictureUrl = oldCategory.getPictureUrl();
            deleteRelateImg(pictureUrl);
            this.processCategory(categoryInfo);
        }
        // 更新分类表信息
        categoryMapper.updateByPrimaryKeySelective(categoryInfo);
        // 如果分类名称发生变化 则更新关联表数据和数据库标签数据
        if (!oldCategory.getName().equals(categoryInfo.getName())) {
            // 更新关联表数据
            articleCategoryMapper.updateCategoryName(oldName, newName);
            // 更新文章表标签数据
            List<ArticleCategory> list = articleCategoryMapper.select(
                    ArticleCategory.builder().categoryName(newName).build());
            for (ArticleCategory articleCategory : list) {
                Long articleId = articleCategory.getArticleId();
                ArticleInfo info = articleInfoMapper.selectByPrimaryKey(articleId);
                String newInfo = StringUtils.replace(info.getTags(), oldName, newName);
                info.setTags(newInfo);
                articleInfoMapper.updateByPrimaryKeySelective(info);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCategory(CategoryInfo categoryInfo) {
        String oldName = categoryInfo.getName();
        // 如果存在该名称的分类信息，则删除
        CategoryInfo oldCategory = categoryMapper.selectByPrimaryKey(categoryInfo.getId());
        if (oldCategory != null) {
            // 删除图标信息
            String pictureUrl = oldCategory.getPictureUrl();
            deleteRelateImg(pictureUrl);
            // 删除数据库信息
            categoryMapper.deleteByPrimaryKey(categoryInfo.getId());
            // 更新文章表标签数据
            List<ArticleCategory> list = articleCategoryMapper.select(
                    ArticleCategory.builder().categoryName(oldName).build());
            for (ArticleCategory articleCategory : list) {
                Long articleId = articleCategory.getArticleId();
                ArticleInfo info = articleInfoMapper.selectByPrimaryKey(articleId);
                String newTags = StringUtils.replace(info.getTags(), oldName, "");
                info.setTags(StringUtils.join(
                        StringUtils.split(newTags, "|"),
                        "|"));
                articleInfoMapper.updateByPrimaryKeySelective(info);
            }
            // 删除关联表数据
            Example example = new Example(ArticleCategory.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("categoryName", oldName);
            articleCategoryMapper.deleteByExample(example);
        }
    }

    /**
     * 处理类型信息的图片内容
     */
    private void processCategory(CategoryInfo categoryInfo){
        String oldImg = categoryInfo.getPictureUrl();
        if (!oldImg.contains(imgGoTemplate.getBaseImgPath())) {
            oldImg = imgGoTemplate.uploadImgByUrl(oldImg);
            categoryInfo.setPictureUrl(oldImg);
        }
        try {
            categoryInfo.setPictureUrl(imgGoTemplate.getThumbImg(
                    oldImg, 350));
        } catch (IllegalArgumentException e) {
            throw new BlogException(ExceptionEnum.INSERT_CATEGORY_ERROR);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addTechnology(Technology technology) {
        // 判断是否已经存在该技术名称
        if(technologyMapper.selectOne(Technology.builder()
                .name(technology.getName()).build()) != null) {
            throw new BlogException(ExceptionEnum.INSERT_TECHNOLOGY_ERROR);
        }
        String oldImg = technology.getPictureUrl();
        // 判断是否是图床图片，如果是则剪裁，如果不是则先上传服务器在存储
        this.processTechnology(technology);
        technology.setCreateBy(LocalDateTime.now());
        technologyMapper.insertSelective(technology);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateTechnology(Technology technology) {
        // 判断是否已经存在该技术名称
        Technology oldTechnology = technologyMapper.selectByPrimaryKey(technology.getId());
        if(oldTechnology == null) {
            throw new BlogException(ExceptionEnum.UPDATE_TECHNOLOGY_ERROR);
        }
        // 如果图标信息发生变化则删除老的图标,所以不同的技术栈信息应该选择不同的图片
        if (!oldTechnology.getPictureUrl().equals(technology.getPictureUrl())){
            String pictureUrl = oldTechnology.getPictureUrl();
            deleteRelateImg(pictureUrl);
            this.processTechnology(technology);
        }
        technologyMapper.updateByPrimaryKeySelective(technology);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteTechnology(Technology technology) {
        // 如果存在该技术栈信息则删除
        Technology oldTechnology = technologyMapper.selectByPrimaryKey(technology.getId());
        if(oldTechnology != null) {
            // 删除删除信息的图标
            String pictureUrl = oldTechnology.getPictureUrl();
            deleteRelateImg(pictureUrl);
            // 删除数据库信息
            technologyMapper.deleteByPrimaryKey(technology.getId());
        }
    }

    /**
     * 处理类型信息的图片内容
     */
    private void processTechnology(Technology technology){
        String oldImg = technology.getPictureUrl();
        if (!oldImg.contains(imgGoTemplate.getBaseImgPath())) {
            oldImg = imgGoTemplate.uploadImgByUrl(oldImg);
            technology.setPictureUrl(oldImg);
        }
        try {
            technology.setPictureUrl(imgGoTemplate.getThumbImg(
                    oldImg, 350));
        } catch (IllegalArgumentException e) {
            throw new BlogException(ExceptionEnum.INSERT_CATEGORY_ERROR);
        }
    }

    /**
     * 根据提供的缩略图地址删除所有图库图片
     * @param pictureUrl 缩略图地址
     */
    private void deleteRelateImg(String pictureUrl){
        imgGoTemplate.deleteImg(pictureUrl.split("_")[0] + ".png");
        imgGoTemplate.deleteImg((pictureUrl));
    }
}

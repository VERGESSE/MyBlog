package top.vergessen.blog.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.vergessen.blog.domain.CategoryInfo;
import top.vergessen.blog.domain.Technology;
import top.vergessen.blog.exception.BlogException;
import top.vergessen.blog.exception.ExceptionEnum;
import top.vergessen.blog.mapper.CategoryInfoMapper;
import top.vergessen.blog.mapper.TechnologyMapper;
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

    @Value("${imgGo.serverUrl}")
    private String serverUrl;

    @Override
    public List<CategoryInfo> selectAllCategory() {
        return categoryMapper.selectAll();
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
        String oldImg = categoryInfo.getPictureUrl();
        // 判断是否是图床图片，如果是则剪裁，如果不是则直接存储
        // TODO 如果不是图床图片则先上传剪裁后再存储数据库
        if (oldImg.contains(serverUrl)) {
            try {
                categoryInfo.setPictureUrl(
                        serverUrl + imgGoTemplate.getThumbImg(
                                oldImg, 350));
            } catch (IllegalArgumentException e) {
                throw new BlogException(ExceptionEnum.INSERT_CATEGORY_ERROR);
            }
            categoryInfo.setCreateBy(LocalDateTime.now());
            categoryMapper.insertSelective(categoryInfo);
        } else {
            categoryMapper.insertSelective(categoryInfo);
        }
    }

    @Override
    public void updateCategory(CategoryInfo categoryInfo) {
        // 判断是否已经存在该分类名称
        CategoryInfo oldCategory = categoryMapper.selectByPrimaryKey(categoryInfo.getId());
        if(oldCategory == null) {
            throw new BlogException(ExceptionEnum.UPDATE_CATEGORY_ERROR);
        }
        // 如果图标信息发生变化删除老的图标
        if (!oldCategory.getPictureUrl().equals(categoryInfo.getPictureUrl())){
            String pictureUrl = oldCategory.getPictureUrl();
            deleteRelateImg(pictureUrl);
        }
        categoryMapper.updateByPrimaryKeySelective(categoryInfo);
    }

    @Override
    public void deleteCategory(CategoryInfo categoryInfo) {
        // 如果存在该名称的分类信息，则删除
        CategoryInfo oldCategory = categoryMapper.selectByPrimaryKey(categoryInfo.getId());
        if (oldCategory != null) {
            // 删除图标信息
            String pictureUrl = oldCategory.getPictureUrl();
            deleteRelateImg(pictureUrl);
            // 删除数据库信息
            categoryMapper.deleteByPrimaryKey(categoryInfo.getId());
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
        // 判断是否是图床图片，如果是则剪裁，如果不是则直接存储
        // TODO 如果不是图床图片则先上传剪裁后再存储数据库
        if (oldImg.contains(serverUrl)) {
            try {
                technology.setPictureUrl(
                        serverUrl + imgGoTemplate.getThumbImg(
                                oldImg, 160));
            } catch (IllegalArgumentException e) {
                throw new BlogException(ExceptionEnum.INSERT_TECHNOLOGY_ERROR);
            }
            technology.setCreateBy(LocalDateTime.now());
            technologyMapper.insertSelective(technology);
        } else {
            technologyMapper.insertSelective(technology);
        }
    }

    @Override
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
        }
        technologyMapper.updateByPrimaryKeySelective(technology);
    }

    @Override
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
     * 根据提供的缩略图地址删除所有图库图片
     * @param pictureUrl 缩略图地址
     */
    private void deleteRelateImg(String pictureUrl){
        imgGoTemplate.deleteImg(pictureUrl.split("_")[0] + ".png");
        imgGoTemplate.deleteImg((pictureUrl));
    }
}

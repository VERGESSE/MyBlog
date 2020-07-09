package top.vergessen.blog.service;

import top.vergessen.blog.domain.CategoryInfo;
import top.vergessen.blog.domain.Technology;

import java.util.List;

/**
 * 博客文章分类系统Service
 * @author Vergessen
 * @date 2020/7/4 21:42.
 */
public interface CategoryTechnologyService {

    /**
     * 获取全部分类信息
     * @return 分类信息列表
     */
    List<CategoryInfo> selectAllCategory();

    /**
     * 新增分类
     * @param categoryInfo 前端提供的分类信息
     */
    void addCategory(CategoryInfo categoryInfo);

    /**
     * 更新分类信息,如果图标变化会删除原图片
     * @param categoryInfo 前端提供的更新数据
     */
    void updateCategory(CategoryInfo categoryInfo);

    /**
     * 删除分类信息,如果图标变化会删除原图片
     * @param categoryInfo 需要删除的分类信息
     */
    void deleteCategory(CategoryInfo categoryInfo);

    /**
     * 获取全部技术栈信息
     * @return 技术栈信息列表
     */
    List<Technology> selectAllTechnology();

    /**
     * 新增技术栈
     * @param technology 前端提供的技术栈信息
     */
    void addTechnology(Technology technology);

    /**
     * 更新技术栈信息，不添加重复技术栈,如果分类图标变化会删除原图片
     * @param technology 前端提供的新增数据
     */
    void updateTechnology(Technology technology);

    /**
     * 删除技术栈信息,如果分类图标变化会删除原图片
     * @param technology 需要删除的信息
     */
    void deleteTechnology(Technology technology);
}

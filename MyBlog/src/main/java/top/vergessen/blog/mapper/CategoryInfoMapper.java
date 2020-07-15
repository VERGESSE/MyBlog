package top.vergessen.blog.mapper;

import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;
import top.vergessen.blog.domain.CategoryInfo;

import java.util.List;

/**
 * @author Vergessen
 * @date 2020/7/4 21:20.
 */
public interface CategoryInfoMapper extends Mapper<CategoryInfo> {

    /**
     * 使对应名称的分类的文章数加一
     * @param tag 分类名称
     */
    void increaseNumByTag(String tag);

    /**
     * 使对应名称的分类的文章数减一
     * @param tag 分类名称
     */
    void decreaseNumByTag(String tag);

    /**
     * 返回按文章数量排序的分类列表
     * @return 分类列表
     */
    List<CategoryInfo> selectAllByNumber();

    /**
     * 返回文章数量不为0的分类列表 按文章数量倒序排序
     * @return 分类列表
     */
    List<CategoryInfo> selectAllCategoryNumNotZero();

    /**
     * 更新分类文章关联表数据
     * @param oldName 老的名称
     * @param newName 新的名称
     */
    void updateCategoryName(@Param("oldName") String oldName, @Param("newName") String newName);
}

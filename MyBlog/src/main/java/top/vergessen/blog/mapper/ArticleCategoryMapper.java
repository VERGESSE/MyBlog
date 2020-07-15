package top.vergessen.blog.mapper;

import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;
import top.vergessen.blog.domain.ArticleCategory;

/**
 * @author Vergessen
 * @date 2020/7/4 21:20.
 */
public interface ArticleCategoryMapper extends Mapper<ArticleCategory> {

    /**
     * 更新分类文章关联表数据
     * @param oldName 老的名称
     * @param newName 新的名称
     */
    void updateCategoryName(@Param("oldName") String oldName, @Param("newName") String newName);
}

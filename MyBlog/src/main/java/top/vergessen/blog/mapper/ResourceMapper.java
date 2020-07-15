package top.vergessen.blog.mapper;

import tk.mybatis.mapper.common.Mapper;
import top.vergessen.blog.domain.Resource;

import java.util.List;

/**
 * @author Vergessen
 * @date 2020/7/5 10:47.
 */
public interface ResourceMapper extends Mapper<Resource> {

    /**
     * 获取全部资源信息，按创建时间顺序排序
     * @return 资源信息
     */
    List<Resource> selectAllOrderByTime();
}

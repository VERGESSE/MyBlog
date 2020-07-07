package top.vergessen.blog.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;
import top.vergessen.blog.domain.Picture;

import java.util.List;

/**
 * @author Vergessen
 * @date 2020/7/4 21:20.
 */
public interface PictureMapper extends Mapper<Picture> {

    /**
     * 随机获取num张type类型的图片地址
     * @param type 图片类型
     * @param num 图片数量
     * @return 图片地址集合
     */
    List<Picture> getRandomPic(@Param("type") Byte type, @Param("num") Integer num);
}

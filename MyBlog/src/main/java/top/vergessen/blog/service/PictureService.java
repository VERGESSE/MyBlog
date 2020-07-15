package top.vergessen.blog.service;

import com.github.pagehelper.PageInfo;
import top.vergessen.blog.domain.Picture;

import java.util.List;

/**
 * 图床相关服务
 * @author Vergessen
 * @date 2020/7/4 21:44.
 */
public interface PictureService {

    /**
     * 随机获取num张type类型的图片地址
     * @param type 图片类型
     * @param num 图片数量
     * @return 图片地址集合
     */
    List<Picture> getRandomPic(Byte type, Integer num);

    /**
     * 随机获取1张type类型的图片地址
     * @param type 图片类型
     * @return 图片地址集合
     */
    Picture getRandomPicOne(Byte type);

    /**
     * 根据图片类型分页获取图库信息（时间倒序排序）
     * @param page 页码
     * @param size 每页条数
     * @param type 图片类型
     * @return 分页信息
     */
    PageInfo<Picture> getPicPageByType(Integer page, Integer size, Byte type);

    /**
     * 保存图片
     * @param imgUrl 图片地址
     * @param type 图片类型
     */
    void uploadPic(String imgUrl, Byte type);

    /**
     * 根据前端提供的图片Id删除图片
     * @param id 图片Id
     */
    void deletePicById(Long id);
}

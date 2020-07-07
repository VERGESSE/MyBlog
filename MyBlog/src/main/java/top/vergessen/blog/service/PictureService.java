package top.vergessen.blog.service;

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
}

package top.vergessen.blog.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.vergessen.blog.domain.Picture;
import top.vergessen.blog.mapper.PictureMapper;
import top.vergessen.blog.service.PictureService;
import top.vergessen.blog.util.ImgGoTemplate;

import java.time.LocalDateTime;
import java.util.List;

import static top.vergessen.blog.domain.Picture.*;

/**
 * @author Vergessen
 * @date 2020/7/4 21:44.
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PictureServiceImpl implements PictureService {

    private final PictureMapper pictureMapper;
    private final ImgGoTemplate imgGoTemplate;

    @Override
    public List<Picture> getRandomPic(Byte type, Integer num) {
        return pictureMapper.getRandomPic(type, num);
    }

    @Override
    public Picture getRandomPicOne(Byte type) {
        List<Picture> pic = this.getRandomPic(type, 1);
        return pic.size() > 0 ? pic.get(0): null;
    }

    @Override
    public PageInfo<Picture> getPicPageByType(Integer page, Integer size, Byte type) {
        return PageHelper.startPage(page, size).doSelectPageInfo(
                () -> pictureMapper.getPicPageByType(type));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void uploadPic(String imgUrl, Byte type) {
        // 构造Picture对象
        Picture pic = Picture.builder()
                .id(System.nanoTime())
                .pictureType(type)
                .pictureUrl(imgUrl)
                .createBy(LocalDateTime.now())
                .build();
        // 根据不同的图片类型新建不同像素的图片
        this.updatePicByType(pic, type);
        // 新建数据库字段
        pictureMapper.insertSelective(pic);
    }

    @Override
    public void deletePicById(Long id) {
        Picture delPic = pictureMapper.selectByPrimaryKey(id);
        this.deleteRelateImg(delPic.getPictureUrl());
        if (delPic.getPictureType().equals(Picture.ARTICLE_PIC)){
            this.deleteRelateImg(delPic.getPictureUrlSmall());
        }
        pictureMapper.deleteByPrimaryKey(id);
    }

    private void updatePicByType(Picture pic, Byte type){
        String picUrl = pic.getPictureUrl();
        int pixel;
        // 根据不同的类型信息获取不同的像素比
        if (HOME_PAGE.equals(type)){
            pixel = 1800;
        } else if (ARTICLE_PIC.equals(type)){
            pixel = 700;
        } else if (BLOG_PIC.equals(type)){
            pixel = 2000;
        } else if (ADMIN_HOME_PAGE.equals(type)){
            pixel = 2000;
        } else {
            return;
        }
        String newPicUrl = imgGoTemplate.getThumbImg(picUrl, pixel);
        pic.setPictureUrl(newPicUrl);
        // 如果是题图类型，还需要添加小图
        if (ARTICLE_PIC.equals(type)){
            String smallPicUrl = imgGoTemplate.getThumbImg(picUrl, 100);
            pic.setPictureUrlSmall(smallPicUrl);
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

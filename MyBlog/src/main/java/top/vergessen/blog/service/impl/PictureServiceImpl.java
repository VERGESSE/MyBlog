package top.vergessen.blog.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.vergessen.blog.domain.Picture;
import top.vergessen.blog.mapper.PictureMapper;
import top.vergessen.blog.service.PictureService;

import java.util.List;

/**
 * @author Vergessen
 * @date 2020/7/4 21:44.
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PictureServiceImpl implements PictureService {

    /**
     * 各种 Service
     */
    private final PictureMapper pictureMapper;

    @Override
    public List<Picture> getRandomPic(Byte type, Integer num) {
        return pictureMapper.getRandomPic(type, num);
    }

    @Override
    public Picture getRandomPicOne(Byte type) {
        List<Picture> pic = this.getRandomPic(type, 1);
        return pic.size() > 0 ? pic.get(0): null;
    }
}

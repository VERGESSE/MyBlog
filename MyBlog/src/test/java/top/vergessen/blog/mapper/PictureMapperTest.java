package top.vergessen.blog.mapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import top.vergessen.blog.domain.Picture;
import top.vergessen.blog.service.PictureService;

/**
 * @author Vergessen
 * @date 2020/7/6 11:32.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class PictureMapperTest {

    @Autowired
    private PictureService pictureService;

    @Test
    public void getRandomPic(){
        Picture pic = pictureService.getRandomPicOne((byte)3);
        System.out.println(pic.getPictureUrl());
    }
}

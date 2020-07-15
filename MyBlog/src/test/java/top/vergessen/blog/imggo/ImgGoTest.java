package top.vergessen.blog.imggo;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;
import top.vergessen.blog.util.ImgGoTemplate;

import java.io.*;
import java.util.concurrent.TimeUnit;

/**
 * @author Vergessen
 * @date 2020/7/8 20:04.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ImgGoTest {

    @Autowired
    private ImgGoTemplate imgGoTemplate;

    @Test
    public void thumbTest(){

        String newImg = imgGoTemplate.getThumbImg(
                "https://www.vergessen.top/imgGo/blog/2020/0708/1594206553.png"
                , 350);

        System.out.println(newImg);
    }

    @Test
    public void deleteTest() throws InterruptedException {
        imgGoTemplate.deleteImg(
                "https://www.vergessen.top/imgGo/default/2020/0709/1594265642.png");
        TimeUnit.SECONDS.sleep(3);
    }

    @Test
    public void uploadFileTest() throws IOException {
        String s = imgGoTemplate.uploadImgByUrl("https://www.vergessen.top/image/home.jpg");
        System.out.println(s);
    }

    @Test
    public void strReplaceTest(){
        String replace = StringUtils.replace("I|am|Vergessen|1", "Vergessen", "");
        String[] split = StringUtils.split(replace, "|");
        for (String s : split) {
            System.out.println(s);
        }
    }
}

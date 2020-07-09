package top.vergessen.blog.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 图床工具请求模板
 * @author Vergessen
 * @date 2020/7/8 19:11.
 */
@Component
public class ImgGoTemplate {

    /**
     * 请求图片不存在
     */
    private static final String IMG_NOT_FOUND = "1";
    /**
     * 剪裁失败
     */
    private static final String THUMB_ERR = "3";
    /**
     * 请求像素过大
     */
    private static final String PIXEL_TOO_BIG = "5";
    /**
     * 无权请求
     */
    private static final String ILLEGAL_REQUEST = "Illegal request";

    /**
     * 后端服务器地址
     */
    @Value("${imgGo.serverUrl}")
    private String serverUrl;
    /**
     * 图床权限密码
     */
    @Value("${imgGo.auth}")
    private String auth;

    @Autowired
    private RestTemplate restTemplate;

    /**
     * 异步删除图片线程池，不阻塞主程序
     */
    private ExecutorService deleteExecutor = Executors.newFixedThreadPool(5);

    /**
     * 将请求的图片地址剪裁成需要的尺寸
     * @param imgPath 请求的图片地址
     * @param pixel 像素大小
     * @return 新的图片地址
     */
    public String getThumbImg(String imgPath, Integer pixel)
            throws IllegalArgumentException{
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.add("access", Sha256Util.str2Sha256(auth));

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("img", imgPath);
        map.add("pixel", String.valueOf(pixel));

        HttpEntity<MultiValueMap<String, String>> request =
                new HttpEntity<>(map, headers);
        String newImg = restTemplate.postForObject(
                serverUrl + "/imgGo/thumb", request, String.class);
        validateResp(newImg);
        return newImg;
    }

    /**
     * 根据提供的图片地址删除图片(异步删除)
     * @param imgPath 需要删除的图片地址
     */
    public void deleteImg(String imgPath){
        deleteExecutor.execute(() -> {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            headers.add("access", Sha256Util.str2Sha256(auth));

            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            map.add("imgPath", imgPath);

            HttpEntity<MultiValueMap<String, String>> request =
                    new HttpEntity<>(map, headers);
            restTemplate.postForObject(
                    serverUrl + "/imgGo/delete", request, Void.class);
        });
    }


    /**
     * 根据传入的返回结果校验返回码如果返回结果不正常
     * 则抛出 {@IllegalArgumentException} 异常
     * @param newImg 需要校验的返回值
     */
    private void validateResp(String newImg){
        if (IMG_NOT_FOUND.equals(newImg)){
            throw new IllegalArgumentException("请求图片不存在");
        } else if (THUMB_ERR.equals(newImg)){
            throw new IllegalArgumentException("剪裁失败");
        } else if (PIXEL_TOO_BIG.equals(newImg)){
            throw new IllegalArgumentException("请求像素过大");
        } else if (ILLEGAL_REQUEST.equals(newImg)){
            throw new IllegalArgumentException("无权请求");
        }
    }
}

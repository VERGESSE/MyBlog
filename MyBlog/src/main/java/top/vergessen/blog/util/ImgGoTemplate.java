package top.vergessen.blog.util;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Objects;
import java.util.UUID;
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
     * 后端图床服务器地址
     */
    @Value("${imgGo.serverUrl}")
    private String serverUrl;
    /**
     * 图床权限密码
     */
    @Value("${imgGo.auth}")
    private String auth;
    /**
     * 图床存储组别
     */
    @Value("${imgGo.group}")
    private String imgGroup;

    @Autowired
    private RestTemplate restTemplate;

    /**
     * 异步删除图片线程池，不阻塞主程序
     */
    private ExecutorService deleteExecutor = Executors.newFixedThreadPool(5);

    /**
     * 向图床请求上传图片
     * @param file 图片文件
     * @return 上传成功后的图片地址
     */
    public String uploadImgByFile(MultipartFile file) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.add("access", Sha256Util.str2Sha256(auth));
        // 创建临时文件
        File tempFile = null;
        String imgUrl = "";
        try(InputStream is = file.getInputStream()) {
            tempFile = new File(String.valueOf(UUID.randomUUID()));
            FileOutputStream os = new FileOutputStream(tempFile);
            IOUtils.copy(is, os);
            os.close();
            MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
            map.add("file", new FileSystemResource(tempFile));
            map.add("group", imgGroup);

            HttpEntity<MultiValueMap<String, Object>> request =
                    new HttpEntity<>(map, headers);
            imgUrl = restTemplate.postForObject(
                    serverUrl + "/imgGo/upload", request, String.class);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            tempFile.delete();
        }
        return serverUrl + imgUrl;
    }

    /**
     * 使用一个外部图片地址上传博客图床图片
     * @param imgUrl 外链的图片地址
     * @return 图片的访问网址
     */
    public String uploadImgByUrl(String imgUrl){
        byte[] imgData = restTemplate.getForObject(imgUrl, byte[].class);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(
                Objects.requireNonNull(imgData));
        try {
            MultipartFile file = new MockMultipartFile("file", inputStream);
            return this.uploadImgByFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

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
        return serverUrl + newImg;
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

    public String getServerUrl() {
        return serverUrl;
    }

    public String getImgGroup() {
        return imgGroup;
    }

    public String getBaseImgPath(){
        return serverUrl + "/imgGo/" + imgGroup;
    }
}

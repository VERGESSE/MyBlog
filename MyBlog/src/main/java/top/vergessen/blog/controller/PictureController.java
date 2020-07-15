package top.vergessen.blog.controller;

import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.vergessen.blog.auth.CheckLogin;
import top.vergessen.blog.service.PictureService;
import top.vergessen.blog.util.ImgGoTemplate;

/**
 * 图库操作接口
 * @author Vergessen
 * @date 2020/7/11 15:17.
 */
@RestController
@RequestMapping("picture")
@CrossOrigin
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PictureController {

    private final PictureService pictureService;
    private final ImgGoTemplate imgGoTemplate;

    /**
     * 根据图片类型分页获取图库信息（时间倒序排序）
     * @param page 页码
     * @param size 每页条数
     * @param type 图片类型
     * @return 分页信息
     */
    @GetMapping("picture")
    @CheckLogin
    public ResponseEntity<PageInfo> getPicturePageByType(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "0") Byte type){
        return ResponseEntity.ok(
                pictureService.getPicPageByType(page, size, type));
    }

    /**
     * 通过上传文件新增图片的接口
     * @param file 上传的文件
     * @param type 保存到图库的类型
     */
    @PostMapping("file")
    @CheckLogin
    public ResponseEntity<Void> uploadPicByFile(
            @RequestParam MultipartFile file,
            @RequestParam Byte type){
        // 上传图片到图床
        String imgUrl = imgGoTemplate.uploadImgByFile(file);
        pictureService.uploadPic(imgUrl, type);
        return ResponseEntity.ok(null);
    }

    /**
     * 通过上传图片链接新增图片的接口
     * @param url 上传的文件
     * @param type 保存到图库的类型
     */
    @PostMapping("url")
    @CheckLogin
    public ResponseEntity<Void> uploadPicByUrl(
            @RequestParam String url,
            @RequestParam Byte type){
        // 判断传入的Url是否位于图床，如果位于图床则直接添加，不位于则先上传图床再添加
        if (!url.contains(imgGoTemplate.getBaseImgPath())){
            // 上传图片到图床
            url = imgGoTemplate.uploadImgByUrl(url);
        }
        // 添加图片
        pictureService.uploadPic(url, type);
        return ResponseEntity.ok(null);
    }

    /**
     * 根据前端提供的图片Id删除图片
     * @param id 图片Id
     */
    @DeleteMapping("picture")
    @CheckLogin
    public ResponseEntity<Void> deletePicById(@RequestParam Long id){
        pictureService.deletePicById(id);
        return ResponseEntity.ok(null);
    }
}

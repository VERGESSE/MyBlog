package top.vergessen.blog.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import top.vergessen.blog.auth.CheckLogin;
import top.vergessen.blog.service.ResourceService;

/**
 * 数据库资源表统一访问接口
 * @author Vergessen
 * @date 2020/7/10 14:24.
 */
@RestController
@RequestMapping("resource")
@CrossOrigin
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ResourceController {

    private final ResourceService resourceService;

    /**
     * 根据前端提供的key获取相应的资源名称
     */
    @GetMapping("{key}")
    @CheckLogin
    public ResponseEntity<String> getResourceByKey(
            @PathVariable("key") String key){
        return ResponseEntity.ok(resourceService.getRes(key));
    }

    /**
     * 根据前端提供的key和value更新相应的资源信息
     */
    @PutMapping("{key}")
    @CheckLogin
    public ResponseEntity<Void> updateResourceByKey(
            @PathVariable("key") String key,
            @RequestParam String value){
        resourceService.putRes(key,value);
        return ResponseEntity.ok(null);
    }

}

package top.vergessen.blog.controller;

import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import top.vergessen.blog.auth.CheckLogin;
import top.vergessen.blog.domain.Picture;
import top.vergessen.blog.domain.Resource;
import top.vergessen.blog.domain.vo.UserInfoVO;
import top.vergessen.blog.exception.BlogException;
import top.vergessen.blog.exception.ExceptionEnum;
import top.vergessen.blog.service.PictureService;
import top.vergessen.blog.service.ResourceService;
import top.vergessen.blog.util.JwtOperator;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * {@CrossOrigin} 允许跨域访问
 * @author Vergessen
 * @date 2020/7/5 16:05.
 */
@RestController
@RequestMapping("admin")
@CrossOrigin
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserController {

    /**
     * 各种 Service
     */
    private final ResourceService resourceService;
    private final PictureService pictureService;

    /**
     * jwt验证工具类
     */
    private final JwtOperator jwtOperator;

    /**
     * 管理员登录逻辑
     * @param username 管理员账号
     * @param password 管理员密码
     * @return 返回登录成功的token值
     */
    @PostMapping("login")
    public ResponseEntity<String> login(
        @RequestParam String username,
        @RequestParam String password
    ){
        // 验证用户名密码
        if (Objects.equals(username, resourceService.getRes("用户名"))
            && Objects.equals(password, resourceService.getRes("密码"))){
            HashMap<String, Object> userInfo = Maps.newHashMap();
            userInfo.put("username", username);
            userInfo.put("auth","root");
            String token = jwtOperator.generateToken(userInfo);
            return ResponseEntity.ok(token);
        }
        throw new BlogException(ExceptionEnum.USERNAME_OR_PASSWORD_FAIL);
    }

    /**
     * 获取管理员信息,验证token
     * @return {@UserInfoVO}
     */
    @GetMapping("user/info")
    @CheckLogin
    public ResponseEntity<UserInfoVO> getUserInfo(){
        return ResponseEntity.ok(UserInfoVO.builder()
                .name(resourceService.getRes("用户名"))
                .avatar(resourceService.getRes("头像"))
                .introduction(resourceService.getRes("introduction"))
                .roles(resourceService.getRes("roles"))
                .build());
    }

    /**
     * 获取全部的博客资源信息
     */
    @GetMapping("res")
    @CheckLogin
    public ResponseEntity<List<Resource>> getAllRes(){
        return ResponseEntity.ok(resourceService.getAllRes());
    }

    /**
     * 更新资源信息（不允许添加）
     * @param resources 资源信息
     */
    @PostMapping("res")
    @CheckLogin
    public ResponseEntity<Void> putRes(@RequestBody List<Resource> resources){
        for (Resource resource : resources) {
            resourceService.putRes(resource.getResKey(),resource.getValue());
        }
        return ResponseEntity.ok(null);
    }

    /**
     * 随机获取一张后台首页图片
     * @return  后台首页图片地址
     */
    @GetMapping("pic")
    public ResponseEntity<Picture> getPic(){
        return ResponseEntity.ok(
                pictureService.getRandomPicOne(Picture.ADMIN_HOME_PAGE));
    }
}

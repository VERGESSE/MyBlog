package top.vergessen.blog.controller;

import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import top.vergessen.blog.auth.CheckLogin;
import top.vergessen.blog.domain.Picture;
import top.vergessen.blog.domain.vo.UserInfoVO;
import top.vergessen.blog.exception.BlogException;
import top.vergessen.blog.exception.ExceptionEnum;
import top.vergessen.blog.service.PictureService;
import top.vergessen.blog.service.ResourceService;
import top.vergessen.blog.util.JwtOperator;

import java.util.HashMap;
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
        if (Objects.equals(username, resourceService.getRes("username"))
            && Objects.equals(password, resourceService.getRes("password"))){
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
                .name(resourceService.getRes("username"))
                .avatar(resourceService.getRes("avatar"))
                .introduction(resourceService.getRes("introduction"))
                .roles(resourceService.getRes("roles"))
                .build());
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

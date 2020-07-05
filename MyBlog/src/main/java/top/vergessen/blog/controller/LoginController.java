package top.vergessen.blog.controller;

import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.vergessen.blog.exception.BlogException;
import top.vergessen.blog.exception.ExceptionEnum;
import top.vergessen.blog.service.ResourceService;
import top.vergessen.blog.util.JwtOperator;

import java.util.HashMap;
import java.util.Objects;

/**
 * @author Vergessen
 * @date 2020/7/5 16:05.
 */
@RestController
@RequestMapping("admin")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LoginController {

    private final ResourceService resourceService;
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
}

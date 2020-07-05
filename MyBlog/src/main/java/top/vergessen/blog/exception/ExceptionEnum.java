package top.vergessen.blog.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author Vergessen
 * @date 2020/7/5 11:22.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum ExceptionEnum {

    // Token校验不合法
    TOKEN_INVALID(500, "Token校验不合法"),
    // 用户名密码不正确
    USERNAME_OR_PASSWORD_FAIL(500,"用户名或密码不正确")
    ;

    private int status;
    private String message;

}

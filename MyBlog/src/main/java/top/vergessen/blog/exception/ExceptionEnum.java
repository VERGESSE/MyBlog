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
    USERNAME_OR_PASSWORD_FAIL(500,"用户名或密码不正确"),
    // 留言修改失败
    MESSAGE_UPDATE_ERROR(500, "留言修改失败"),
    // 博文分类新增失败
    INSERT_CATEGORY_ERROR(500, "博文分类新增失败"),
    // 博文分类不存在
    UPDATE_CATEGORY_ERROR(500, "博文分类不存在"),
    // 技术栈新增失败
    INSERT_TECHNOLOGY_ERROR(500, "技术栈新增失败"),
    // 技术栈信息不存在
    UPDATE_TECHNOLOGY_ERROR(500, "技术栈信息不存在"),
    // 访问频次超标
    FREQUENCY_OUT_OF_LIMITS(403, "暂时无法访问")
    ;

    private int status;
    private String message;

}

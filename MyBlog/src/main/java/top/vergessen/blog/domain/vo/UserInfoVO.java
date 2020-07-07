package top.vergessen.blog.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 传输给前端的管理员信息
 * @author Vergessen
 * @date 2020/7/6 10:29.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserInfoVO {

    /**
     * 权限，无需修改
     */
    private String roles;

    /**
     * 身份介绍
     */
    private String introduction;

    /**
     * 头像地址
     */
    private String avatar;

    /**
     * 管理员名称
     */
    private String name;
}

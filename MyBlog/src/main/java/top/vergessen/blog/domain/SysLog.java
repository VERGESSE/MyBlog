package top.vergessen.blog.domain;

import javax.persistence.*;

import lombok.*;

import java.time.LocalDateTime;

/**
 * 日志表
 * @author Vergessen
 * @date 2020/7/4 21:20.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "sys_log")
public class SysLog {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(generator = "JDBC")
    private Long id;

    /**
     * 操作地址的IP
     */
    private String ip;

    /**
     * 操作者地址
     */
    private String addr;

    /**
     * 操作者描述
     */
    private String detail;

    /**
     * 操作时间
     */
    @Column(name = "create_by")
    private LocalDateTime createBy;

    /**
     * 操作内容
     */
    private String remark;

    /**
     * 操作的访问地址
     */
    @Column(name = "operate_url")
    private String operateUrl;

    /**
     * 操作的浏览器
     */
    @Column(name = "operate_by")
    private String operateBy;
}

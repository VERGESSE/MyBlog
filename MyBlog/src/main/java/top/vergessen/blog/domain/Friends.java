package top.vergessen.blog.domain;

import javax.persistence.*;

import lombok.*;

import java.time.LocalDateTime;

/**
 * 友链表
 * @author Vergessen
 * @date 2020/7/4 21:20.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "tbl_friends")
public class Friends {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(generator = "JDBC")
    private Long id;

    /**
     * 友链地址
     */
    private String url;

    /**
     * 友链描述
     */
    private String detail;

    /**
     * 友链名称
     */
    private String name;

    /**
     * 友链头像
     */
    private String photo;

    /**
     * 友链修改时间
     */
    @Column(name = "modified_by")
    private LocalDateTime modifiedBy;

    /**
     * 友链创建时间
     */
    @Column(name = "create_by")
    private LocalDateTime createBy;
}

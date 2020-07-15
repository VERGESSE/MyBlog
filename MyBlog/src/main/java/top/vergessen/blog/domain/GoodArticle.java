package top.vergessen.blog.domain;

import javax.persistence.*;

import lombok.*;

import java.time.LocalDateTime;

/**
 * 优质博文分享表表
 * @author Vergessen
 * @date 2020/7/5 15:42.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "tbl_good_article")
public class GoodArticle {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(generator = "JDBC")
    private Long id;

    /**
     * 文章标题
     */
    private String title;

    /**
     * 文章地址
     */
    private String url;

    /**
     * 审核状态，通过为1，默认为0
     */
    private Byte state;

    /**
     * 申请人邮箱
     */
    private String email;

    /**
     * 文章链接修改时间
     */
    @Column(name = "modified_by")
    private LocalDateTime modifiedBy;

    /**
     * 文章链接创建时间
     */
    @Column(name = "create_by")
    private LocalDateTime createBy;
}

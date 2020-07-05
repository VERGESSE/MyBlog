package top.vergessen.blog.domain;

import java.time.LocalDateTime;
import javax.persistence.*;

import lombok.*;

/**
 * 博文评论表
 * @author Vergessen
 * @date 2020/7/4 21:20.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "tbl_article_comment")
public class ArticleComment {
    /**
     * 评论ID
     */
    @Id
    @GeneratedValue(generator = "JDBC")
    private Long id;

    /**
     * 文章ID
     */
    @Column(name = "article_id")
    private Long articleId;

    /**
     * 评论的父ID，0代表无父评论
     */
    @Column(name = "parent_comment_id")
    private Long parentCommentId;

    /**
     * 用户自己定义的名称
     */
    private String name;

    /**
     * 邮箱，用于回复消息
     */
    private String email;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 评论者IP
     */
    private String ip;

    /**
     * 是否有效，默认为1为有效，0为无效
     */
    @Column(name = "is_effective")
    private Byte isEffective;

    /**
     * 评论创建日期
     */
    @Column(name = "create_by")
    private LocalDateTime createBy;
}

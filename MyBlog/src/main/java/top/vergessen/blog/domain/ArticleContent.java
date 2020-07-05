package top.vergessen.blog.domain;

import javax.persistence.*;

import lombok.*;

import java.time.LocalDateTime;

/**
 * 博文主体信息存储表
 * @author Vergessen
 * @date 2020/7/4 21:20.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "tbl_article_content")
public class ArticleContent {
    /**
     * 对应文章ID
     */
    @Id
    @Column(name = "article_id")
    private Long articleId;

    /**
     * 更新时间
     */
    @Column(name = "modifield_by")
    private LocalDateTime modifieldBy;

    /**
     * 创建时间
     */
    @Column(name = "create_by")
    private LocalDateTime createBy;

    /**
     * 博文主体
     */
    private String content;
}

package top.vergessen.blog.domain;

import java.time.LocalDateTime;
import javax.persistence.*;

import lombok.*;

/**
 * 博文分类与文章对照中间表
 * @author Vergessen
 * @date 2020/7/4 21:20.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "tbl_article_category")
public class ArticleCategory {
    /**
     * 分类id
     */
    @Id
    @Column(name = "category_id")
    private Long categoryId;

    /**
     * 文章id
     */
    @Id
    @Column(name = "article_id")
    private Long articleId;

    /**
     * 更新时间
     */
    @Column(name = "modified_by")
    private LocalDateTime modifiedBy;

    /**
     * 创建时间
     */
    @Column(name = "create_by")
    private LocalDateTime createBy;
}

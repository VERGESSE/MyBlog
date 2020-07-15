package top.vergessen.blog.domain;

import javax.persistence.*;

import lombok.*;

import java.time.LocalDateTime;

/**
 * 博文信息表
 * @author Vergessen
 * @date 2020/7/4 21:20.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "tbl_article_info")
public class ArticleInfo {
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
     * 文章简介，默认100个汉字以内
     */
    private String summary;

    /**
     * 文章标签，分类的集合字符串
     */
    private String tags;

    /**
     * 文章是否置顶，0为否，1为是
     */
    @Column(name = "is_top")
    private Byte isTop;

    /**
     * 文章访问量
     */
    private Integer traffic;

    /**
     * 修改日期
     */
    @Column(name = "modified_by")
    private LocalDateTime modifiedBy;

    /**
     * 创建时间
     */
    @Column(name = "create_by")
    private LocalDateTime createBy;
}

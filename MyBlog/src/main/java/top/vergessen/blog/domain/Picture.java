package top.vergessen.blog.domain;

import javax.persistence.*;

import lombok.*;

import java.time.LocalDateTime;

/**
 * 图库
 * @author Vergessen
 * @date 2020/7/4 21:20.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "tbl_picture")
public class Picture {

    /**
     * 定义图库对应类型的常量
     */
    public transient static final Byte HOME_PAGE = 0;
    public transient static final Byte ARTICLE_PIC = 1;
    public transient static final Byte BLOG_PIC = 2;
    public transient static final Byte ADMIN_HOME_PAGE = 3;

    /**
     * 图片id
     */
    @Id
    @GeneratedValue(generator = "JDBC")
    private Long id;

    /**
     * 图片url
     */
    @Column(name = "picture_url")
    private String pictureUrl;

    /**
     * 小图地址
     */
    @Column(name = "picture_url_small")
    private String pictureUrlSmall;

    /**
     * 图片类型: 0 首页图片，1 文章题图， 3 博客页图片
     */
    @Column(name = "picture_type")
    private Byte pictureType;

    /**
     * 创建时间
     */
    @Column(name = "create_by")
    private LocalDateTime createBy;
}

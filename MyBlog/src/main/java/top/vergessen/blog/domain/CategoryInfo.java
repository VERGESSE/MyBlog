package top.vergessen.blog.domain;

import javax.persistence.*;

import lombok.*;

import java.time.LocalDateTime;

/**
 * 分类信息表
 * @author Vergessen
 * @date 2020/7/4 21:20.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "tbl_category_info")
public class CategoryInfo {
    /**
     * 分类ID
     */
    @Id
    @GeneratedValue(generator = "JDBC")
    private Long id;

    /**
     * 分类名称
     */
    private String name;

    /**
     * 该分类下的文章数量
     */
    private Integer number;

    /**
     * 分类图片地址
     */
    @Column(name = "picture_url")
    private String pictureUrl;

    /**
     * 是否有效，默认为1有效，为0无效
     */
    @Column(name = "is_effective")
    private Byte isEffective;

    /**
     * 分类修改时间
     */
    @Column(name = "modified_by")
    private LocalDateTime modifiedBy;

    /**
     * 分类创建时间
     */
    @Column(name = "create_by")
    private LocalDateTime createBy;
}

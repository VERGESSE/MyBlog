package top.vergessen.blog.domain;

import java.time.LocalDateTime;
import javax.persistence.*;

import lombok.*;

/**
 * 技术栈展示
 * @author Vergessen
 * @date 2020/7/8 21:59.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "tbl_technology")
public class Technology {
    /**
     * 分类ID
     */
    @Id
    @GeneratedValue(generator = "JDBC")
    private Long id;

    /**
     * 技术名
     */
    private String name;

    /**
     * 图标地址
     */
    @Column(name = "picture_url")
    private String pictureUrl;

    /**
     * 修改时间
     */
    @Column(name = "modified_by")
    private LocalDateTime modifiedBy;

    /**
     * 创建时间
     */
    @Column(name = "create_by")
    private LocalDateTime createBy;
}

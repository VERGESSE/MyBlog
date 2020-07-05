package top.vergessen.blog.domain;

import javax.persistence.*;

import lombok.*;

import java.time.LocalDateTime;

/**
 * 资源映射表
 * @author Vergessen
 * @date 2020/7/5 10:47.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "tbl_resource")
public class Resource {
    /**
     * 资源名
     */
    @Id
    private String resKey;

    /**
     * 资源值
     */
    private String value;

    /**
     * 资源修改时间
     */
    @Column(name = "modified_by")
    private LocalDateTime modifiedBy;

    /**
     * 资源创建时间
     */
    @Column(name = "create_by")
    private LocalDateTime createBy;
}

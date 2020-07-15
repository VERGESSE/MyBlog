package top.vergessen.blog.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 带题图信息无主体内容的文章信息实体类
 * @author Vergessen
 * @date 2020/7/12 14:52.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ArticleInfoVO {

    /**
     * 文章id
     */
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
     * 图片地址
     */
    private String pictureUrl;

    /**
     * 小图地址
     */
    private String pictureUrlSmall;

    /**
     * 文章标签，分类的集合字符串
     */
    private List<String> tags;

    /**
     * 文章访问量
     */
    private Integer traffic;

    /**
     * 文章是否置顶，0为否，1为是
     */
    private Byte isTop;

    /**
     * 创建时间
     */
    private LocalDateTime createBy;
}

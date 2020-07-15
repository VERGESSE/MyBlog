package top.vergessen.blog.domain;

import javax.persistence.*;

import lombok.*;

import java.time.LocalDateTime;

/**
 * 博客留言表
 * @author Vergessen
 * @date 2020/7/4 21:20.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "tbl_message")
public class Message {
    /**
     * 留言id
     */
    @Id
    @GeneratedValue(generator = "JDBC")
    private Long id;

    /**
     * 留言者昵称
     */
    private String name;

    /**
     * 留言者邮箱
     */
    private String email;

    /**
     * 留言详细信息
     */
    private String message;

    /**
     * 是否展示，若展示则为1
     */
    @Column(name = "is_show")
    private Byte isShow;

    /**
     * 评论者IP
     */
    private String ip;

    /**
     * 留言时间
     */
    @Column(name = "create_time")
    private LocalDateTime createTime;
}

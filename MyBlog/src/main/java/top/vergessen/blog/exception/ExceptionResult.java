package top.vergessen.blog.exception;

import lombok.Data;

/**
 * 异常返回值信息
 * @author Vergessen
 * @date 2020/7/5 11:22.
 */
@Data
public class ExceptionResult {

    private int status;
    private String message;
    private Long timestamp;

    public ExceptionResult(ExceptionEnum em) {
        this.status = em.getStatus();
        this.message = em.getMessage();
        this.timestamp = System.currentTimeMillis();
    }
}

package top.vergessen.blog.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Vergessen
 * @date 2020/7/5 11:22.
 */
@Getter
@AllArgsConstructor
public class BlogException extends RuntimeException {

    private ExceptionEnum exceptionEnum;
}

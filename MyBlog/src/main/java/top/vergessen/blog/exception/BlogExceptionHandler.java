package top.vergessen.blog.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * {@ExceptionHandler} 捕获在程序中抛出的 {@BlogException}
 * @author Vergessen
 * @date 2020/7/5 11:22.
 */
@ControllerAdvice
public class BlogExceptionHandler {

    @ExceptionHandler(BlogException.class)
    public ResponseEntity<ExceptionResult> handlerException(BlogException e){
        return ResponseEntity.status(e.getExceptionEnum().getStatus())
                .body(new ExceptionResult(e.getExceptionEnum()));
    }
}

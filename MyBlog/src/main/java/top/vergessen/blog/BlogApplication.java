package top.vergessen.blog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author Vergessen
 * @date 2020/7/4 19:08.
 */
@SpringBootApplication
@Configuration
@MapperScan("top.vergessen.blog.mapper")
public class BlogApplication {

    public static void main(String[] args) {

        SpringApplication.run(BlogApplication.class, args);
    }
}

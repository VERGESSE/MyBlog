package top.vergessen.blog.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.vergessen.blog.service.ArticleService;

/**
 * @author Vergessen
 * @date 2020/7/6 23:04.
 */
@RestController
@RequestMapping("article")
@CrossOrigin
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ArticleController {

    private final ArticleService articleService;

    @GetMapping("articleNum")
    public ResponseEntity<Integer> getArticleNum(){
        return ResponseEntity.ok(articleService.selectArticleCount());
    }
}

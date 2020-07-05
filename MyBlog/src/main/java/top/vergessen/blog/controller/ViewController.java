package top.vergessen.blog.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import top.vergessen.blog.service.*;

/**
 * @author Vergessen
 * @date 2020/7/5 15:44.
 */
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ViewController {

    private final ArticleService articleService;
    private final CategoryService categoryService;
    private final FriendsService friendsService;
    private final MessageService messageService;
    private final PictureService pictureService;


}

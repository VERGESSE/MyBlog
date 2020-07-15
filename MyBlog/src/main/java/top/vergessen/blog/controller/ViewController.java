package top.vergessen.blog.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import top.vergessen.blog.domain.*;
import top.vergessen.blog.domain.vo.ArticleInfoVO;
import top.vergessen.blog.domain.vo.ArticleVO;
import top.vergessen.blog.service.*;

import java.util.List;

/**
 * 前端视图控制器
 * @author Vergessen
 * @date 2020/7/5 15:44.
 */
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ViewController {

    /**
     * 每页博客显示条数
     */
    private static final Integer ONE_PAGE_NUM = 6;

    private final ArticleService articleService;
    private final CategoryTechnologyService categoryService;
    private final FriendsService friendsService;
    private final MessageService messageService;
    private final PictureService pictureService;
    private final ResourceService resourceService;
    private final GoodArticleService goodArticleService;

    /**
     * 首页访问控制器
     */
    @GetMapping("/")
    public ModelAndView home(){
        ModelAndView model = new ModelAndView("index");
        // 获取个人信息 分类信息
        this.addInfos(model);
        // 获取 关于页 展示个人介绍
        String personalInformation = resourceService.getRes("自我介绍");
        model.addObject("personalInformation", personalInformation);
        // 获取 关于我处 个人介绍
        String aboutMe = resourceService.getRes("关于我");
        model.addObject("aboutMe", aboutMe);
        // 获取首页左侧介绍
        String leftInfo = resourceService.getRes("首页左侧介绍");
        model.addObject("leftInfo", leftInfo);
        // 获取友链宣言
        String friendInfo = resourceService.getRes("友链宣言");
        model.addObject("friendInfo", friendInfo);
        // 获取我的标签
        String myTags = resourceService.getRes("我的标签");
        model.addObject("myTags", myTags);
        // 获取随机主页图片
        Picture picOne = pictureService.getRandomPicOne(Picture.HOME_PAGE);
        model.addObject("homeImg", picOne.getPictureUrl());
        // 获取技术栈信息
        List<Technology> technologies = categoryService.selectAllTechnology();
        model.addObject("technologies", technologies);
        // 获取带置顶的最新博文6条
        List<ArticleInfoVO> articles =  articleService.getTopArticle(1, 6);
        model.addObject("articles", articles);
        // 获取友链信息
        List<Friend> friends = friendsService.selectAllFriends();
        model.addObject("friends", friends);
        // 获取外链信息
        List<GoodArticle> goodLinkList = goodArticleService.selectAllLinks(1, 15).getList();
        model.addObject("goodLinkList", goodLinkList);
        // 获取展示中的留言信息
        List<Message> messages = messageService.selectPageMessageIsshow(1, 8).getList();
        model.addObject("messages", messages);
        return model;
    }

    /**
     * 分类展示博文页
     * @param name 分类名称
     * @param page 页码
     */
    @GetMapping({"blog/all","blog/all/{page}","blog/{name}",  "blog/{name}/{page}"})
    public ModelAndView blog(
            @PathVariable(name = "name", required = false)String name,
            @PathVariable(name = "page", required = false)Integer page){
        name  = name == null ? "" : name;
        page = (page == null || page < 1) ? 1 : page;
        ModelAndView model = new ModelAndView("blog");
        // 获取共用信息
        this.addCategorySearchInfos(model);
        model.addObject("category", "".equals(name) ? "全部" : name);
        // 获取文章信息
        List<ArticleInfoVO> articleList = articleService.getArticleList(
                page, ONE_PAGE_NUM, "", name);
        model.addObject("articles", articleList);
        // 添加分页信息
        String[] pageInfo = this.generatePageInfo(name, page, articleList.size());
        model.addObject("Previous", pageInfo[0]);
        model.addObject("Next",pageInfo[1]);
        return model;
    }

    /**
     * 根据博文Id渲染页面，展示博文信息
     * @param articleId 博文Id
     */
    @GetMapping("article/v/{articleId}")
    public ModelAndView article(
            @PathVariable(name = "articleId")Long articleId){
        ModelAndView model = new ModelAndView("article");
        // 文章浏览量加一
        articleService.increaseViewNum(articleId);
        // 获取共用信息
        this.addCategorySearchInfos(model);
        // 获取博文信息
        ArticleVO article = articleService.getArticleById(articleId);
        model.addObject("article", article);
        List<ArticleInfoVO> articleList = articleService.getTopArticle(1, 10);
        model.addObject("articleList", articleList);
        // 获取评论信息
        List<ArticleComment> comments = articleService.getCommentByArticleId(articleId);
        model.addObject("comments", comments);
        return model;
    }

    @GetMapping({"search/{search}","search/{search}/{page}"})
    public ModelAndView search(
            @PathVariable(name = "search", required = true)String search,
            @PathVariable(name = "page", required = false)Integer page){
        page = (page == null || page < 1) ? 1 : page;
        ModelAndView model = new ModelAndView("blog");
        this.addCategorySearchInfos(model);
        model.addObject("category", search);
        // 获取文章信息
        List<ArticleInfoVO> articleList = articleService.getArticleList(
                page, ONE_PAGE_NUM, search, "");
        model.addObject("articles", articleList);
        // 添加分页信息
        String[] pageInfo = this.generatePageInfo(search, page, articleList.size());
        model.addObject("Previous", pageInfo[0]);
        model.addObject("Next",pageInfo[1]);
        return model;
    }

    /**
     * 根据提供得信息生成页码信息传给前端
     */
    private String[] generatePageInfo(String name, Integer page, Integer size){
        String[] pageInfo = new String[2];
        name = "".equals(name) ? "all" : name;
        int previousPage = page == 1 ? page :    page - 1;
        int nextPage = size < ONE_PAGE_NUM ? page : page + 1;
        pageInfo[0] = "/blog/" + name + "/" + previousPage;
        pageInfo[1] = "/blog/" + name + "/" + nextPage;
        return pageInfo;
    }

    /**
     * 为 blog、search 接口服务的信息填充方法
     */
    private void addCategorySearchInfos(ModelAndView model){
        // 获取随机头部图片
        Picture picOne = pictureService.getRandomPicOne(Picture.BLOG_PIC);
        model.addObject("blogImg", picOne.getPictureUrl());
        this.addInfos(model);
    }

    /**
     * 接口共用信息 (个人信息，分类信息等)
     */
    private void addInfos(ModelAndView model){
        // 获取个人信息
        String username = resourceService.getRes("用户名");
        String logo = resourceService.getRes("logo");
        model.addObject("username", username);
        model.addObject("logo", logo);
        // 获取全部文章数量不为0的分类信息并按文章数量排序
        List<CategoryInfo> categorys = categoryService.selectAllCategoryNumNotZero();
        model.addObject("categorys", categorys);
        // 获取备案号相关信息
        String records = resourceService.getRes("备案号信息");
        model.addObject("records", records);
    }
}

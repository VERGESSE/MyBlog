package top.vergessen.blog.controller;

import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import top.vergessen.blog.auth.CheckLogin;
import top.vergessen.blog.domain.ArticleComment;
import top.vergessen.blog.domain.ArticleInfo;
import top.vergessen.blog.domain.GoodArticle;
import top.vergessen.blog.domain.vo.ArticleVO;
import top.vergessen.blog.service.ArticleService;
import top.vergessen.blog.service.GoodArticleService;
import top.vergessen.blog.util.MailTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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
    private final GoodArticleService goodArticleService;
    private final MailTemplate mailTemplate;

    /**
     * 创建新的博文
     * @param articleVO 新博文信息
     */
    @PostMapping("article")
    @CheckLogin
    public ResponseEntity<Void> createArticle(@RequestBody ArticleVO articleVO){
        articleService.createArticle(articleVO);
        return ResponseEntity.ok(null);
    }

    /**
     * 分页获取博文
     * @param page 页码
     * @param size 每页条数
     * @param search 搜索条件
     * @param category 分类
     * @return 搜索获取的结果集
     */
    @GetMapping("article")
    public ResponseEntity<PageInfo<ArticleInfo>> getArticleByCategoryAndSearchAndPage(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "") String search,
            @RequestParam(defaultValue = "") String category
    ){
        return ResponseEntity.ok(articleService.getArticleByCategoryAndSearchAndPage(
           page, size, search, category, true
        ));
    }

    /**
     * 根据文章ID获取文章信息
     * @param id 文章ID
     * @return ID对应的文章主体信息
     */
    @GetMapping("{id}")
    public ResponseEntity<ArticleVO> getArticleById(
            @PathVariable("id") Long id){
        return ResponseEntity.ok(articleService.getArticleById(id, false));
    }

    /**
     * 根据文章ID更新文章信息
     * @param id 文章ID
     * @param articleVO 更新的文章信息
     */
    @PutMapping("{id}")
    @CheckLogin
    public ResponseEntity<Void> updateArticleById(
            @PathVariable("id") Long id,
            @RequestBody ArticleVO articleVO){
        articleService.updateArticleById(id, articleVO);
        return ResponseEntity.ok(null);
    }

    /**
     * 根据文章ID删除文章
     * @param id 要删除的文章Id
     */
    @DeleteMapping("{id}")
    @CheckLogin
    public ResponseEntity<Void> deleteArticle(
            @PathVariable("id") Long id){
        articleService.deleteArticleById(id);
        return ResponseEntity.ok(null);
    }

    /**
     * 获取文章总数
     */
    @GetMapping("num")
    public ResponseEntity<Integer> getArticleNum(){
        return ResponseEntity.ok(articleService.selectArticleCount());
    }

    //===================== 收藏外链服务 ========================

    /**
     * 获取全部审核成功的外链列表，时间倒序排列
     * @param page 页码
     * @param size 每页条数
     * @return 分页信息
     */
    @GetMapping("link")
    public ResponseEntity<PageInfo<GoodArticle>> getAllLinks(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "5") Integer size){
        return ResponseEntity.ok(goodArticleService.selectAllLinks(page, size));
    }

    /**
     * 获取全部未审核成功的外链列表，时间倒序排列
     * @param page 页码
     * @param size 每页条数
     * @return 分页信息
     */
    @GetMapping("link-n")
    @CheckLogin
    public ResponseEntity<PageInfo<GoodArticle>> getAllLinksNotCheck(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "5") Integer size){
        return ResponseEntity.ok(
                goodArticleService.selectAllLinksNotCheck(page,size));
    }

    /**
     * 后台主动添加外链，不需要审核
     * @param goodArticle 友链信息
     */
    @PostMapping("link-n")
    @CheckLogin
    public ResponseEntity<Void> addFriendNotCheck(
            @RequestBody GoodArticle goodArticle){
        goodArticle.setState((byte) 1);
        goodArticleService.addNewLink(goodArticle);
        return ResponseEntity.ok(null);
    }

    /**
     * 根据前端提供信息更新外链信息
     * @param goodArticle 新的外链信息
     */
    @PutMapping("link")
    @CheckLogin
    public ResponseEntity<Void> updateLink(
            @RequestBody GoodArticle goodArticle ){
        goodArticleService.updateLink(goodArticle);
        // 如果前端提供了外链状态信息，则为审核通过外链需给申请人发送邮件
        if (goodArticle.getState() != null && goodArticle.getState() == 1){
            // 给外链请求人邮件提醒
            mailTemplate.sendTxtMail(
                    "外链审核通知",
                    "您申请的外链: " + goodArticle.getTitle()
                            + " \n地址: " + goodArticle.getUrl()
                            + " \n已经通过审核,已在本博客展示。感谢贡献",
                    goodArticle.getEmail());
        }
        return ResponseEntity.ok(null);
    }

    /**
     * 根据友链ID删除外链信息
     * @param id 外链ID
     */
    @DeleteMapping("link")
    @CheckLogin
    public ResponseEntity<Void> deleteLink(
            @RequestParam Long id ){
        goodArticleService.deleteLinkById(id);
        return ResponseEntity.ok(null);
    }

    /**
     * 前台请求动添加外链，需要审核
     * @param goodArticle 友链信息
     */
    @PostMapping("link")
    public ResponseEntity<Void> addLinkCheck(
            @RequestBody GoodArticle goodArticle){
        goodArticle.setState((byte) 0);
        goodArticleService.addNewLink(goodArticle);
        // 给自己邮件提醒
        mailTemplate.sendTxtMail(
                "收到新的外链申请",
                "外链标题: " + goodArticle.getTitle()
                        + " \n地址: " + goodArticle.getUrl(),
                mailTemplate.getRootMail());
        return ResponseEntity.ok(null);
    }

    /**
     * 添加评论信息
     * @param comment 评论信息
     * @param request 用于获取评论者Ip
     */
    @PostMapping("comment")
    public ResponseEntity<Void> addComment(
            @RequestBody ArticleComment comment,
            HttpServletRequest request){
        String ip = request.getHeader("X-Real-IP");
        comment.setIp(ip);
        articleService.addComment(comment);
        String title = articleService.getArticleTitleById(comment.getArticleId());
        // 给自己邮件提醒
        mailTemplate.sendTxtMail(
                "标题为: " + title + " 的博文收到一条新的评论",
                "评论内容: " + comment.getContent(),
                mailTemplate.getRootMail());
        return ResponseEntity.ok(null);
    }

    /**
     * 获取全部有评论的博文列表
     * @return 评论的博文列表
     */
    @GetMapping("commentArticle")
    @CheckLogin
    public ResponseEntity<List<ArticleInfo>> getArticleHasComment(){
        return ResponseEntity.ok(articleService.getArticleHasComment());
    }

    /**
     * 根据博文Id，获取单个博文的评论
     * @param articleId 博文Id
     * @return 博文评论信息列表
     */
    @GetMapping("comment/{articleId}")
    public ResponseEntity<List<ArticleComment>> getCommentsByArticleId(
            @PathVariable("articleId") Long articleId){
        return ResponseEntity.ok(articleService.getCommentByArticleId(articleId));
    }

    /**
     * 根据评论Id，删除评论
     * @param commentId 评论Id
     */
    @DeleteMapping("comment/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable("commentId") Long commentId){
        articleService.deleteCommentById(commentId);
        return ResponseEntity.ok(null);
    }
}

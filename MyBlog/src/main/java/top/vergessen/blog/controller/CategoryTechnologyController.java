package top.vergessen.blog.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import top.vergessen.blog.auth.CheckLogin;
import top.vergessen.blog.domain.CategoryInfo;
import top.vergessen.blog.domain.Technology;
import top.vergessen.blog.service.CategoryTechnologyService;

import java.util.List;

/**
 * 分类信息和技术栈信息前端控制器
 * @author Vergessen
 * @date 2020/7/8 17:19.
 */
@RestController
@CrossOrigin
@RequestMapping("catetech")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CategoryTechnologyController {

    private final CategoryTechnologyService categoryTechnologyService;

    /**
     * 获取全部分类信息
     */
    @GetMapping("category")
    public ResponseEntity<List<CategoryInfo>> getAllCategory(){
        return ResponseEntity.ok(categoryTechnologyService.selectAllCategory());
    }

    /**
     * 新增分类信息，不添加重复分类
     * @param categoryInfo 前端提供的新增数据
     */
    @PostMapping("category")
    @CheckLogin
    public ResponseEntity<Void> addCategory(@RequestBody CategoryInfo categoryInfo){
        categoryTechnologyService.addCategory(categoryInfo);
        return ResponseEntity.ok(null);
    }

    /**
     * 更新分类信息,如果分类图标变化会删除原图片
     * @param categoryInfo 前端提供的更新数据
     */
    @PutMapping("category")
    @CheckLogin
    public ResponseEntity<Void> updateCategory(@RequestBody CategoryInfo categoryInfo){
        categoryTechnologyService.updateCategory(categoryInfo);
        return ResponseEntity.ok(null);
    }

    /**
     * 删除分类信息,如果分类图标变化会删除原图片
     * @param categoryInfo 需要删除的分类信息
     */
    @DeleteMapping("category")
    @CheckLogin
    public ResponseEntity<Void> deleteCategory(@RequestBody CategoryInfo categoryInfo){
        categoryTechnologyService.deleteCategory(categoryInfo);
        return ResponseEntity.ok(null);
    }

    /**
     * 获取全部技术栈信息
     */
    @GetMapping("technology")
    public ResponseEntity<List<Technology>> getAllTechnology(){
        return ResponseEntity.ok(categoryTechnologyService.selectAllTechnology());
    }

    /**
     * 新增技术栈信息，不添加重复技术栈
     * @param technology 前端提供的新增数据
     */
    @PostMapping("technology")
    @CheckLogin
    public ResponseEntity<Void> addTechnology(@RequestBody Technology technology){
        categoryTechnologyService.addTechnology(technology);
        return ResponseEntity.ok(null);
    }

    /**
     * 更新技术栈信息，不添加重复技术栈,如果图标变化会删除原图片
     * @param technology 前端提供的新增数据
     */
    @PutMapping("technology")
    @CheckLogin
    public ResponseEntity<Void> updateTechnology(@RequestBody Technology technology){
        categoryTechnologyService.updateTechnology(technology);
        return ResponseEntity.ok(null);
    }

    /**
     * 删除技术栈信息,如果图标变化会删除原图片
     * @param technology 需要删除的信息
     */
    @DeleteMapping("technology")
    @CheckLogin
    public ResponseEntity<Void> deleteTechnology(@RequestBody Technology technology){
        categoryTechnologyService.deleteTechnology(technology);
        return ResponseEntity.ok(null);
    }
}

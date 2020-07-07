package top.vergessen.blog.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.vergessen.blog.auth.CheckLogin;
import top.vergessen.blog.service.SysLogService;

import java.util.List;

/**
 * 日志获取接口
 * @author Vergessen
 * @date 2020/7/6 17:09.
 */
@RestController
@RequestMapping("sys")
@CrossOrigin
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SysLogController {

    /**
     * 各种 Service
     */
    private final SysLogService sysLogService;

    /**
     * @return 今天的访客；列表
     */
    @GetMapping("todayVisitors")
    @CheckLogin
    public ResponseEntity<List<Integer>> todayVisitors(){
        return ResponseEntity.ok(sysLogService.todayVisitors());
    }

    /**
     * @return 今年的访客列表
     */
    @GetMapping("yearVisitors")
    @CheckLogin
    public ResponseEntity<List<Integer>> yearVisitors(){
        return ResponseEntity.ok(sysLogService.yearVisitors());
    }

    /**
     * @return 今日访客数
     */
    @GetMapping("todayVisitorNum")
    @CheckLogin
    public ResponseEntity<Integer> todayVisitorNum(){
        return ResponseEntity.ok(sysLogService.getTodayVisitors());
    }

    /**
     * @return 总访客数
     */
    @GetMapping("yearVisitorNum")
    @CheckLogin
    public ResponseEntity<Integer> yearVisitorNum(){
        return ResponseEntity.ok(sysLogService.getAllVisitors());
    }
}

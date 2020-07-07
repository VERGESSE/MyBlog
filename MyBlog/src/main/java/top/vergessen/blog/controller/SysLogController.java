package top.vergessen.blog.controller;

import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import top.vergessen.blog.auth.CheckLogin;
import top.vergessen.blog.domain.SysLog;
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
     * 根据传入的page和size返回分页的日志信息
     * @param page 页码，最小为1
     * @param size 每页条数
     * @return 日志列表
     */
    @GetMapping("getLogs")
    @CheckLogin
    public ResponseEntity<PageInfo<SysLog>> getLogs(Integer page, Integer size){
        if (page <= 0 ){
            page = 1;
        }
        if (size < 1){
            size = 1;
        }
        return ResponseEntity.ok(sysLogService.getLogs(page, size));
    }

    /**
     * 更新具体ip的描述信息
     * @param ip ip
     * @param detail 新的描述信息
     */
    @PostMapping("updateIpDetail")
    @CheckLogin
    public ResponseEntity<Void> updateIpDetail(String ip, String detail){
        sysLogService.updateIpDetail(ip, detail);
        return ResponseEntity.ok(null);
    }

    /**
     * @return 今天的访客；列表
     */
    @GetMapping("todayVisitors")
    public ResponseEntity<List<Integer>> todayVisitors(){
        return ResponseEntity.ok(sysLogService.todayVisitors());
    }

    /**
     * @return 今年的访客列表
     */
    @GetMapping("yearVisitors")
    public ResponseEntity<List<Integer>> yearVisitors(){
        return ResponseEntity.ok(sysLogService.yearVisitors());
    }

    /**
     * @return 今日访客数
     */
    @GetMapping("todayVisitorNum")
    public ResponseEntity<Integer> todayVisitorNum(){
        return ResponseEntity.ok(sysLogService.getTodayVisitors());
    }

    /**
     * @return 总访客数
     */
    @GetMapping("yearVisitorNum")
    public ResponseEntity<Integer> yearVisitorNum(){
        return ResponseEntity.ok(sysLogService.getAllVisitors());
    }
}

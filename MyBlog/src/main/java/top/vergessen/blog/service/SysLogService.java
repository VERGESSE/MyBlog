package top.vergessen.blog.service;

import com.github.pagehelper.PageInfo;
import top.vergessen.blog.domain.SysLog;

import java.util.List;

/**
 * 日志相关服务
 * @author Vergessen
 * @date 2020/7/4 21:44.
 */
public interface SysLogService {

    /**
     * 新增日志接口
     * @param sysLog 日志信息
     */
    void addLog(SysLog sysLog);

    /**
     * 根据传入的page和size返回分页的日志信息
     * @param page 页码，最小为1
     * @param size 每页条数
     * @return 日志列表
     */
    PageInfo<SysLog> getLogs(Integer page, Integer size);

    /**
     * 获取24小时访客记录
     * @return 最近24小时访客记录表
     */
    List<Integer> todayVisitors();

    /**
     * 获取近12个月访客记录
     * @return 近12个月访客记录
     */
    List<Integer> yearVisitors();

    /**
     * 根据提供的ip查询描述
     * @param ip 要查询的ip地址
     * @return ip的描述
     */
    String selectDetailByIp(String ip);

    /**
     * 获取今日访客记录条数
     * @return 访客记录条数
     */
    Integer getTodayVisitors();

    /**
     * 获取总访客记录条数
     * @return 访客记录条数
     */
    Integer getAllVisitors();

    /**
     * 更新具体ip的描述信息
     * @param ip ip
     * @param detail 新的描述信息
     */
    void updateIpDetail(String ip, String detail);
}

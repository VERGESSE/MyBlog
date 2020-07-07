package top.vergessen.blog.service;

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
}

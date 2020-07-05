package top.vergessen.blog.service;

import top.vergessen.blog.domain.SysLog;

/**
 * 日志相关服务
 * @author Vergessen
 * @date 2020/7/4 21:44.
 */
public interface SysLogService {

    void addLog(SysLog sysLog);
}

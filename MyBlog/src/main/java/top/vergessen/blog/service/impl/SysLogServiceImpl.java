package top.vergessen.blog.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.vergessen.blog.domain.SysLog;
import top.vergessen.blog.service.SysLogService;

/**
 * @author Vergessen
 * @date 2020/7/4 21:44.
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SysLogServiceImpl implements SysLogService {

    @Override
    public void addLog(SysLog sysLog) {

    }
}

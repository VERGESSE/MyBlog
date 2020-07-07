package top.vergessen.blog.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import top.vergessen.blog.domain.SysLog;
import top.vergessen.blog.mapper.SysLogMapper;
import top.vergessen.blog.service.SysLogService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Vergessen
 * @date 2020/7/4 21:44.
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SysLogServiceImpl implements SysLogService {

    private final SysLogMapper sysLogMapper;

    /**
     * 时间格式化器
     */
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public void addLog(SysLog sysLog) {
        sysLogMapper.insert(sysLog);
    }

    @Override
    public PageInfo<SysLog> getLogs(Integer page, Integer size) {
        return PageHelper.startPage(page, size).doSelectPageInfo(
                sysLogMapper::selectAllOrderByTime);
    }

    @Override
    public List<Integer> todayVisitors() {
        List<Integer> res = new ArrayList<>(12);
        LocalDateTime now = LocalDateTime.now();
        // 获取每个时间段的访客数
        for (int i = 12; i > 0; i--) {
            String time1 = now.minusHours(2*i).format(dateTimeFormatter);
            String time2 = now.minusHours(2*(i-1)).format(dateTimeFormatter);
            res.add(sysLogMapper.selectCountByTime(time1, time2));
        }
        return res;
    }

    @Override
    public List<Integer> yearVisitors() {
        List<Integer> res = new ArrayList<>(12);
        LocalDateTime now = LocalDateTime.now();
        now = now.minusDays(now.getDayOfMonth());
        // 获取每个时间段的访客数
        for (int i = 11; i >= 0; i--) {
            String time1 = now.minusMonths(i).format(dateTimeFormatter);
            String time2 = now.minusMonths(i-1).format(dateTimeFormatter);
            res.add(sysLogMapper.selectCountByTime(time1, time2));
        }
        return res;
    }

    @Override
    public String selectDetailByIp(String ip) {
        SysLog sysLog = sysLogMapper.selectOneByIp(ip);
        if (sysLog == null || sysLog.getDetail() == null){
            return "暂无";
        }
        return sysLog.getDetail();
    }

    @Override
    public Integer getTodayVisitors() {
        LocalDateTime now = LocalDateTime.now();
        String time1 = now.format(dateFormatter);
        String time2 = now.format(dateTimeFormatter);
        return sysLogMapper.selectCountByTime(time1, time2);
    }

    @Override
    public Integer getAllVisitors() {
        return sysLogMapper.selectCountByExample(new Example(SysLog.class));
    }

    @Override
    public void updateIpDetail(String ip, String detail) {
        sysLogMapper.updateIpDetail(ip, detail);
    }
}

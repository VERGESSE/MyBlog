package top.vergessen.blog.mapper;

import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;
import top.vergessen.blog.domain.SysLog;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Vergessen
 * @date 2020/7/4 21:20.
 */
public interface SysLogMapper extends Mapper<SysLog> {

    /**
     * 根据传入的ip获取一个{@SysLog}
     * @param ip ip
     * @return ip对应的一个日志对象
     */
    SysLog selectOneByIp(String ip);

    /**
     * 根据提供的时间范围返回数据库中的此时间范围日志数量
     * @param time1 起始时间
     * @param time2 终止时间
     * @return 日志数量
     */
    Integer selectCountByTime(@Param("time1") String time1, @Param("time2") String time2);


    /**
     * 获取全部的日志，按时间倒序排序，通过PageHelper分页使用
     * @return 日志信息列表
     */
    List<SysLog> selectAllOrderByTime();

    /**
     * 更新具体ip的描述信息
     * @param ip ip
     * @param detail 新的描述信息
     */
    void updateIpDetail(@Param("ip") String ip, @Param("detail") String detail);
}

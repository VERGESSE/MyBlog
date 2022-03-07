package top.vergessen.blog.interceptor;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import top.vergessen.blog.exception.BlogException;
import top.vergessen.blog.exception.ExceptionEnum;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * 爬虫拦截器
 * @author haoyu
 * @date 2022/3/7 11:48.
 */
@Slf4j
public class CrawlerInterceptor implements HandlerInterceptor {

    /**
     * 每秒最大访问频次
     */
    private static final int MAX_FREQUENCY = 7;

    /**
     * 每个ip访问频次
     */
    private final ConcurrentHashMap<String, AtomicInteger> FREQUENCY = new ConcurrentHashMap<>();

    /**
     * 小黑屋
     */
    private final ConcurrentHashMap<String, Object> BAN = new ConcurrentHashMap<>();

    /**
     * 定时释放访问记录线程池
     */
    private final ScheduledThreadPoolExecutor RELEASE_EXECUTOR = new ScheduledThreadPoolExecutor(
            1, new ThreadFactoryBuilder().setNameFormat("SysLog-%d").build(),
            new ThreadPoolExecutor.CallerRunsPolicy());

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 访问者的IP
        String ip = request.getHeader("X-Real-IP");
        if (StringUtils.isBlank(ip)) {
            ip = request.getRemoteAddr();
        }
        String finalIp = ip;

        // 如果当前ip在小黑屋则直接pass
        if (BAN.get(ip) != null){
            throw new BlogException(ExceptionEnum.FREQUENCY_OUT_OF_LIMITS);
        }

        // 获取当前ip本秒内访问频次
        AtomicInteger ipFrequency = FREQUENCY.get(ip);
        // 如果本秒访问频次大于了最大访问频次则直接报错
        if (ipFrequency != null && ipFrequency.get() >= MAX_FREQUENCY){
            // 关小黑屋6小时
            BAN.put(ip, new Object());
            log.info("检测到过量访问，拦截ip: " + ip);
            RELEASE_EXECUTOR.schedule(() -> {
                BAN.remove(finalIp);
            }, 6, TimeUnit.HOURS);
            throw new BlogException(ExceptionEnum.FREQUENCY_OUT_OF_LIMITS);
        }

        if (ipFrequency != null){
            // 记录频次
            ipFrequency.incrementAndGet();
        } else {
            FREQUENCY.put(ip, new AtomicInteger(1));
            // 一秒后删除此ip访问记录
            RELEASE_EXECUTOR.schedule(() -> {
                FREQUENCY.remove(finalIp);
            }, 1, TimeUnit.SECONDS);
        }


        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}

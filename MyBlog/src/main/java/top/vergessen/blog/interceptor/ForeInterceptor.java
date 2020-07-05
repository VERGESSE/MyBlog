package top.vergessen.blog.interceptor;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import top.vergessen.blog.domain.SysLog;
import top.vergessen.blog.service.SysLogService;
import top.vergessen.blog.util.LRUCache;
import top.vergessen.blog.util.UserAgentUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 前台拦截器 用于对访问数量进行统计
 * @author Vergessen
 * @date 2020/7/5 12:02.
 */
public class ForeInterceptor implements HandlerInterceptor {

    @Autowired
    private SysLogService sysLogService;
    @Autowired
    private RestTemplate restTemplate;

    /**
     * 缓存最近最多使用的60个ip的地址信息
     */
    private final LRUCache<String, String> ipAddrCache = new LRUCache<>(60);

    /**
     * 日志记录线程池
     */
    private ExecutorService logExecutor = Executors.newFixedThreadPool(2);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // 访问者的IP
        String ip = request.getHeader("X-Real-IP");
        // 访问地址
        String url = request.getRequestURL().toString();
        //得到用户的操作系统以及浏览器信息
        String userBrowser =
                UserAgentUtil.getOsAndBrowserInfo(request.getHeader("User-Agent"));

        if (StringUtils.isBlank(ip)) {
            ip = "0.0.0.0";
        }
        if (StringUtils.isBlank(url)) {
            url = "获取URL失败";
        }
        if (StringUtils.isBlank(userBrowser)) {
            userBrowser = "UnKnown";
        }

        // 构建日志
        SysLog sysLog = SysLog.builder()
                        .ip(ip)
                        .operateBy(userBrowser)
                        .operateUrl(url)
                        .build();

        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            // 保存日志信息
            sysLog.setRemark(method.getName());
        } else {
            String uri = request.getRequestURI();
            sysLog.setRemark(uri);
        }

        // 开启线程记录日志
        logExecutor.execute(() -> {
            // 根据用户ip获取用户信息
            String addr;
            addr = ipAddrCache.get(sysLog.getIp());
            if (addr == null) {
                String baseUrl = "http://whois.pconline.com.cn/ipJson.jsp?json=true&ip=";
                String json = restTemplate
                        .getForObject(baseUrl + "202.173.11.248", String.class);
                HashMap hashMap = JSON.parseObject(json, HashMap.class);
                addr = (String) Objects.requireNonNull(hashMap).get("addr");
            }
            sysLog.setAddr(addr);
            sysLogService.addLog(sysLog);
        });
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}

package top.vergessen.blog.interceptor;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
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
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
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
     * 缓存最近最多使用的666个ip的地址信息
     */
    private final LRUCache<String, String> ipAddrCache = new LRUCache<>(666);

    /**
     * 日志记录线程池
     */
    private ExecutorService logExecutor = Executors.newFixedThreadPool(5);

    /**
     * 记录访客日志
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // 访问者的IP
        String ip = request.getHeader("X-Real-IP");
        // 访问地址 使用UTF-8解码
        String url = URLDecoder.decode(request.getRequestURL().toString(), StandardCharsets.UTF_8);
        //得到用户的操作系统以及浏览器信息
        String userBrowser = UserAgentUtil.getOsAndBrowserInfo(request.getHeader("User-Agent"));

        if (StringUtils.isBlank(ip)) {
            ip = request.getRemoteAddr();
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
            // 使用UTF-8解码
            String uri = URLDecoder.decode(request.getRequestURI(), StandardCharsets.UTF_8);
            sysLog.setRemark(uri);
        }

        // 开启线程记录日志
        logExecutor.execute(() -> {
            // 根据用户ip获取用户信息
            String addr = ipAddrCache.get(sysLog.getIp());
            if (addr == null) {
                String baseUrl = "http://whois.pconline.com.cn/ipJson.jsp?json=true&ip=";
                String json = restTemplate
                        .getForObject(baseUrl + sysLog.getIp(), String.class);
                HashMap hashMap = JSON.parseObject(json, HashMap.class);
                addr = (String) Objects.requireNonNull(hashMap).get("addr");
                ipAddrCache.put(sysLog.getIp(), addr);
            }
            String detail = sysLogService.selectDetailByIp(sysLog.getIp());
            sysLog.setAddr(addr);
            sysLog.setDetail(detail == null ? addr : detail);
            sysLogService.addLog(sysLog);
        });
    }
}

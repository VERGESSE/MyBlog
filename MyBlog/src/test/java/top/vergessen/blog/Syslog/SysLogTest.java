package top.vergessen.blog.Syslog;

import com.alibaba.fastjson.JSON;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;
import top.vergessen.blog.service.SysLogService;

import java.util.HashMap;
import java.util.Objects;

/**
 * @author Vergessen
 * @date 2020/7/5 14:40.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SysLogTest {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private SysLogService sysLogService;

    @Test
    public void sysLogIpAddrTest(){
        String baseUrl = "http://whois.pconline.com.cn/ipJson.jsp?json=true&ip=";
        String json = restTemplate
                .getForObject(baseUrl + "202.173.11.248", String.class);
        HashMap hashMap = JSON.parseObject(json, HashMap.class);
        System.out.println(Objects.requireNonNull(hashMap).get("addr"));
    }

    @Test
    public void getDetailByIpTest(){
        String s = sysLogService.selectDetailByIp("202.173.11.248");
        System.out.println(s);
        String s2 = sysLogService.selectDetailByIp("127.0.0.1");
        System.out.println(s2);
        String s1 = sysLogService.selectDetailByIp("135468");
        System.out.println(s1);
    }

    @Test
    public void todayVisitorsTest(){
        System.out.println(sysLogService.todayVisitors());
        System.out.println(sysLogService.yearVisitors());
    }
}

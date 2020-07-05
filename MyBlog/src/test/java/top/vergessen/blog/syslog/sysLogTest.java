package top.vergessen.blog.syslog;

import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Objects;

/**
 * @author Vergessen
 * @date 2020/7/5 14:40.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class sysLogTest {

    @Autowired
    private RestTemplate restTemplate;

    @Test
    public void sysLogIpAddrTest(){
        String baseUrl = "http://whois.pconline.com.cn/ipJson.jsp?json=true&ip=";
        String json = restTemplate
                .getForObject(baseUrl + "202.173.11.248", String.class);
        HashMap hashMap = JSON.parseObject(json, HashMap.class);
        System.out.println(Objects.requireNonNull(hashMap).get("addr"));
    }
}

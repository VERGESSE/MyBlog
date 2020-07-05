package top.vergessen.blog.util;

import cz.mallat.uasparser.OnlineUpdater;
import cz.mallat.uasparser.UASparser;
import cz.mallat.uasparser.UserAgentInfo;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * 用于从Request请求中获取到客户端的获取操作系统,浏览器及浏览器版本信息
 * @author Vergessen
 * @date 2020/7/5 11:22.
 */
@Slf4j
public class UserAgentUtil {

    /**
     * 初始化 {@UASpaper} 对象
     */
    private static UASparser uasPaper = null;
    static {
        try {
            uasPaper = new UASparser(OnlineUpdater.getVendoredInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取操作系统,浏览器及浏览器版本信息
     * @param userAgent 传入UserAgent
     * @return 返回操作系统以及浏览器信息
     */
    public static String getOsAndBrowserInfo(String userAgent) {
        // 解析UserAgent
        try {
            UserAgentInfo userAgentInfo = uasPaper.parse(userAgent);
            String osName = userAgentInfo.getOsName();
            String uaName = userAgentInfo.getUaName();
            return osName + " - " + uaName;
        } catch (IOException e) {
            log.warn("uasPaper解析失败: " + userAgent);
            return "Unknown";
        }
    }

    public static void main(String[] args) {
        System.out.println(UserAgentUtil.getOsAndBrowserInfo("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.13; rv:63.0) Gecko/20100101 Firefox/63.0"));
        System.out.println(UserAgentUtil.getOsAndBrowserInfo("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.77 Safari/537.36"));
        System.out.println(UserAgentUtil.getOsAndBrowserInfo("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.116 Safari/537.36"));
        System.out.println(UserAgentUtil.getOsAndBrowserInfo("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.95 Safari/537.36 OPR/26.0.1656.60"));
        System.out.println(UserAgentUtil.getOsAndBrowserInfo("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/536.11 (KHTML, like Gecko) Chrome/20.0.1132.11 TaoBrowser/2.0 Safari/536.11"));
        System.out.println(UserAgentUtil.getOsAndBrowserInfo("MQQBrowser/26 Mozilla/5.0 (Linux; U; Android 2.3.7; zh-cn; MB200 Build/GRJ22; CyanogenMod-7) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0"));
        System.out.println(UserAgentUtil.getOsAndBrowserInfo("Mozilla/4.0 (compatible; MSIE 6.0; ) Opera/UCWEB7.0.2.37/28/999"));
        System.out.println(UserAgentUtil.getOsAndBrowserInfo("Mozilla/5.0 (Linux;u;Android 4.2.2;zh-cn;) AppleWebKit/534.46 (KHTML,like Gecko) Version/5.1 Mobile Safari/10600.6.3 (compatible; Baiduspider/2.0; +http://www.baidu.com/search/spider.html)"));
        System.out.println(UserAgentUtil.getOsAndBrowserInfo("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/30.0.1599.101 Safari/537.36"));
        System.out.println(UserAgentUtil.getOsAndBrowserInfo("Mozilla/5.0 (iPhone; U; CPU iPhone OS 4_3_3 like Mac OS X; en-us) AppleWebKit/533.17.9 (KHTML, like Gecko) Version/5.0.2 Mobile/8J2 Safari/6533.18.5"));
        System.out.println(UserAgentUtil.getOsAndBrowserInfo("Mozilla/5.0 (iPad; CPU OS 5_0_1 like Mac OS X) AppleWebKit/534.46 (KHTML, like Gecko) Version/5.1 Mobile/9A405 Safari/7534.48.3"));
        System.out.println(UserAgentUtil.getOsAndBrowserInfo("User-Agent:Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; .NET4.0C; .NET4.0E; .NET CLR 2.0.50727; .NET CLR 3.0.30729; .NET CLR 3.5.30729; InfoPath.3; rv:11.0) like Gecko"));
    }
}

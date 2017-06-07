package qb.moviecrawler.common;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import qb.moviecrawler.database.model.Agency;
import qb.moviecrawler.database.repository.AgencyRepository;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.proxy.Proxy;
import us.codecraft.webmagic.proxy.SimpleProxyProvider;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 功能：通用方法
 * Created by 乔斌 on 2017/6/7.
 */
public class CommonUtil {

    /**
     * 功能描述：设置默认值
     * @author qiaobin
     * @param value
     */
    public final static String defaultValue(String value) {
        return StringUtils.isNotEmpty(value) ? value : "";
    }

    /**
     * 功能描述：读取文本标签属性
     * @author qiaobin
     * @param document 解析文本
     * @param cssQuery 标签名称
     * @param attr 解析属性
     */
    public final static String readElement(String document, String cssQuery, String attr) {
        Document doc = Jsoup.parse(document);
        Elements elements=doc.select(cssQuery);
        return elements.attr(attr);
    }

    /**
     * 功能描述：设置代理
     * @author qiaobin
     * @param agencies
     */
    public final static HttpClientDownloader makeProxy(List<Agency> agencies) {
        HttpClientDownloader httpClientDownloader = null;
        if (agencies.size() > 0) {
            Proxy[] proxies = new Proxy[agencies.size()];
            httpClientDownloader = new HttpClientDownloader();
            for (int i = 0; i < agencies.size(); i++) {
                proxies[i] = new Proxy(agencies.get(i).getIp(), Integer.parseInt(agencies.get(i).getPort()));
            }
            httpClientDownloader.setProxyProvider(SimpleProxyProvider.from(proxies));
        }
        return httpClientDownloader;
    }

    /**
     * 功能描述：获取电影名
     * @author qiaobin
     * @param title
     */
    public final static String getMovieName(String title) {
        Pattern p = Pattern.compile("《([^》]+)》");
        Matcher m = p.matcher(title);
        if (m.find())
            return m.group().replaceAll("《", "").replaceAll("》", "");
        else return "";
    }
}

package qb.moviecrawler.common;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import qb.moviecrawler.database.model.Agency;
import qb.moviecrawler.database.repository.AgencyRepository;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.proxy.Proxy;
import us.codecraft.webmagic.proxy.SimpleProxyProvider;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
    public static String defaultValue(String value) {
        return StringUtils.isNotEmpty(value) ? value : "";
    }

    /**
     * 功能描述：过滤html标签
     * @author qiaobin
     * @param htmlStr 正文
     */
    public static String removeHTMLTag(String htmlStr){
        String regEx_script="<script[^>]*?>[\\s\\S]*?<\\/script>"; //定义script的正则表达式
        String regEx_style="<style[^>]*?>[\\s\\S]*?<\\/style>"; //定义style的正则表达式
        String regEx_html="<[^>]+>"; //定义HTML标签的正则表达式

        Pattern p_script=Pattern.compile(regEx_script,Pattern.CASE_INSENSITIVE);
        Matcher m_script=p_script.matcher(htmlStr);
        htmlStr=m_script.replaceAll(""); //过滤script标签

        Pattern p_style=Pattern.compile(regEx_style,Pattern.CASE_INSENSITIVE);
        Matcher m_style=p_style.matcher(htmlStr);
        htmlStr=m_style.replaceAll(""); //过滤style标签

        Pattern p_html=Pattern.compile(regEx_html,Pattern.CASE_INSENSITIVE);
        Matcher m_html=p_html.matcher(htmlStr);
        htmlStr=m_html.replaceAll(""); //过滤html标签

        return htmlStr.trim(); //返回文本字符串
    }

    /**
     * 功能描述：读取文本标签属性
     * @author qiaobin
     * @param document 解析文本
     * @param cssQuery 标签名称
     * @param attr 解析属性
     */
    public static String readElement(String document, String cssQuery, String attr) {
        try {
            Document doc = Jsoup.parse(document);
            Elements elements=doc.select(cssQuery);
            return elements.attr(attr);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 功能描述：读取文本标签属性
     * @author qiaobin
     * @param document 解析文本
     * @param cssQuery 标签名称
     * @param attr 解析属性
     */
    public static String[] readElements(String document, String cssQuery, String attr) {
        try {
            Document doc = Jsoup.parse(document);
            Elements elements=doc.select(cssQuery);
            String[] arrs = new String[elements.size()];
            for (int i = 0; i < elements.size(); i++) {
                arrs[i] = elements.get(i).attr(attr);
            }
            return arrs;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 功能描述：设置代理
     * @author qiaobin
     * @param agencies
     */
    public static HttpClientDownloader makeProxy(List<Agency> agencies) {
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
    public static String getMovieName(String title) {
        try {
            Pattern p = Pattern.compile("《([^》]+)》");
            Matcher m = p.matcher(title);
            if (m.find())
                return m.group().replaceAll("《", "").replaceAll("》", "");
            else return "";
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 功能描述：根据xpath查询符合条件列表
     */
    public final static List<String> getLinksList(Page page, String xpath) {
        return page.getHtml().xpath(xpath).links().all();
    }

    /**
     * 功能描述：根据xpath查询值
     */
    public final static String getValue(Page page, String xpath) {
        return page.getHtml().xpath(xpath).get().trim();
    }

    /**
     * 功能描述：根据xpath查询值
     */
    public final static List<String> getValues(Page page, String xpath) {
        return page.getHtml().xpath(xpath).all();
    }


    /**
     * 功能描述：从文章内容解析出电影属性
     * @author qiaobin
     * @param
     */
    public static String parseProperty(String content, String property, String end) {
        try {
            String temp = content.substring(content.indexOf(property));
            temp = temp.substring(property.length(), temp.indexOf(end));
            return temp.trim().replace("　", "");
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 功能描述：获取日志路径
     * @author qiaobin
     * @param
     */
    public static String getLogName() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String formatDate = sdf.format(date);
        String path = String.format("moviecrawler.%s.log", formatDate);
        return path;
    }

    /**
     * 功能描述：将日志中失败的连接提出
     * @author qiaobin
     * @param
     */
    public static String[] parseLog(File file) {
        try {
            List<String> list = new ArrayList<>();
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line = "";
            while ((line = br.readLine()) != null) {
                Pattern p = Pattern.compile("(http):\\/\\/[\\w\\-_]+(\\.[\\w\\-_]+)+([\\w\\-\\.,@?^=%&amp;:/~\\+#]*[\\w\\-\\@?^=%&amp;/~\\+#])?");
                Matcher m = p.matcher(line);
                if (m.find()) {
                    list.add(m.group());
                }
            }
            br.close();
            if (list.size() > 0) {
                return list.toArray(new String[list.size()]);
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }
}

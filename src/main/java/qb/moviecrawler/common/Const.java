package qb.moviecrawler.common;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 功能：全局常量
 * Created by 乔斌 on 2017/6/7.
 */
public class Const {
    //-------------无忧代理-------------
    //无忧代理获取网站
    public final static String WUYOU_WEBSITE = "http://www.data5u.com/";
    //浏览器Header
    public final static String HEADER = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/28.0.1500.71 Safari/537.36";
    //无忧代理了网站获取代理列表XPATH
    public final static String WUYOU_AGENCIES_XPATH = "//div[@class='wlist']/li[2]/ul[@class='l2']";

    //-------------电影天堂-------------
    //电影标题连接
    public final static String MOVIE_LINKS = "//div[@class='co_content8']/ul/table/tbody/tr[2]/td[2]/b/a[2]";
    //分页列表(1 - n)  生成例子 ： <option value="list_7_1.html">1</option>
    public final static String PAGE_LINKS_XPATH = "//div[@class='x']/select/option";
    //电影天堂主页
    public final static String MAINPAGE = "http://www.ygdy8.net";
    //详情页匹配正则
    public final static String DETAIL_PAGE_REGIX = "html/[a-z]*/[a-z]*/[0-9]+/[0-9]+.html";
    //电影标题
    public final static String MOVIE_TITLE_XPATH = "//div[@class='co_area2']/div/h1/font/text()";
    //内容XPATH
    public final static String CONTENT_XPATH = "//div[@id='Zoom']/span/p";
    //发布时间XPATH
    public final static String PUBTIME_XPATH = "//div[@class=\"co_content8\"]/ul";
    //下载链接XPATH
    public final static String DOWNLOAD_LINKS_XPATH = "//div[@id='Zoom']/span/table/tbody/tr/td/a";

    //电影天堂爬取页面
    public final static Map<String, String> DIANYINGTIANTANG_MAP = new LinkedHashMap<>();

    static {
        DIANYINGTIANTANG_MAP.put(EnumConst.CLASSIFY.GUONEIMOVIE.ID, "http://www.ygdy8.net/html/gndy/china/index.html");   //国内电影
        DIANYINGTIANTANG_MAP.put(EnumConst.CLASSIFY.OUMEIMOVIE.ID, "http://www.ygdy8.net/html/gndy/oumei/index.html");   //欧美电影
        DIANYINGTIANTANG_MAP.put(EnumConst.CLASSIFY.RIHANMOVIE.ID, "http://www.dytt8.net/html/gndy/rihan/index.html");   //日韩电影
        DIANYINGTIANTANG_MAP.put(EnumConst.CLASSIFY.HUAYUTV.ID, "http://www.dytt8.net/html/tv/hytv/index.html");   //华语电视
        DIANYINGTIANTANG_MAP.put(EnumConst.CLASSIFY.OUMEITV.ID, "http://www.dytt8.net/html/tv/oumeitv/index.html");   //欧美电视
        DIANYINGTIANTANG_MAP.put(EnumConst.CLASSIFY.RIHANTV.ID, "http://www.ygdy8.net/html/tv/rihantv/index.html");   //日韩电视
//        DIANYINGTIANTANG_MAP.put(EnumConst.CLASSIFY.LATESTVARIETY.ID, "http://www.dytt8.net/html/zongyi2013/index.html");   //最新综艺
//        DIANYINGTIANTANG_MAP.put(EnumConst.CLASSIFY.CARTOON.ID, "http://www.dytt8.net/html/dongman/index.html");   //动漫
    }

    //-------------80s-------------
    public final static String EIGHTS_LAST_PAGE_XPATH = "//div[@class='pager']/a[6]";
}

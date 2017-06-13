package qb.moviecrawler.common;

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
    public final static String MOVIE_LINKS ="//div[@class='co_content8']/ul/table/tbody/tr[2]/td[2]/b/a[2]";
    //分页列表(1 - n)  生成例子 ： <option value="list_7_1.html">1</option>
    public final static String PAGE_LINKS_XPATH = "//div[@class='x']/select/option";
    //电影天堂主页
    public final static String MAINPAGE = "http://www.ygdy8.net";
    //详情页匹配正则
    public final static String DETAIL_PAGE_REGIX = "html/[a-z]*/[a-z]*/[0-9]+/[0-9]+.html";

}

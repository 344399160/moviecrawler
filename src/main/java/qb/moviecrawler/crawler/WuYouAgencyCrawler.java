package qb.moviecrawler.crawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import qb.moviecrawler.common.CheckIPUtils;
import qb.moviecrawler.common.CommonUtil;
import qb.moviecrawler.common.Const;
import qb.moviecrawler.common.UserAgentUtils;
import qb.moviecrawler.database.model.Agency;
import qb.moviecrawler.database.repository.AgencyRepository;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.ArrayList;
import java.util.List;

/**
 * 功能：无忧网代理获取
 * Created by 乔斌 on 2017/6/7.
 */
public class WuYouAgencyCrawler implements PageProcessor {

    @Autowired
    private AgencyRepository agencyRepository;

    public WuYouAgencyCrawler(AgencyRepository agencyRepository) {
        this.agencyRepository = agencyRepository;
    }

    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000)
            .setUserAgent(UserAgentUtils.radomUserAgent());

    @Override
    public void process(Page page) {

        List<String> all = page.getHtml().xpath(Const.WUYOU_AGENCIES_XPATH).all();
        for (String s : all) {
            Document doc = Jsoup.parse(s);
            Elements elements = doc.select("span");
            String ip = CommonUtil.defaultValue(elements.get(0).child(0).text());
            String port = CommonUtil.defaultValue(elements.get(1).child(0).text());
            String anonymityDegree = CommonUtil.defaultValue(elements.get(2).child(0).child(0).text());
            String type = CommonUtil.defaultValue(elements.get(3).child(0).child(0).text());
            String country = CommonUtil.defaultValue(elements.get(4).child(0).child(0).text());
            String operator = CommonUtil.defaultValue(elements.get(6).child(0).child(0).text());
            if (country.equals("中国")) {
                System.out.println(ip + ":" + Integer.parseInt(port));
                if (CheckIPUtils.checkValidIP(ip, Integer.parseInt(port))) {
                    Agency agency = new Agency();
                    agency.setIp(ip);
                    agency.setPort(port);
                    agency.setAnonymityDegree(anonymityDegree);
                    agency.setType(type);
                    agency.setCountry(country);
                    agency.setOperator(operator);
                    agencyRepository.save(agency);
                }
            }
        }
    }

    @Override
    public Site getSite() {
        return site;
    }


}

package qb.moviecrawler.crawler;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;
import qb.moviecrawler.common.CommonUtil;
import qb.moviecrawler.common.Const;
import qb.moviecrawler.common.UserAgentUtils;
import qb.moviecrawler.database.model.Comment;
import qb.moviecrawler.database.model.DownloadLink;
import qb.moviecrawler.database.model.Movie;
import qb.moviecrawler.database.repository.DownloadLinkRepository;
import qb.moviecrawler.database.repository.MovieRepository;
import qb.moviecrawler.database.util.jpa.Criteria;
import qb.moviecrawler.database.util.jpa.Restrictions;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 功能：80s网站爬取
 * Created by 乔斌 on 2017/6/19.
 */
public class EightsCrawler implements PageProcessor {

    private String movieMainPage = "http://www.80s.tw/movie/list";

    private String mainPage = "http://www.80s.tw";

    private MovieRepository movieRepository;

    private DownloadLinkRepository downloadLinkRepository;

    //爬取类型（grap 抓取， regrap 错误链接重新爬取）
    private String grapType;

    public EightsCrawler(MovieRepository movieRepository, DownloadLinkRepository downloadLinkRepository, String grapType) {
        this.movieRepository = movieRepository;
        this.grapType = grapType;
        this.downloadLinkRepository = downloadLinkRepository;
    }

    @Override
    public void process(Page page) {
        if (page.getUrl().get().equals(movieMainPage)) {
            List<String> pageLinks = new ArrayList<>();
            String lastPagePath = page.getHtml().xpath(Const.EIGHTS_LAST_PAGE_XPATH).links().get();
            int lastPage = Integer.parseInt(lastPagePath.substring(lastPagePath.lastIndexOf("p") + 1, lastPagePath.length()));
            for (int i = 1; i <= lastPage; i++) {
                pageLinks.add(String.format("%s/-----p%s", movieMainPage, i));
            }
//            page.addTargetRequests(pageLinks);
            page.addTargetRequest(pageLinks.get(0));
        } else {
            if (page.getUrl().regex(Const.EIGHTS_PAGE_REGIX).match()) {  //匹配分页链接
                List<String> titleLinks = page.getHtml().xpath(Const.EIGHTS_PAGE_LINKS_XPATH).links().all();
                titleLinks.stream().map(titleLink -> {
                    return mainPage + titleLink;
                }).collect(Collectors.toList());
                page.addTargetRequests(titleLinks);
            } else { //详情页
                Movie movie = parseContent(page);
                Movie savedMoive = this.existMovie(movie.getName());
                if (null != savedMoive && !CollectionUtils.isEmpty(movie.getLinks())) {   //如果有记录存在， 比较下载链接，如果不存在新链接则保存
                    List<DownloadLink> oldLinks = savedMoive.getLinks();
                    List<DownloadLink> links = movie.getLinks();
                    List<DownloadLink> combineList = new ArrayList<>();
                    if (CollectionUtils.isEmpty(oldLinks)) {
                        savedMoive.setLinks(links);
                        savedMoive.setLastTime(new Date());
                        movieRepository.save(savedMoive);
                    }
                    if (!CollectionUtils.isEmpty(oldLinks)) {
                        //查询库里相同id记录是否有该link
                        for (DownloadLink link : links) {
                            if (downloadLinkRepository.linkExist(link.getLink(), savedMoive.getId()) <= 0) {
                                combineList.add(link);
                            }
                        }
                        if (combineList.size() > 0) {
                            combineList.addAll(oldLinks);
                            savedMoive.setLinks(combineList);
                            savedMoive.setLastTime(new Date());
                            movieRepository.save(savedMoive);
                        }
                    }
                } else {
                    movieRepository.save(movie);
                }
            }
        }
    }

    private Movie parseContent(Page page) {
        Movie movie = new Movie();
        //电影名
        String title = CommonUtil.getValue(page, Const.EIGHTS_MOVIE_TITLE_XPATH);
        //别名
        String alias = CommonUtil.getValue(page, Const.EIGHTS_ALIAS_XPATH);
        if (StringUtils.isNotEmpty(alias)) {
            alias = StringUtils.join(alias.split(","), "/").trim().replace(" ", "");
            title = String.format("%s/%s", title, alias);
        }
        //海报图片
        String coverImg = CommonUtil.getValue(page, Const.EIGHTS_COVER_IMG_XPATH);
        //截图
        String screenShotImg = CommonUtil.getValue(page, Const.EIGHTS_SCREENSHOT_IMG_XPATH);
        //概述
        String describe = CommonUtil.getValue(page, Const.EIGHTS_DESCRIBE_XPATH);
        //导演
        String director = CommonUtil.getValue(page, Const.EIGHTS_DIRECTOR_XPATH);
        //上映时间
        String releaseDate = CommonUtil.getValue(page, Const.EIGHTS_RELEASEDATE_XPATH);
        //片长
        String filmLength = CommonUtil.getValue(page, Const.EIGHTS_FILMLENGTH_XPATH);
        //评分
        String score = CommonUtil.getValue(page, Const.EIGHTS_SCORE_XPATH);
        //主演
        List<String> actors = CommonUtil.getValues(page, Const.EIGHTS_ACTOR_XPATH);
        //类型
        List<String> types = CommonUtil.getValues(page, Const.EIGHTS_TYPE_XPATH);
        String type = StringUtils.join(types, "/");
        //地区
        List<String> countryList = CommonUtil.getValues(page, Const.EIGHTS_AREA_XPATH);
        countryList.remove("豆瓣短评 ");
        String country = StringUtils.join(countryList, "/");
        //内容
        String content = CommonUtil.getValue(page, Const.EIGHTS_CONTENT_XPATH);

        movie.setFirstTime(new Date());
        movie.setLastTime(new Date());
        movie.setId(UUID.randomUUID().toString());
        movie.setName(title);
        movie.setSourceURL(page.getUrl().get());
        movie.setYear(releaseDate.substring(0, releaseDate.indexOf("-")));
        movie.setReleaseDate(releaseDate);
        movie.setCountry(country);
        movie.setType(type);
        movie.setFilmLength(filmLength);
        movie.setDirector(director);
        movie.setActor(StringUtils.join(actors, ","));
        movie.setAbs(content);
        movie.setScore(score);
        movie.setCoverImg(coverImg);
        movie.setScreenshotImg(screenShotImg);
        movie.setIntroduce(describe);
        //------------
        List<DownloadLink> list = new ArrayList<>();
        DownloadLink link = new DownloadLink();
        link.setId(UUID.randomUUID().toString());
        link.setLink("www.baidu.com");
        link.setFirstDate(new Date());
        list.add(link);
        movie.setLinks(list);

        List<Comment> comments = new ArrayList<>();
        Comment comment = new Comment();
        comment.setComment("lalal");
        comments.add(comment);
        movie.setComments(comments);

        //------
        return movie;
    }

    private Movie existMovie(String movieName) {
        Criteria<Movie> criteria = new Criteria<>();
        criteria.add(Restrictions.eq("name", movieName));
        return movieRepository.findOne(criteria);
    }

    @Override
    public Site getSite() {
        Site site = Site.me().setTimeOut(10000).setRetryTimes(3)
                .setSleepTime(1000).setCharset("utf-8")
                .setUserAgent(UserAgentUtils.radomUserAgent());
        return site;
    }

}

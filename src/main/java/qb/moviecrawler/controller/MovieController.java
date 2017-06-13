package qb.moviecrawler.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import qb.moviecrawler.service.MovieService;

/**
 * 功能：电影
 * Created by 乔斌 on 2017/6/13.
 */
@RestController
@RequestMapping("movie")
public class MovieController {

    @Autowired
    private MovieService movieService;

    /**
     * 功能描述：电影信息爬取
     * @author qiaobin
     * @param
     */
    @RequestMapping(value = "/grapmovie")
    public void grapMovie() {
        movieService.grapMovieDetail();
    }
}

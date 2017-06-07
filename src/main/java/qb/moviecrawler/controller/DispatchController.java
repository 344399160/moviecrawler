package qb.moviecrawler.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import qb.moviecrawler.service.DispatchService;

/**
 * 功能：爬虫调度
 * Created by 乔斌 on 2017/6/7.
 */
@RestController
@RequestMapping("dispatcher")
public class DispatchController {

    @Autowired
    private DispatchService dispatchService;

    /**
     * 功能描述：爬取代理信息
     * @author qiaobin
     */
    @RequestMapping(value = "/grapAgencies")
    public String saveTag(){
        dispatchService.grapAgencies();
        return "success";
    }
}

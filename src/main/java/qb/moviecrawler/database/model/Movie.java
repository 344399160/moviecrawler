package qb.moviecrawler.database.model;

import lombok.Data;
import lombok.Getter;

import javax.persistence.*;
import java.util.Date;

/**
 * 功能：电影实体
 * Created by 乔斌 on 2017/6/13.
 */
@Data
@Entity
@Table(name = "`MOVIE`")
public class Movie {

    /**
     *  主键
     */
    @Id
    @Column(name="`ID`", length = 40)
    private String id;

    /**
     *  影片名称
     */
    @Column(name="`NAME`", length = 200)
    private String name;

    /**
     *  年代
     */
    @Column(name="`YEAR`", length = 50)
    private String year;

    /**
     *  产地
     */
    @Column(name="`COUNTRY`", length = 100)
    private String country;

    /**
     *  类别
     */
    @Column(name="`TYPE`", length = 100)
    private String type;

    /**
     *  分类
     */
    @Column(name="`CLASSIFY`")
    private int classify;

    /**
     *  字幕
     */
    @Column(name="`SUBTITLE`", length = 50)
    private String subtitle;

    /**
     *  片长
     */
    @Column(name="`FILM_LENGTH`", length = 100)
    private String filmLength;

    /**
     *  主演
     */
    @Column(name="`ACTOR`", length = 10000)
    private String actor;

    /**
     *  导演
     */
    @Column(name="`DIRECTOR`", length = 100)
    private String director;

    /**
     *  上映日期
     */
    @Column(name="`RELEASEDATE`", length = 100)
    private String releaseDate;

    /**
     *  简介
     */
    @Column(name="`ABS`")
    @Lob
    private String abs;

    /**
     *  封面
     */
    @Column(name="`COVER_IMG`", length = 200)
    private String coverImg;

    /**
     *  截图
     */
    @Column(name="`SCREENSHOT_IMG`", length = 200)
    private String screenshotImg;

    /**
     *  下载地址
     */
    @Column(name="`LINKS`")
    @Lob
    private String links;

    /**
     *  第一次入库时间
     */
    @Column(name="`FIRSTTIME`")
    private Date firstTime;

    /**
     *  最后一次入库时间
     */
    @Column(name="`LASTTIME`")
    private Date lastTime;


}

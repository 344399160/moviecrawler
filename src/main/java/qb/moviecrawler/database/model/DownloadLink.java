package qb.moviecrawler.database.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * 功能：电影下载地址
 * Created by 乔斌 on 2017/6/15.
 */
@Data
@Entity
@Table(name = "`DOWNLOAD_LINKS`")
public class DownloadLink implements java.io.Serializable{

    /**
     *  主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    /**
     *  标题
     */
    @Column(name="`TITLE`", length = 500)
    private String title;

    /**
     *  链接
     */
    @Column(name="`LINK`", length = 500)
    private String link;

    /**
     *  资源大小
     */
    @Column(name="`SOURCE_SIZE`", length = 50)
    private String sourceSize;

    /**
     *  资源画质
     */
    @Column(name="`SOURCE_TYPE`", length = 50)
    private String sourceType;

    /**
     *  入库时间
     */
    @Column(name="`FIRST_DATE`")
    private Date firstDate;

    /**
     * 格式 （平板、TV、手机）
     */
    @Column(name = "`TYPE`")
    private String type;

}

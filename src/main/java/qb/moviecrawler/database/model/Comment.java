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
@Table(name = "`COMMENT`")
public class Comment implements java.io.Serializable{

    /**
     *  主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    /**
     *  评论
     */
    @Column(name="`COMMENT`")
    @Lob
    private String comment;

    /**
     *  评论人
     */
    @Column(name="`USER_NAME`", length = 1000)
    private String userName;

    /**
     *  时间
     */
    @Column(name="`COMMENT_TIME`")
    private Date commentTime;

    /**
     *  入库时间
     */
    @Column(name="`FIRST_DATE`")
    private Date firstDate;


    /**
     *  预留字段
     */
    @Column(name="`RESERVED`")
    private String reserved;
}

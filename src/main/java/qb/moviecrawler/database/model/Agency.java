package qb.moviecrawler.database.model;


import lombok.Data;
import lombok.Getter;

import javax.persistence.*;
import java.io.Serializable;


/**
 * 功能：无忧网代理实体 http://www.data5u.com/
 * Created by 乔斌 on 2017/6/7.
 */
@Data
@Entity
@Table(name = "`AGENCY`")
public class Agency implements Serializable{

    /**
     *  ip 地址
     */
    @Id
    @Column(name="`IP`")
    @Getter
    private String ip;

    /**
     *  端口
     */
    @Column(name="`PORT`")
    private String port;

    /**
     *  匿名度
     */
    @Column(name="`ANONYMITY_DEGREE`")
    private String anonymityDegree;

    /**
     *  类型
     */
    @Column(name="`TYPE`")
    private String type;

    /**
     *  国家
     */
    @Column(name="`COUNTRY`")
    private String country;

    /**
     *  运营商
     */
    @Column(name="`OPERATOR`")
    private String operator;
}

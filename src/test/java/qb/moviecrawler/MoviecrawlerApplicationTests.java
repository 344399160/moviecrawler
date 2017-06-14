package qb.moviecrawler;

import org.junit.Test;
import qb.moviecrawler.common.CheckIPUtils;
import qb.moviecrawler.common.UserAgentUtils;

import java.lang.annotation.Target;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MoviecrawlerApplicationTests {



    @Test
    public void test() {
        //116.226.101.93 9999
//        System.out.println(CheckIPUtils.checkValidIP("116.226.101.93", Integer.parseInt("9999")));
        System.out.println(CheckIPUtils.checkValidIP("60.178.87.240", Integer.parseInt("8080")));
    }
}

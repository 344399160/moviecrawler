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
        boolean flag = CheckIPUtils.checkValidIP("118.178.86.181", 80);
        System.out.println(flag);
    }
}

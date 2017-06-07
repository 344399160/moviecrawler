package qb.moviecrawler;

import org.junit.Test;

import java.lang.annotation.Target;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MoviecrawlerApplicationTests {



    @Test
    public void test() {
        String title = "2016年动作《超级强盗》BD中文字幕";
        Pattern p = Pattern.compile("《([^》]+)》");
        Matcher m = p.matcher(title);
        if (m.find())
        System.out.println(m.group());
    }
}

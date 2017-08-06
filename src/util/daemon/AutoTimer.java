package util.daemon;

import util.crawler.MatomeCrawler;

import java.sql.SQLException;
import java.util.TimerTask;

public class AutoTimer extends TimerTask {

    private MatomeCrawler crawler;

    public AutoTimer() { //생성자.
        crawler = new MatomeCrawler();  //생성되었을 때 크롤러 인스턴스를 만들어서.
    }

    public void run() {
        String url = "https://matome.naver.jp/feed/hot";
        String selector = "item";
        try {
            crawler.updateNewsTable();  //매 실행시마다 업데이트를 실행한다.
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

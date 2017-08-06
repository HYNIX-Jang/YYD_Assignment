package util.daemon;

import javax.servlet.http.HttpServlet;
import java.util.Timer;

public class CrawlerDaemon extends HttpServlet {
    private Timer jobScheduler;

    @Override
    public void init() {
        AutoTimer job = new AutoTimer();
        jobScheduler = new Timer();

        //로딩후 10초 경과후부터 20분 간격으로 지속적으로 크롤링.
        jobScheduler.scheduleAtFixedRate(job, 1000 * 10, 1000 * 5);
    }

    public void finalize() { //객체가 소멸될 때 스케쥴러 멈춰주기
        jobScheduler.cancel();
    }
}



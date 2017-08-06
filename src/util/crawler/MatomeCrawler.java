package util.crawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import util.mysql.DataVO;
import util.mysql.JdbcUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class MatomeCrawler {

    private Elements InfoItem;

    public void getDataSet(String url, String selector) {
        //       return Elements
        try {
            Document doc = Jsoup.connect(url).get();
            this.InfoItem = doc.select(selector);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    public void truncTable() {
//        Connection con = JdbcUtil.getConnection();
//        PreparedStatement ps = null;
//        try {
//            ps = con.prepareStatement("TRUNCATE TABLE matome ");
//            ps.executeUpdate(); //クエリの実行
//            System.out.println("TRUNCATE SUCCESS!");
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } finally {
//            if (ps != null) {
//                try {
//                    ps.close();
//                } catch (Exception e) {
//                }
//            }
//        }
//    }

    //뉴스 갱신 함수.
    public void updateNewsTable() throws SQLException {
        Connection con = JdbcUtil.getConnection();
        String url = "https://matome.naver.jp/feed/hot";
        String selector = "item";
        getDataSet(url, selector); //InfoItem 更新

//     MATOMEテーブルのデーターを全部削除してデーターを入れる
        PreparedStatement ps = con.prepareStatement("TRUNCATE TABLE matome ");
        ps.executeUpdate(); //クエリの実行
        System.out.println("TRUNCATE SUCCESS!");
        System.out.println("Updated!");

        for (int i = 0; i < InfoItem.size(); i++) {
            Element item = InfoItem.get(i);
            String title = item.select("title").text();
            String link = item.select("link").text();
            String pubDate = item.select("pubDate").text();
            SimpleDateFormat originalDf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH);
            java.util.Date utilDate = new java.util.Date();
            try {
                utilDate = originalDf.parse(pubDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());

//            SimpleDateFormat newDate = new SimpleDateFormat("yyyy年 MM月 dd日 E曜日");
//            String sqlDate = newDate.format(utilDate);

            insertMatome(title, link, sqlDate, con);
        }
    }

    //DB news 테이블에 뉴스를 입력하는 함수.
    private int insertMatome(String title, String href, java.sql.Date sqlDate, Connection con) {
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement("INSERT INTO matome(title, link, pubDate) VALUES(?, ?, ?)");
            ps.setString(1, title);
            ps.setString(2, href);
            ps.setDate(3, sqlDate);
            ps.executeUpdate(); //クエリの実行
        } catch (SQLException e) {
            //すでにあるまとめがあるときは挿入ダメである
            e.printStackTrace();
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return 1;
    }

    //해당 시간대에 특보를 가져오는 함수.
    public List<DataVO> getMatomeList() {
        List<DataVO> matome;
        Connection con = JdbcUtil.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            matome = new ArrayList<DataVO>();
            ps = con.prepareStatement("SELECT * FROM matome ORDER BY NO");
            rs = ps.executeQuery();
            while (rs.next()) {
                DataVO dataVO = makeNewsFromResultSet(rs);
                matome.add(dataVO);
            }
            return matome;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception e) {
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception e) {
                }
            }
        }

    }

    //결과 DB셋을 DataVO 객체로 만들어줌.
    private DataVO makeNewsFromResultSet(ResultSet rs) throws SQLException {
        DataVO temp = new DataVO();
        temp.setLink(rs.getString("link"));
        temp.setTitle(rs.getString("title"));
        temp.setPubDate(rs.getString("pubDate"));
        return temp;
    }


}


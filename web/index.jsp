<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="util.crawler.MatomeCrawler" %>
<%@ page import="util.mysql.DataVO" %>
<%@ page import="java.util.List" %>
<%@ page import="util.crawler.MatomeCrawler" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>NAVERまとめ「クロール」</title>
    <link rel="stylesheet" href="css/style.css">
    <link rel="stylesheet" href="css/materialize.min.css">
    <script src="js/materialize.min.js"></script>
    <script src="js/jquery.min.js"></script>
    <script src="js/jquery.smooth-scroll.js"></script>
    <script src="js/script.js"></script>
</head>
<body>
<%
    //　アイテムターグを全部探してリストにセイヴ
    MatomeCrawler matomeCrawler = new MatomeCrawler();
//    try {
//        matomeCrawler.updateNewsTable(url, selector);
//    } catch (SQLException e) {
//        e.printStackTrace();
//    }

    List<DataVO> matome = matomeCrawler.getMatomeList();
    //     JSTLで matomeを使用
    request.setAttribute("matome", matome);
%>

<%--<ul>--%>
<%--<c:forEach items="${matome}" var="data" begin="1" end="30" varStatus="num">--%>
<%--<li>--%>
<%--<a href="${data.link}"><h3>${num.count}) ${data.title} |--%>
<%--&lt;%&ndash;数字) 題目(リンク)&ndash;%&gt;--%>
<%--<fmt:setLocale value="ja_JP"/>--%>
<%--<fmt:formatDate pattern="yyyy年 MM月 dd日 E曜日"--%>
<%--value="${date}"/></h3>--%>
<%--</a></li>--%>
<%--</c:forEach>--%>
<%--</ul>--%>
<%--Parsing Matome--%>


<header class="navbar-fixed" id="matome">
    <nav style="background-color: #03A9F4;">
        <div class="container">
            <b><a href="#" class="brand-logo">日本情報サイト</a></b>
            <div class="nav-wrapper">
                <ul id="nav-mobile" class="right hide-on-med-and-down">
                    <li><b><a href="#matome" style="font-size: 1.5rem">まとめ</a></b></li>
                </ul>
            </div>
        </div>
    </nav>
</header>

<div class="container">
    <div class="matome">
        <table class="highlight responsive-table bordered centered">
            <thead>
            <tr>
                <th>番号</th>
                <th>題目</th>
                <th>更新日</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${matome}" var="data" begin="0" end="29" varStatus="num">
                <tr>
                    <td>${num.count}</td>
                    <td onclick="location.href='${data.link}'">
                        <b>${data.title}</b>
                    </td>
                    <td>
                        <fmt:parseDate value="${data.pubDate}" var="parseDate" pattern="yyyy-MM-dd"/>
                        <fmt:setLocale value="ja_JP"/>
                        <fmt:formatDate pattern="yyyy年 MM月 dd日 E曜日"
                                        value="${parseDate}"/>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</div>
</body>
</html>

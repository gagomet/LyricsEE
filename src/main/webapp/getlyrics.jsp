<%--
  Created by IntelliJ IDEA.
  User: Padonag
  Date: 26.08.2014
  Time: 15:06
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1" %>
<%@ page errorPage="error.jsp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>GetLyrics</title>
</head>
<body>

<table border="1" cellpadding="5" cellspacing="5">
    <tr>
        <th>Artist Name</th>
        <th>Song name</th>
        <th>Lyrics</th>
    </tr>

    <c:forEach var="song" items="${songsList}">
        <tr>
            <td>${song.artistName}</td>
            <td>${song.songName}</td>
            <td>
                <div id="spoiler${song.songName}" style="display:none">
                        ${song.lyrics}
                </div>
                <button title="Click to show/hide content" type="button"
                        onclick="if(document.getElementById('spoiler${song.songName}') .style.display=='none')
                                {document.getElementById('spoiler${song.songName}') .style.display=''}
                                else{document.getElementById('spoiler${song.songName}') .style.display='none'}">
                    Show Lyrics
                </button>
            </td>
        </tr>
    </c:forEach>

    <c:if test="${currentPage != 1}">
        <td><a href="${pageContext.request.contextPath}/search.db?query=${query}&page=${currentPage - 1}" method="get">Previous</a></td>
    </c:if>

    <%--For displaying Page numbers.
    The when condition does not display a link for the current page--%>
    <table border="1" cellpadding="5" cellspacing="5">
        <tr>
            <c:forEach begin="1" end="${noOfPages}" var="i">
                <c:choose>
                    <c:when test="${currentPage eq i}">
                        <td>${i}</td>
                    </c:when>
                    <c:otherwise>
                        <td><a href="${pageContext.request.contextPath}/search.db?query=${query}&page=${i}" method="get">${i}</a></td>
                    </c:otherwise>
                </c:choose>
            </c:forEach>
        </tr>
    </table>

    <%--For displaying Next link --%>
    <c:if test="${currentPage lt noOfPages}">
        <td><a href="${pageContext.request.contextPath}/search.db?query=${query}&page=${currentPage + 1}" method="get">Next</a></td>
    </c:if>
</table>

</body>
</html>

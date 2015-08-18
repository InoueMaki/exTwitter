<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="exTwitter.OnceBean"%>
<%@ page import="exTwitter.Once"%>
<%@page import="java.util.ArrayList"%>
<%@ page import="java.lang.Integer"%>

<% request.setCharacterEncoding("UTF-8"); %>
<% ArrayList<OnceBean> onceList = (ArrayList)session.getAttribute("onceList"); %>

<!doctype html>
<html>
	<head>
		<title>ツイート削除</title>
		<jsp:include page="../exTwitterTemplate/header.jsp" />
	</head>
	
	<body>
		<jsp:include page="../exTwitterTemplate/menu_bar.jsp" />
	
		<!-- タイトル -->
		<div id="a">
			<div id="label">
				ツイート削除
			</div>
		<!-- フォームの配置 -->
			<form method="post" action="../Controller" onsubmit="return jump();">
				<center>
				<!-- テーブル -->
					<table class="tweet">
						<thead>
							<tr>
								<th width=85px>　</th>
								<th width=60%>ツイート</th>
								<th>予約日時</th>
							</tr>
						</thead>
						<tbody>
						<%
							if(onceList != null){
								for(int i=0;i<onceList.size();i++){
									out.println("<tr>");
									out.println("<td data-label=\"　\" align=\"center\"><button id=\"button\" onclick=\"delBtn("+i+")\" name=\"btn\" value=\"単発削除\" style=\"width:80px\">削除</button></td>");
									out.println("<td data-label=\"ツイート\" align=\"center\">" + onceList.get(i).getText() + "</td>");
									out.println("<td data-label=\"予約日時\" align=\"center\">" + onceList.get(i).getReserveTime() + "</td>");
									out.print("<input type=\"hidden\" id=\"del"+i+"\"value=\""+ onceList.get(i).getOnceId()+"\">");
									out.print("</tr>");
								}
							}
						%>
						</tbody>
					</table>
				<!-- こここまでTBL -->
				</center>
				</form>
		</div>
	</body>
	
	<script type="text/javascript" src="once-del.js"></script>
	
</html>		

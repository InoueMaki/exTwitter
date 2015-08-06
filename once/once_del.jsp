<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="exTwitter.OnceBean"%>
<%@ page import="exTwitter.Once"%>
<%@page import="java.util.ArrayList"%>
<%@ page import="java.lang.Integer"%>

<% request.setCharacterEncoding("UTF-8"); %>

<!doctype html>
<html>
	<head>
		<title>単発ツイート削除</title>
		<jsp:include page="../exTwitterTemplate/header.jsp" />
	
	<!-- タイトル -->
		<div id="a">
			<div id="label">
				単発ツイート削除
			</div>
		<!-- フォームの配置（delBtnは後で作成する） -->
			<form method="post" action="../Controller" onsubmit="return jump();">
				<center>
				<!-- テーブル -->
					<table class="tweet">
						<thead>
							<tr>
								<th width=10%>　</th>
								<th width=50%>ツイート</th>
								<th>予約日時</th>
							</tr>
						</thead>
						<tbody>
						<%
							if(Once.onceList != null){
								for(int i=0;i<Once.onceList.size();i++){
									out.println("<tr>");
									out.println("<td data-label=\"　\" align=\"center\"><button id=\"button\" onclick=\"delBtn("+i+")\" name=\"btn\" value=\"単発削除\">削除</button></td>");
									out.println("<td data-label=\"ツイート\" align=\"center\">" + Once.onceList.get(i).getText() + "</td>");
									out.println("<td data-label=\"予約日時\" align=\"center\">" + Once.onceList.get(i).getReserveTime() + "</td>");
									out.print("<input type=\"hidden\" id=\"del"+i+"\"value=\""+Once.onceList.get(i).getOnceId()+"\">");
									out.print("</tr>");
								}
							}
						%>
						</tbody>
					</table>
				<!-- こここまでTBL -->
				</form>
				<br>
				<form method="post" action="Controller">
					<button id="button" name="btn"  value="単発">戻る</button>
				</form>
			</center>
		</div>
		
	<script type="text/javascript" src="once-del.js"></script>
	
</html>		

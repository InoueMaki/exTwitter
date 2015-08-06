<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="exTwitter.OnceBean"%>
<%@ page import="exTwitter.Once"%>
<%@ page import="java.util.Calendar"%>
<%@ page import="java.lang.Integer"%>
<%@page import="java.util.ArrayList"%>
<!doctype html>
<html>
	<%	
		//現在時刻の取得
		Calendar cal = Calendar.getInstance();
		int now_minute = cal.get(Calendar.MINUTE);
		cal.add(Calendar.MINUTE,+5-(now_minute%5));
		int now_year = cal.get(Calendar.YEAR);
  		int now_month = cal.get(Calendar.MONTH)+1;
  		int now_day = cal.get(Calendar.DATE);
  		int now_hour = cal.get(Calendar.HOUR_OF_DAY);
  		now_minute = cal.get(Calendar.MINUTE);
  		//ここまで
  		
  		Integer flg = (Integer)session.getAttribute("onceflg");	//ツイート登録後かどうかのフラグを取得
  		session.setAttribute("onceflg",0);	//セッションの値を0にしておく
	%>	
	<head>
		<title>単発ツイート作成</title>
		<jsp:include page="../exTwitterTemplate/header.jsp" />
	<!-- タイトル -->
		<div id="a">
			<div id="label">
				単発ツイート作成
			</div>
			
			<!-- 以下、フォームの配置 -->
				<center>
					<form method="post" action="../Controller" onSubmit="return twbtn(this)">
				<!-- ツイートのタイトルと本文の入力フォーム -->
					<textarea name="text" maxlength="140" placeholder="ツイートを入力してね" required></textarea><br>
				<!-- ツイート終わり -->
				<!-- 日付指定チェックボックス -->
					<dev id="check">
						<br>
							<input type="checkbox" id="chk1" name="chk1" onclick='chkdisp(this)' /><label for="chk1">日時指定する</label>
						<br>
					</dev>
				<!--ツイート日時の入力フォーム-->
					<div id="select_t">
						<br>
						日付
						<% out.println("<input id=\"year\" type=\"number\" name=\"year\" require max=2020 min=2015 value="+ now_year +" step=1>年</input>"); %>
						<% out.println("<input type=\"number\" name=\"month\" require max=12 min=1 value="+ now_month +" step=1>月</input>"); %>
						<% out.println("<input type=\"number\" name=\"day\" require max=31 min=1 value="+ now_day +" step=1>日</input>"); %>
						<br><br>
						時刻
						<% out.println("<input type=\"number\" name=\"hour\" require max=23 min=0 value="+ now_hour +" step=1>時</input>"); %>
						<% out.println("<input type=\"number\" name=\"minute\" require max=59 min=0 value="+ now_minute +" step=5>分</input>"); %>
					</div>
					<!--日時ここまで-->
				<br>
				<button id="button" name="btn" value="単発登録">ツイート登録</button>
			</form>
			<br>
			<div Align="left">登録ツイート一覧</div>
			<div align="right">
				<form method="post" action="../Controller">
					<button id="button" name="btn" value="単発削除">削除画面へ</button>
				</form>
			</div>
			<br>
			<!--テーブル-->
		<table class="tweet">
			<thead>
				<tr>
					<th width=70% >ツイート</th>
					<th>予約日時</th>
				</tr>
			</thead>
			<tbody>
				<%
					if(Once.onceList != null){
						for(int i=0;i<Once.onceList.size();i++){
							out.print("<tr>");
							out.print("<td data-label=\"ツイート\" align=\"center\">" + Once.onceList.get(i).getText() + "</td>");
							out.print("<td data-label=\"予約日時\" align=\"center\">" + Once.onceList.get(i).getReserveTime() + "</td>");
							out.print("</tr>");
						}
					}
				%>
			</tbody>
		</table>
				<!--テーブル終わり-->
		</div>
		
	<%if(flg!=0){%>
		<input type="hidden" id="flg" value="1">
	<%}else{%>
		<input type="hidden" id="flg" value="0">
	<%}%>

	<script type="text/javascript" src="once-form.js"></script>

	</body>
</html>
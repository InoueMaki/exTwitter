<%@page contentType="text/html; charset=UTF-8" %>
<%@page import="exTwitter.RoutineBean"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.lang.*"%>
<%@ page import="java.util.Calendar"%>

<%
	//定期ツイート一覧受け取り
	ArrayList<RoutineBean> tweetList = (ArrayList<RoutineBean>)session.getAttribute("tweetList");

	//現在時刻の取得
	Calendar cal = Calendar.getInstance();
	int nowYear = cal.get(Calendar.YEAR);
	int nowMonth = cal.get(Calendar.MONTH)+1;
	int nowDay = cal.get(Calendar.DATE);
	int nowHour = cal.get(Calendar.HOUR_OF_DAY);
	cal.add(Calendar.MINUTE,+5-(cal.get(Calendar.MINUTE)%5));
	int nowMinute = cal.get(Calendar.MINUTE);
%>

<!doctype html>
<html>
	
	<head>
		<title>定期ツイート作成</title>
		<jsp:include page="../exTwitterTemplate/header.jsp" />
	</head>
	
	<body>
		<jsp:include page="../exTwitterTemplate/menu_bar.jsp" />
		
		<!-- タイトル -->
		<div id="a">
			<div id="label">
				定期ツイート作成
			</div>
			
			<!-- 以下、フォームの配置 -->
			<!--<form action="test">-->
			<form class="large" action="../Controller" onsubmit="return errorCheck()" method="post">
				<center>
					
				<!-- ツイートのタイトルと本文の入力フォーム -->
					<input type="text" required class="twt_title" size="7" id="title" name="title" maxlength="10" require autofocusend placeholder="ツイートのタイトル">
						10文字まで<br><br>
					<textarea id="text" required name="text" class="twt_text" maxlength="140" placeholder="ツイートを入力してね"></textarea><br>
				<!-- ツイート終わり -->					
					<br>					
				<!--ツイートの期間と時刻の入力フォーム-->
					<div id="inputbox">
						<div id="time">
							期間
						</div>
						<div id="time">
							<input type="number" class="inp_4num" id="start_year" name="st_y" value=<%= nowYear %> max=2020 min=2015 step=1 >年
							<input type="number" class="inp_2num" id="start_month" name="st_m" value=<%= nowMonth %> max=12 min=1 step=1 >月
							<input type="number" class="inp_2num" id="start_day" name="st_d" value=<%= nowDay %> max=31 min=1 step=1 >日
						</div>
						<div id="time">～　
							<input type="number" class="inp_4num" id="end_year" name="end_y" value=<%= nowYear+1 %> max=2020 min=2015 step=1 >年
							<input type="number" class="inp_2num" id="end_month" name="end_m" value=<%= nowMonth %> max=12 min=1 step=1 >月
							<input type="number" class="inp_2num" id="end_day" name="end_d" value=<%= nowDay %> max=31 min=1 step=1 >日
						</div>
							<br>
						<div id="time">
							ツイートする時刻
						</div>
						<div id="time">
							<input type="number" class="inp_2num" id="tweet_hour" name="twt_h" value=<%= nowHour %> max=23 min=0 step=1 >時</input>
							<input type="number" class="inp_2num" id="tweet_minute" name="twt_m" value=<%= nowMinute %> max=55 min=0 step=5 >分</input>
						</div>
					</div>
				<!--期間と時刻終わり-->
				<!--ツイート周期の入力フォーム-->
					<div id="routine">
						<div id="routine_in">ツイート周期
						</div>
						<!-- 曜日or日付 -->
						<div id="routine_in">
							<input type="radio" class="rad_chk" id="weekday" name="entryPlan" value="hoge1" onclick="entryChange();" checked="checked" /><label for="weekday">曜日指定</label>
							<input type="radio" class="rad_chk" id="day" name="entryPlan" value="hoge2" onclick="entryChange();" /><label for="day">日付指定</label>
						</div>
					</div>
					<!--曜日指定フォームの表示-->
					<div id="week" name="weekly">
						<div id="chk">
							<input type="checkbox" class="rad_chk" name="chk" value=1 id="chk1"/><label for="chk1">月</label>
							<input type="checkbox" class="rad_chk" name="chk" value=2 id="chk2"/><label for="chk2">火</label>
							<input type="checkbox" class="rad_chk" name="chk" value=3 id="chk3"/><label for="chk3">水</label>
							<input type="checkbox" class="rad_chk" name="chk" value=4 id="chk4"/><label for="chk4">木</label>
						</div>
						<div id="chk">
							<input type="checkbox" class="rad_chk" name="chk" value=5 id="chk5"/><label for="chk5">金</label>
							<input type="checkbox" class="rad_chk" name="chk" value=6 id="chk6"/><label for="chk6">土</label>
							<input type="checkbox" class="rad_chk" name="chk" value=7 id="chk7"/><label for="chk7">日</label>
							<input type="checkbox" class="rad_chk" id="chk8" onclick="chkWeek();" /><label for="chk8">平日</label>
						</div>
					</div>
					<!--日付指定フォームの表示-->
					<div id="month">
						<table>
						<tbody id="seldays">
							<tr>
								<td></td>
								<td><input type="number" class="inp_2num" name="days" id="num0" value=1  max=31 min=1 step=1 >日</input></td>
								<td><input type="checkbox" class="rad_chk" name="monthend" id="monthend"><label for="monthend">月末</label></td>
						</tbody>
						</table>
						<input type="button" value="追加入力" onclick="addElement()"> 
					</div>
					<br>
					<button name="btn" id="button" class="btn" value="定期登録">ツイート登録</button>
				</center>
				
			</form>
			<!--</form>-->
			<!-- ここまでフォームの配置 -->
			
			<% if(tweetList!=null && tweetList.size()!=0){ %>
					<!-- 以下、定期ツイート一覧の表示 -->
					 <br><div id="font30px" align="left"><font size="4">登録ツイート一覧</font></div> 
					 <br> 
					 <div align="right"><font size="5"> 
					 <form action="../Controller" method="post"> 
						 <button class="btn" id="button"  name="btn" value="定期削除">削除画面へ</button> 
					 </form> 
					 </font></div><br> 
					<!-- テーブル -->
					 <center> 
					 <table class="tweet"> 
						 <thead> 
							 <tr> 
								 <th>タイトル</th> 
								 <th>本文</th> 
								 <th>時刻</th> 
								 <th>開始日</th> 
								 <th>終了日</th> 
							 </tr> 
						 </thead> 
						 <tbody> 
						<%for(int i=0;i<tweetList.size();i++){%> 
							 <tr> 
							 <td data-label="タイトル"> <%= tweetList.get(i).getTitle() %> </td> 
							 <td data-label="本文"> <%= tweetList.get(i).getSnippet() %> </td> 
							 <td data-label="時間"><%= tweetList.get(i).getPostTime() %> </td> 
							 <td data-label="開始日"> <%= tweetList.get(i).getStartDate() %> </td> 
							 <td data-label="終了日"> <%= tweetList.get(i).getEndDate() %> </td> 
							 </tr> 
						<%}%> 
						 </tbody> 
					 </table> 
					 </center> 
				<%}%> 
			
			<!--定期ツイート一覧表示終わり-->
		</div><!--<div id="a">終わり-->
		
<%	
	/*フラグ受け取り*/
	Integer flg;
	if((session.getAttribute("flg"))!=null){
		flg = (Integer)(session.getAttribute("flg"));
	}else{
		flg = 0;
	}
	session.setAttribute("flg",0);
%>

<% 
	if(flg!=0){
		out.print("<input type='hidden' id='flg' value='1'>");
	}else{
		out.print("<input type='hidden' id='flg' value='0'>");
	}
%>

	<script type="text/javascript"src="routine-form.js"></script>
	
	</body>
</html>

<%@page contentType="text/html; charset=UTF-8"%>
<%@ page import="exTwitter.OnceBean"%>
<%@ page import="exTwitter.RoutineBean"%>
<%@ page import="exTwitter.Scheduler"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Arrays"%>
<%
	if (session.getAttribute("year")==null){}

%>


<!DOCTYPE html>
<html>
	<head>
		<title>スケジュール</title>
		<jsp:include page="../exTwitterTemplate/header.jsp" />
		<LINK rel="stylesheet" type="text/css" href="../schedule.css">
		<script type="text/javascript" src="schedule.js"></script>
	</head>
	<body>
	<jsp:include page="../exTwitterTemplate/menu_bar.jsp" />
		<div id="a">
		<div id="label">
				スケジュール
			</div>
		<%if (session.getAttribute("year")!=null){
				int year = Integer.parseInt(session.getAttribute("year").toString());
				int month = Integer.parseInt(session.getAttribute("month").toString());
	
				ArrayList<ArrayList<OnceBean>> OnceBeans = (ArrayList<ArrayList<OnceBean>>)session.getAttribute("OnceBeans");
				ArrayList<ArrayList<RoutineBean>> RoutineBeans = (ArrayList<ArrayList<RoutineBean>>)session.getAttribute("RoutineBeans");
				Arrays.asList(session.getAttribute("calendarDay"));
				int[] calendarDay = (int[])session.getAttribute("calendarDay");
		%>
		<!-- 年月指定移動用 -->
		<form METHOD="POST" ACTION="../Controller" onSubmit="return checkInput()">
			<TABLE >
				<tr>
					<td>
						<input type="number" class="scheYear" name="scheYear" step=1 min=2010 max=2020 value=<%=year%>>年
						<input type="number" class="scheMonth" name="scheMonth" step=1 min=1 max=12 value=<%=month%>>月
						<button value="スケジュール" name="btn" class="moveBtn">
							移動
						</button>
					</td>
				</tr>
				<tr>
					<td id="yError" style="display:none;color:red;">
						2010～2020の範囲で入力してください
					</td>
				</tr>
				<tr>
					<td id="mError" style="display:none;color:red;">
						1～12の範囲で入力してください
					</td>
			</table>
		</form>
		<!-- ここまで。（年月指定移動用） -->
		
		
		<!-- 年/月表示、単発/定期カラー表示 -->
		<table class="headPanel">
			<tr>
				<td><p class="scheYM"><%=year%>年<%=month%>月</p></td>
				<td><p class="sampleOnce">ツイート</p><p class="sampleRoutine">定期ツイート</p></td>
			</tr>
		</table>
		<!-- ここまで。（年/月表示、単発/定期カラー表示） -->
		
		
		<% if(calendarDay[0]>0){%>
		<!-- カレンダーTBL/詳細TBL外枠 -->
		<table class="frame">
			<tr>
				<td class="scheTBL">
				
					<!-- カレンダーTBL -->
					<table class="schedule">
						<tr>
							<td class="scheWeekSun">日</td>
							<td class="scheWeek">月</td>
							<td class="scheWeek">火</td>
							<td class="scheWeek">水</td>
							<td class="scheWeek">木</td>
							<td class="scheWeek">金</td>
							<td class="scheWeekSat">土</td>
						</tr>
						<tr>
			<% int i;
			String mnth="Prev";
			for(i=0;i<OnceBeans.size();i++){
				if(calendarDay[i]==1){
					if(mnth=="Prev"){
						mnth="";
					}else{
						mnth="Next";
					}
				}
				if (i%7==0){%>
							<td class="scheDateSun<%=mnth%>"><%=calendarDay[i]%></td>
				<% }else if(i%7==6){ %>
							<td class="scheDateSat<%=mnth%>"><%=calendarDay[i]%></td>
						</tr>
						<tr>
					<% for(int j=(i-6);j<=i;j++){%>
					<% if(j%7==0){%>
							<td class="scheTweetSun">
					<% }else if(j%7==6){%>
							<td class="scheTweetSat">	
					<% }else{%>
							<td class="scheTweet">
					<% }
					if(OnceBeans.get(j).size()>0){%>
								<div class="scheBtn"><button class="scheOnceBtn" onClick="dispOnce(<%=j+1%>)"><%=OnceBeans.get(j).size()%></button></div>
					<%}
					if(RoutineBeans.get(j).size()>0){%>
								<div class="scheBtn"><button class="scheRoutineBtn" onClick="dispRoutine(<%=j+1%>)"><%=RoutineBeans.get(j).size()%></button></div>
					<%}else{%>
								<div class="scheBtn"></div>	
					<%}%>
							</td>
					<% }%>
						</tr>
				<% }else{%>
							<td class="scheDate<%=mnth%>"><%=calendarDay[i]%></td>
				<% }
			}%>
						</tr>
					</table>
					<!-- ここまで。（カレンダーTBL） -->
					
					
				</td>
				<td class="twtTBL">
					<!-- 詳細TBL -->
					<table class="detail" border=1 id="detail">	
						<tbody id="detbody">
							<tr>
						</tbody>
					</table>
					<!-- ここまで。（詳細TBL） -->
				</td>
			</tr>
		</table>	
		<!-- ここまで。（カレンダーTBL/詳細TBL外枠） -->
		
		
		<%-- 以下、詳細データ格納用Hidden領域 --%>
		<%-- 単発ツイート詳細 --%>
		<table hidden>
			<% for(i=0;i<OnceBeans.size();i++){%>
			<%-- 一日分 --%>
			<tr id="once_<%=i+1%>">
				<% for(int j=0;j<OnceBeans.get(i).size();j++){%>
				<td>
					<p><%=OnceBeans.get(i).get(j).getText().trim()%></p>
					<p><%=OnceBeans.get(i).get(j).getReserveTime().trim()%></p>
					<p><%=(OnceBeans.get(i).get(j).getPosted()==0 ? "未":"済")%></p>
				<% }%>
				</td>
			<% }%>
			</tr>
		</table>
		
		<%-- 定期ツイート詳細 --%>
		<table hidden>
			<% for(i=0;i<RoutineBeans.size();i++){%>
			<%-- 一日分 --%>
			<tr id="routine_<%=i+1%>">
				<% for(int j=0;j<RoutineBeans.get(i).size();j++){%>
				<td>
					<p><%=RoutineBeans.get(i).get(j).getTitle().trim()%></p>
					<p><%=RoutineBeans.get(i).get(j).getText().trim()%></p>
					<p><%=RoutineBeans.get(i).get(j).getPostTime().trim()%></p>
					<p><%=RoutineBeans.get(i).get(j).getStartDate().trim()%></p>
					<p><%=RoutineBeans.get(i).get(j).getEndDate().trim()%></p>
				</td>
				<% }%>
			</tr>
			<% }%>
		</table>
		<%}%> <%-- End of (if(calendarDay[0]>0){ /line:44) --%>
	<%}%>
	</div>
	</body>
</html>
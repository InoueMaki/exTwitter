<%@page contentType="text/html; charset=UTF-8"%>
<%@ page import="exTwitter.OnceBean"%>
<%@ page import="exTwitter.RoutineBean"%>
<%@ page import="exTwitter.Scheduler"%>
<%@ page import="java.util.ArrayList"%>
<!DOCTYPE html>
<html>
	<head>
		<title>test</title>
		<jsp:include page="../exTwitterTemplate/header.jsp" />
		<LINK rel="stylesheet" type="text/css" href="../schedule.css">
		<script type="text/javascript" src="schedule.js"></script>
	</head>
	<body>
	<jsp:include page="../exTwitterTemplate/menu_bar.jsp" />
	
		<!-- 年月指定移動用 -->
		<form METHOD="POST" ACTION="../Controller">
			<TABLE >
				<tr>
					<td>
						<input type="number" class="scheYear" name="scheYear" step=1 min=2010 max=2020 value=<%=Scheduler.year%>>年
						<input type="number" class="scheMonth" name="scheMonth" step=1 min=1 max=12 value=<%=Scheduler.month+1%>>月
						<button value="スケジュール" name="btn" class="moveBtn">
							移動
						</button>
					</td>
				</tr>
			</table>
		</form>
		<!-- ここまで。（年月指定移動用） -->
		
		
		<!-- 年/月表示、単発/定期カラー表示 -->
		<table border=0 class="headPanel">
			<tr>
				<td><p class="scheYM"><%=Scheduler.year%>年<%=Scheduler.month+1%>月</p></td>
				<td align=left><p class="sampleOnce">単発ツイート</p><p class="sampleRoutine">定期ツイート</p></td>
			</tr>
		</table>
		<!-- ここまで。（年/月表示、単発/定期カラー表示） -->
		
		
		<% if(Scheduler.calendarDay[0]>0){%>
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
			for(i=0;i<Scheduler.OnceBeans.size();i++){
				if (i%7==0){%>
							<td class="scheDateSun"><%=Scheduler.calendarDay[i]%></td>
				<% }else if(i%7==6){ %>
							<td class="scheDateSat"><%=Scheduler.calendarDay[i]%></td>
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
					if(Scheduler.OnceBeans.get(j).size()>0){%>
								<div class="scheBtn"><button class="scheOnceBtn" onClick="dispOnce(<%=j+1%>)"><%=Scheduler.OnceBeans.get(j).size()%></button></div><br/>
					<%}
					if(Scheduler.RoutineBeans.get(j).size()>0){%>
								<div class="scheBtn"><button class="scheRoutineBtn" onClick="dispRoutine(<%=j+1%>)"><%=Scheduler.RoutineBeans.get(j).size()%></button></div>
					<%}%>
							</td>
					<% }%>
						</tr>
				<% }else{%>
							<td class="scheDate"><%=Scheduler.calendarDay[i]%></td>
				<% }
			}%>
						</tr>
					</table>
					<!-- ここまで。（カレンダーTBL） -->
					
					
				</td>
				<td class="twtTBL">
				
					<!-- 詳細TBL -->
					<table class="detail" border=1 id="detail">	
						<tbody>
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
			<% for(i=0;i<Scheduler.OnceBeans.size();i++){%>
			<%-- 一日分 --%>
			<tr id="once_<%=i+1%>">
				<% for(int j=0;j<Scheduler.OnceBeans.get(i).size();j++){%>
				<td>
					<p><%=Scheduler.OnceBeans.get(i).get(j).getText().trim()%></p>
					<p><%=Scheduler.OnceBeans.get(i).get(j).getReserveTime().trim()%></p>
					<p><%=(Scheduler.OnceBeans.get(i).get(j).getPosted()==0 ? "未":"済")%></p>
				<% }%>
				</td>
			<% }%>
			</tr>
		</table>
		
		<%-- 定期ツイート詳細 --%>
		<table hidden>
			<% for(i=0;i<Scheduler.RoutineBeans.size();i++){%>
			<%-- 一日分 --%>
			<tr id="routine_<%=i+1%>">
				<% for(int j=0;j<Scheduler.RoutineBeans.get(i).size();j++){%>
				<td>
					<p><%=Scheduler.RoutineBeans.get(i).get(j).getTitle().trim()%></p>
					<p><%=Scheduler.RoutineBeans.get(i).get(j).getText().trim()%></p>
					<p><%=Scheduler.RoutineBeans.get(i).get(j).getStartDate().trim()%></p>
					<p><%=Scheduler.RoutineBeans.get(i).get(j).getEndDate().trim()%></p>
					<p><%=(Scheduler.RoutineBeans.get(i).get(j).getPosted()==0 ? "未":"済")%></p>
				</td>
				<% }%>
			</tr>
			<% }%>
		</table>
		<%}%> <%-- End of (if(Scheduler.calendarDay[0]>0){ /line:44) --%>
	</body>
</html>
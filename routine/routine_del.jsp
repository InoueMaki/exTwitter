<%@page contentType="text/html; charset=UTF-8" %>
<%@page import="exTwitter.RoutineBean"%>
<%@page import="java.util.ArrayList"%>

<!doctype html>
<%
	/*定期ツイート一覧受け取り*/
	ArrayList<RoutineBean> tweetList = (ArrayList<RoutineBean>)session.getAttribute("tweetList");
%>

<html>
	<head>
		<title>定期ツイート削除</title>
		<jsp:include page="../exTwitterTemplate/header.jsp" />

	<!--以下、定期ツイート一覧の表示-->
	<jsp:include page="../exTwitterTemplate/menu_bar.jsp" />
		<div id="a">
			<div id="label">定期ツイート削除</div>
			<%
				/*以下、定期ツイート一覧の表示*/
				if(tweetList!=null && tweetList.size()!=0){%>
					<br><br>
					<form action='../Controller' onsubmit='return jump();'method='post'> 
					<center>
					<!-- テーブル -->
					<table class="tweet"> 
							<thead>
								<tr>
									<th>　</th> 
									<th>タイトル</th> 
									<th>本文</th> 
									<th>時刻</th> 
									<th width="18%">開始日</th> 
									<th width="18%">終了日</th> 
							 	</tr> 
							 </thead> 
							 <tbody> 
						<%for(int i=0;i<tweetList.size();i++){%>
							<tr id=<%= i %>>
								<td data-label="　"><button id="button2" value="定期削除" name="btn" onclick="delComfirmDialog(<%= i %>)">削除</button></td>
								<td data-label="タイトル"> <%= tweetList.get(i).getTitle() %></td>
								<td data-label="本文" > <%= tweetList.get(i).getSnippet() %></td>
								<td data-label="時刻" > <%= tweetList.get(i).getPostTime() %></td>
								<td data-label="開始日"> <%= tweetList.get(i).getStartDate() %></td>
								<td data-label="終了日"> <%= tweetList.get(i).getEndDate() %></td>
								<input type="hidden" value="<%= tweetList.get(i).getText() %>"/>
								<input type="hidden" value="<%= tweetList.get(i).getRoutineId() %>"/>
							</tr> 
						<%}%>
						 </tbody></table> 
						 </center>  
						 </form> 
						
				<%}else{%>
					 <h3>登録されているツイートはありません<h3>
				<%}%>
				
		</div>

	<script type="text/javascript" src="routine-del.js"></script>

</body>
</html>

<%@page contentType="text/html; charset=UTF-8" %>
<%@page import="exTwitter.RoutineBean"%>
<%@page import="java.util.ArrayList"%>

<%
/*定期ツイート一覧受け取り*/
ArrayList<RoutineBean> tweetList = (ArrayList<RoutineBean>)session.getAttribute("tweetList");
Integer flg = (Integer)(session.getAttribute("flg"));
session.setAttribute("flg",0);
%>

<html>

	<link rel="stylesheet" type="text/css" href="style.css">
	
	<head>
		<title>定期ツイート削除</title>
	</head>
	
<body>
	<jsp:include page="header.jsp" />
	<!--以下、定期ツイート一覧の表示-->
		<div id="a">
			<div id="label">定期ツイート削除</div>
			<%
				if(tweetlist != null && tweetList.size() != 0){
					/*以下、定期ツイート一覧の表示*/
					out.print("登録されている定期ツイート一覧");
					out.print("<form action='Controller' onsubmit='return jump();' method='post'>");
					out.print("<center>");
					/*テーブル*/
					out.print("<table border=\"2\" style=\"font-size:24\">");
							out.print("<th></th>");
							out.print("<th>　タイトル　</th>");
							out.print("<th>　本文　</th>");
							out.print("<th>　ツイート時刻　</th>");
							out.print("<th>　開始日　</th>");
							out.print("<th>　終了日　</th>");
						out.print("</tr>");
						
						for(int i=0;i<tweetList.size();i++){
							out.print("<tr id=\""+i+"\">");
							out.print("<td><button value=\"定期削除\" name=\"btn\" onclick=\"delComfirmDialog("+i+")\">削除</button></td>");
							out.print("<td>" + tweetList.get(i).getTitle() + "</td>");
							out.print("<td>" + tweetList.get(i).getSnippet() + "</td>");
							out.print("<td>" + tweetList.get(i).getPostTime() + "</td>");
							out.print("<td>" + tweetList.get(i).getStartDate() + "</td>");
							out.print("<td>" + tweetList.get(i).getEndDate() + "</td>");
							out.print("<input type=\"hidden\" value=\""+tweetList.get(i).getText()+"\">");
							out.print("<input type=\"hidden\" value=\""+tweetList.get(i).getRoutineId()+"\">");
							out.print("</tr>");
						}
						out.print("</table>");
						out.print("</center>");
						out.print("</form>");
						
				}else{
					out.print("<h3>"+"登録されているツイートはありません"+"<h3>");
				}
			%>
				
		</div>
	<script type="text/javascript">
		
		var jmp=0;
		
		//<!--削除の確認ダイアログ-->
		function delComfirmDialog(i){
			var tr=document.getElementById(i);
			var child = tr.childNodes;
			var str="以下のツイートを削除してよろしいですか？\n\n"+
					"タイトル\n"+child[1].innerHTML+"\n\n"+
					"本文\n"+child[6].value+"\n\n"+
					"開始日\n"+child[3].innerHTML+"\n\n"+
					"終了日\n"+child[4].innerHTML+"\n\n"+
					"時刻\n"+child[5].innerHTML+"\n\n";
					
					if(confirm(str)){
						child[7].name="del";
						jmp=1;
					}
		}
		
		//<!--onsubmitで動く関数-->
		function jump(){
			if(jmp==1){
				return true;
			}else{
				return false;
			}
		}


		//<!--削除完了ダイアログ-->
		function doneDialog(){
			var flg=0;
			<% out.print("flg=" + flg + ";" );%>
			if(flg==2){
				alert("削除完了しました！");
			}
		}
		
		//<!--画面ロード時に実行する関数設定-->
		window.onload = doneDialog;
		
	</script>
</body>
</html>

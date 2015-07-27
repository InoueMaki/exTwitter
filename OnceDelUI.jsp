<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="exTwitter.OnceBean"%>
<%@ page import="exTwitter.Once"%>
<%@page import="java.util.ArrayList"%>
<%@ page import="java.lang.Integer"%>

<% request.setCharacterEncoding("UTF-8"); %>

<% 
	Integer flg = (Integer)session.getAttribute("delflg");	//ツイート削除後かどうかのフラグを取得
	session.setAttribute("delflg",0);	//セッションの値を0にしておく
	int flgInt;	//フラグの値を代入する（JavaScriptで使う）
	if(flg == null){	//フラグがnullの場合
  		flgInt = 0;	//フラグの値を0とする
 	}
 	else{
 		flgInt = flg.intValue();	//フラグの値を代入
 	}
 %>
 
<html>
	<link rel="stylesheet" type="text/css" href="style.css">
	<head>
		<title>単発ツイート削除</title>
	</head>
	
	<jsp:include page="header.jsp" />
	
	<!-- タイトル -->
		<div id="a">
			<div id="label">
				単発ツイート削除
			</div>
		<!-- フォームの配置（delBtnは後で作成する） -->
			<form method="post" action="OnceDel" onSubmit="return delBtn()">
				<center>
				<!-- テーブル -->
					<table border="2" style="font-size:20">
						<tr>
							<th>　　　</th>
							<th>　ツイート　</th>
							<th>　予約日　</th>
							<th>　予約時間　</th>
						</tr>
						
						<%
							if(Once.onceList != null){
								for(int i=0;i<Once.onceList.size();i++){
									out.println("<tr>");
									out.println("<td align=\"center\">　<input type=\"submit\" style=\"width:50px;height:30px;font-size:15\" value=\"削除\" name=\"" + Once.onceList.get(i).getOnceId() + "\">　</td>");
									out.println("<td align=\"center\">" + Once.onceList.get(i).getText() + "</td>");
									out.println("<td align=\"center\">" + Once.onceList.get(i).getDate() + "</td>");
									out.println("<td align=\"center\">" + Once.onceList.get(i).getTime() + "</td>");
									out.print("</tr>");
								}
							}
						%>
					
					</table>
				<!-- こここまでTBL -->
				</form>
				<br>
				<form method="post" action="Control">
					<input type="submit" style="width:100px;height:40px;font-size:20" name="単発" value="戻る">
				</form>
			</center>
		</div>
	<script type="text/javascript">	
	
		//ツイート登録確認ダイアログを表示
		function delBtn(){
			if(window.confirm('ツイート削除しますか？')){
				return true;
			}
			else{
				window.alert('キャンセルしました');
				return false;
			}
		}
		
		//画面遷移後最初に読み込まれる関数
		function disp(){
			<% out.print( "var flgInt = " + flgInt + ";" ); %>	//Javaで処理した値を挿入
			if(flgInt == 1)	window.alert('登録ツイートを削除しました');	//フラグが投稿成功状態の場合ダイアログを表示
			else if(flgInt == -1) window.alert('登録ツイートの削除に失敗しました');	//失敗の場合のダイアログ
		}
		
		window.onload = disp;
		
	</script>
</html>		

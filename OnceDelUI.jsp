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
			<form method="post" action="Controller" onsubmit="return jump();">
				<center>
				<!-- テーブル -->
					<table border="2" style="font-size:20">
						<tr>
							<th>　　　</th>
							<th>　　ツイート　　</th>
							<th>　　予約日 / 時間　　</th>
						</tr>
						
						<%
							if(Once.onceList != null){
								for(int i=0;i<Once.onceList.size();i++){
									out.println("<tr>");
									out.println("<td align=\"center\"><button style=\"width:50px;height:30px;font-size:15\" onclick=\"delBtn("+i+")\" name=\"btn\" value=\"単発削除\">削除</button></td>");
									out.println("<td align=\"center\">" + Once.onceList.get(i).getText() + "</td>");
									out.println("<td align=\"center\">" + Once.onceList.get(i).getReserveTime() + "</td>");
									out.print("<input type=\"hidden\" id=\"del"+i+"\"value=\""+Once.onceList.get(i).getOnceId()+"\">");
									out.print("</tr>");
								}
							}
						%>
					
					</table>
				<!-- こここまでTBL -->
				</form>
				<br>
				<form method="post" action="Controller">
					<button style="width:100px;height:40px;font-size:20" name="btn"  value="単発">戻る</button>
				</form>
			</center>
		</div>
	<script type="text/javascript">	
	
	//画面遷移許可フラグを宣言
	var jmpFlg = 0;
	
		//ツイート登録確認ダイアログを表示
		function delBtn(i){
			if(window.confirm('ツイート削除しますか？')){
				var element = document.getElementById("del"+i);
				element.name="del";
				jmpFlg = 1;//画面遷移許可フラグ
				
			}
			else{
				window.alert('キャンセルしました');
			}
		}
		
		//画面遷移後最初に読み込まれる関数
		function disp(){
			<% out.print( "var flgInt = " + flgInt + ";" ); %>	//Javaで処理した値を挿入
			if(flgInt == 1)	window.alert('登録ツイートを削除しました');	//フラグが投稿成功状態の場合ダイアログを表示
			else if(flgInt == -1) window.alert('登録ツイートの削除に失敗しました');	//失敗の場合のダイアログ
		}
		
		//onsubmitで呼ばれる関数
		//「jmpFlg」は「delBtn」でOKをクリックすると「1」になる
		function jump(){
			if(jmpFlg==1){
				return true;
			}else{
				return false;
			}
		}
		
		window.onload = disp;
		
		</script>
</html>		

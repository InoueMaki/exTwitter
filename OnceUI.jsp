<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="exTwitter.OnceBean"%>
<%@ page import="exTwitter.Once"%>
<%@ page import="java.util.Calendar"%>
<%@ page import="java.lang.Integer"%>
<%@page import="java.util.ArrayList"%>

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
  		int flgInt;	//フラグの値を代入する（JavaScriptで使う）
  		if(flg == null){	//フラグがnullの場合
  			flgInt = 0;	//フラグの値を0とする
  		}
  		else{
  			flgInt = flg.intValue();	//フラグの値を代入
  		}
	%>
	
	<link rel="stylesheet" type="text/css" href="style.css">
	
	<head>
		<title>単発ツイート作成</title>
	</head>

	<body>

	<jsp:include page="header.jsp" />
		
	<!-- タイトル -->
		<div id="a">
			<div id="label">
				単発ツイート作成
			</div>
			
			<!-- 以下、フォームの配置 -->
			<form method="post" action="Controller" onSubmit="return twbtn(this)">
				<center>
					
				<!-- ツイートのタイトルと本文の入力フォーム -->
					<textarea name="text" style="width:400px;height:200px;resize:none;font-size:22" maxlength="140" placeholder="ツイートを入力してね" required></textarea><br>
				<!-- ツイート終わり -->
				<!-- 日付指定チェックボックス -->
					<dev id="check">
						<br>
						<input type="checkbox" id="chk1" name="chk1" onclick='chkdisp(this)' /><label for="chk1">日付指定する</label>
						<br>
					</dev>
				<!--ツイート日時の入力フォーム-->
					<div id="select_t">
						<br>
						日付指定
						<% out.println("<input type=\"number\" style=\"width:70px;height:30;font-size:20\" name=\"year\" require max=2020 min=2015 value="+ now_year +" step=1>年</input>"); %>
						<% out.println("<input type=\"number\" style=\"width:50px;height:30;font-size:20\" name=\"month\" require max=12 min=1 value="+ now_month +" step=1>月</input>"); %>
						<% out.println("<input type=\"number\" style=\"width:50px;height:30;font-size:20\" name=\"day\" require max=31 min=1 value="+ now_day +" step=1>日</input>"); %>
						<br><br>
						時刻指定
						<% out.println("<input type=\"number\" style=\"width:50px;height:30;font-size:20\" name=\"hour\" require max=23 min=0 value="+ now_hour +" step=1>時</input>"); %>
						<% out.println("<input type=\"number\" style=\"width:50px;height:30;font-size:20\" name=\"minute\" require max=59 min=0 value="+ now_minute +" step=5>分</input>"); %>
					</div>
					<!--日時ここまで-->
				<br>
				<div id="botton">
					<button style="width:140px;height:40px;font-size:20" name="btn" value="単発登録">ツイート登録</button>
				</div>
			</form>
			<br>
			<br>
			<div Align="left">登録されている時間指定ツイート一覧</div>
			<div align="right">
				<form method="post" action="Controller">
					<button style="width:120px;height:40px;font-size:20" name="btn" value="単発削除">削除画面へ</button>
				</form>
			</div>
			<center>
				<!--テーブル-->
				<table border="2" style="font-size:20">
					<tr>
						<th>　　ツイート　　</th>
						<th>　　予約日 / 時間　　</th>
					</tr>
					
					<%
						if(Once.onceList != null){
							for(int i=0;i<Once.onceList.size();i++){
								out.print("<tr>");
								out.print("<td align=\"center\">" + Once.onceList.get(i).getText() + "</td>");
								out.print("<td align=\"center\">" + Once.onceList.get(i).getReserveTime() + "</td>");
								out.print("</tr>");
							}
						}
					%>
				
				</table><!--テーブル終わり-->
			</center>			
		</div>
	
	<!-- 以下、javascript -->
	<script type="text/javascript">
	
		//チェックボックスの状態によって表示を切り替える
	 	function chkdisp(obj) {
 			if( obj.checked ){	//チェックされている
 		 		document.getElementById('select_t').style.display = "block";	//表示
  			}
  			else {	//されていない
  				document.getElementById('select_t').style.display = "none";	//非表示
			}
		}
		
		//画面遷移後最初に読み込まれる関数
		function disp(){
			<% out.print( "var flgInt = " + flgInt + ";" ); %>	//Javaで処理した値を挿入
			document.getElementById('select_t').style.display = "none";	//日付指定は初期状態で非表示
			var checkbox = document.getElementById("chk1");
			checkbox.checked = false;	//チェックボックスは初期状態ではチェックしない
			if(flgInt == 1)	window.alert('ツイートの登録を完了しました');	//フラグが投稿成功状態の場合ダイアログを表示
			else if(flgInt == -1) window.alert('ツイートの登録に失敗しました');	//失敗の場合のダイアログ
		}
		
		//日付が未来かチェックする
		function checkDate(Obj){
			 var now = new Date();
			 var chkDate = new Date(Obj.year.value , (Obj.month.value-1) , Obj.day.value , Obj.hour.value, Obj.minute.value, 59);
			 if(now.getTime() > chkDate.getTime()){
			 	return false;
			 }
			 else{
			 	return true;
			 }
		}
		
		//ツイート登録確認ダイアログを表示
		function twbtn(Obj){
			if(window.confirm('ツイート登録しますか？')){
				if(checkDate(Obj)){
						return true;
				}
				else{
						window.alert('日時の指定が過去になっています');
						return false;
				}
			}
			else{
				window.alert('キャンセルしました');
				return false;
			}
		}
		
		window.onload = disp;

	</script>
	<!-- ここまでjavascript -->

	</body>
</html>
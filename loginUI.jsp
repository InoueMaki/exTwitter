<%@page contentType="text/html; charset=UTF-8"%>

<!-- ログイン画面 -->

<HTML>

	<HEAD>
		<TITLE>ログイン画面</TITLE>
		<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1" http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<!--[if lt IE 9]>
		<script src="http://css3-mediaqueries-js.googlecode.com/svn/trunk/css3-mediaqueries.js"></script>
		<![endif]-->
		<LINK rel="stylesheet" type="text/css" href="style.css">
	</HEAD>
	
	<BODY>
		<BR><BR>
		<CENTER ID="a">
			<H1>ログイン画面</H1>
			<div id="login">
				<!-- ユーザ名・パスワード入力 -->
				<FORM METHOD="POST" ACTION="../exTwitter/Controller">
					<TABLE class="login" border="0">
						<TR><TD><center><INPUT type="text" name="user_name" name="user_name" required size=50 minlength=1 maxlength=16 pattern="^[0-9a-zA-Z]+$" placeholder="ユーザ名" autocomplete="off" autofocus></center></td></tr>
						<TR><TD><center><INPUT type="password" name="password" required size=50 minlength=4 maxlength=16 autocomplete="off" pattern="^[0-9a-zA-Z]+$" placeholder="パスワード" ></center></td></tr>
					</TABLE>
					<br>
					<button value="ログイン" name="btn" style="font-size:40px" color:black">ログイン</button>
				</FORM>
			</div>			
			<!-- 入力ミス等で戻ってきたときの処理 -->
			<%
			String error =(String)request.getAttribute("err");
			if (error!=null){%>
				<H3><font color=red><%=error%></font></H3>
			<%}else{%>
			<%}%>
		</CENTER>
	</BODY>
</HTML>

<%@page contentType="text/html; charset=UTF-8"%>

<!-- ログイン画面 -->

<HTML>

	<HEAD>
		<TITLE>ログイン画面</TITLE>
		<LINK rel="stylesheet" type="text/css" href="style.css">
	</HEAD>
	
	<BODY>
		<BR><BR>
		<CENTER ID="a">
			<H1>ログイン画面</H1>
			
			<!-- ユーザ名・パスワード入力 -->
			<FORM METHOD="POST" ACTION="../exTwitter/Controller">
				<TABLE class="login" border="0">
					<TR><TD><INPUT type="text" name="user_name" name="user_name" style="font-size:30px;" required size=50 minlength=1 maxlength=16 pattern="^[0-9a-zA-Z]+$" placeholder="ユーザ名" autocomplete="off" autofocus>
					<TR><TD><INPUT type="password" name="password" style="font-size:30px;"required size=50 minlength=4 maxlength=16 autocomplete="off" pattern="^[0-9a-zA-Z]+$" placeholder="パスワード" >
					<TR><TD><button value="ログイン" name="btn" style="font-size:40px;color:black">ログイン</button>
				</TABLE>
			</FORM>
			
			<!-- 入力ミス等で戻ってきたときの処理 -->
			<%
			String error =(String)request.getAttribute("err");
			if (error!=null){%>
				<H3><font color=red><%=error%></font></H3>
			<%}else{%>
				<H3><br></H3>
			<%}%>
		</CENTER>	
	
	</BODY>

</HTML>
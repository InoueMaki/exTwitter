<%@page contentType="text/html; charset=UTF-8"%>

<!-- ログイン画面 -->
<!DOCTYPE html>
<HTML>

	<HEAD>
		<TITLE>ログイン画面</TITLE>
		<jsp:include page="../exTwitterTemplate/header.jsp" />
	</HEAD>
	
	<BODY>
		<BR><BR>
		<CENTER ID="a">
			<H1>ログイン画面</H1>
			<div id="login">
				<!-- ユーザ名・パスワード入力 -->
				<FORM METHOD="POST" ACTION="../Controller">
					<TABLE class="login" border="0">
						<TR><TD><center><INPUT type="text" name="user_name" name="user_name" required size=50 minlength=1 maxlength=16 pattern="^[0-9a-zA-Z]+$" placeholder="ユーザ名" autocomplete="off" autofocus></center></td></tr>
						<TR><TD><center><INPUT type="password" name="password" required size=50 minlength=4 maxlength=16 autocomplete="off" pattern="^[0-9a-zA-Z]+$" placeholder="パスワード" ></center></td></tr>
					</TABLE>
					<br>
					<button value="ログイン" name="btn" style="font-size:40px; color:black">ログイン</button>
				</FORM>
			</div>			
			<!-- 入力ミス等で戻ってきたときの処理 -->
			<%
			String error =(String)session.getAttribute("err");
			session.setAttribute("err",null);
			if (error!=null){%>
				<H3><font color=red><%=error%></font></H3>
			<%}%>
		</CENTER>
	</BODY>

</HTML>

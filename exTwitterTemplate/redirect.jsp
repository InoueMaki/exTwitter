<%@page contentType="text/html; charset=UTF-8"%>

<!-- セッション切れの時リダイレクトでログイン画面に渡す -->

<HTML>
	<HEAD>
		<SCRIPT type="text/javascript">
			// 画面遷移コントローラに移動
			function link(){
			location.href="../user/login.jsp";
			}
		</SCRIPT>
		<TITLE>Session TimeOut</TITLE>
	</HEAD>

	<BODY onload="setTimeout('link()',3*1000)">
		<H1>
			セッションが切断されています。<BR>3秒後にログイン画面に戻ります。
		</H1>
		
	</body>
</html>


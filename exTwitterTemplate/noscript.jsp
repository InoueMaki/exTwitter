<%@page contentType="text/html; charset=UTF-8"%>

<!-- JavaScript非対応時に出力する画面 -->

<HTML>
	<HEAD>
	<jsp:include page="header.jsp"/>
		<TITLE>JavaScript無効</TITLE>
	</HEAD>
	<BODY>
		<div id="a">
			<H1>
				JavaScriptが無効になっています。
			</H1>
			<H3>
				有効なブラウザでご利用ください。
			</H3>
				<a href="../user/login.jsp">ログイン画面に戻る</a>
			<br>
		</div>
	</body>
</html>
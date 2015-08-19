<%@page contentType="text/html; charset=UTF-8"%>

<!-- エラー時に出力する画面 -->

<HTML>
	<HEAD>
	<jsp:include page="header.jsp"/>
		<SCRIPT type="text/javascript">
			// 画面遷移コントローラに移動
			function link(){
			location.href="../user/login.jsp";
			}
		</SCRIPT>
		<TITLE>error!</TITLE>
	</HEAD>

	<BODY onload="setTimeout('link()',3*1000)">
		<div id="a">
			<H1>
				不正な処理が行われました。<BR>3秒後にログイン画面に戻ります。
			</H1>
			<br>
				自動で遷移しない場合は<a href="../user/login.jsp">こちら</a>をクリック
			<br>
		</div>
	</body>
</html>
<%@page contentType="text/html; charset=UTF-8"%>

<HTML>
	<HEAD>
		<TITLE>メニュー画面</TITLE>
		<LINK rel="stylesheet" type="text/css" href="style.css">
	</HEAD>
	
	<BODY>
		<jsp:include page="header.jsp" />
		<CENTER ID="a">
			<H1>メニュー画面</H1>
			
			<!-- 各画面への遷移(必要性検討中<-ヘッダで十分？) -->
			<FORM METHOD="POST" ACTION="Control">
				<TABLE class="center">
					<TR>
						<TD ><INPUT type="submit" class="menuButton" value="単発ツイート" name="単発">
						<TD><DIV class="box">飲み会のリマインドや<BR>面白ページや小ネタの投稿</DIV>
					<TR>
						<TD ><INPUT type="submit" class="menuButton" value="定期ツイート" name="定期">
						<TD><DIV class="box">週報や業務報告書など<BR>定期的な連絡用</DIV>
				</TABLE>
			</FORM>
		</CENTER>	
	
	</BODY>

</HTML>
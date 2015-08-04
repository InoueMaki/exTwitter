<%@page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<HTML>
	<HEAD>
		<TITLE>メニュー画面</TITLE>
		<jsp:include page="header.jsp" />
		<div id="a">
			<div id="pc">
				<CENTER>
					<H1>メニュー画面</H1>
					<!-- 各画面への遷移(必要性検討中<-ヘッダで十分？) -->
					<FORM METHOD="POST" ACTION="Controller">
						<TABLE class="center">
							<TR>
								<TD ><button class="menuButton" value="単発" name="btn">単発ツイート</button>
								<TD><DIV class="box">飲み会のリマインドや<BR>面白ページや小ネタの投稿</DIV>
							<TR>
								<TD ><button class="menuButton" value="定期" name="btn">定期ツイート</button>
								<TD><DIV class="box">週報や業務報告書など<BR>定期的な連絡用</DIV>
						</TABLE>
					</FORM>
				</CENTER>
			</div>
			<div id="mobile">
				<CENTER>
					<H1>メニュー画面</H1>
					<!-- 各画面への遷移(必要性検討中<-ヘッダで十分？) -->
					<FORM METHOD="POST" ACTION="Controller">
						<TABLE class="center">
							<TR>
								<TD>
									<center><button class="menuButton" value="単発" name="btn">単発ツイート</button></center>
									<DIV class="box">飲み会のリマインドや<BR>面白ページや小ネタの投稿<BR><BR></DIV>
								</TD>
							</TR>
							<TR>
								<TD>
									<center><button class="menuButton" value="定期" name="btn">定期ツイート</button></center>
									<DIV class="box">週報や業務報告書など<BR>定期的な連絡用<BR><BR></DIV>
								</TD>
							</TR>
						</TABLE>
					</FORM>
				</CENTER>
			</div>
		</div>
	</BODY>
	<script type="text/javascript">
		$("#header2 li").hover(function() {
			$(this).children('ul').show();
		}, function() {
			$(this).children('ul').hide();
		});
	</script>
</HTML>
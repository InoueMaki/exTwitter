<%@page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<HTML>
	<HEAD>
		<TITLE>メニュー</TITLE>
		<jsp:include page="header.jsp" />
	</HEAD>
	
	<BODY>
		<jsp:include page="menu_bar.jsp" />
	
		<div id="a">
		
		<!-- PC用画面 -->
			<div id="pc">
				<CENTER>
					<div id="label2">メニュー</div>
					<!-- 各画面への遷移(必要性検討中<-ヘッダで十分？) -->
					<FORM METHOD="POST" ACTION="../Controller">
						<TABLE class="center">
							<TR>
								<TD ><button class="menuButton" value="単発" name="btn">ツイート登録</button></TD>
								<TD><DIV class="box">飲み会のリマインドや<BR>面白ページや小ネタの投稿用</DIV></TD>
							</TR>
							<TR>
								<TD><button class="menuButton" value="単発削除" name="btn">ツイート削除</button></TD>
								<TD><DIV class="box">予約した単発ツイートを<BR>削除できます</DIV></TD>
							</TR>
							<TR>
								<TD ><button class="menuButton" value="定期" name="btn">定期ツイート登録</button></TD>
								<TD><DIV class="box">週報や業務報告書など<BR>定期的な連絡用</DIV></TD>
							</TR>
							<TR>
								<TD ><button class="menuButton" value="定期削除" name="btn">定期ツイート削除</button></TD>
								<TD><DIV class="box">予約した定期ツイートを<BR>削除できます</DIV></TD>
							</TR>
							<TR>
								<TD><button class="menuButton" value="スケジュール" name="btn">スケジュール確認</button></TD>
								<TD><DIV class="box">ツイートの登録状況を<BR>閲覧できます</DIV></TD>
							</TR>
						</TABLE>
					</FORM>
				</CENTER>
			</div>
			
			<!-- スマホ用画面 -->
			<div id="mobile">
				<CENTER>
					<div id="label2">メニュー</div>
					<!-- 各画面への遷移(必要性検討中<-ヘッダで十分？) -->
					<FORM METHOD="POST" ACTION="../Controller">
						<TABLE class="center">
							<TR>
								<TD>
									<center><button class="menuButton" value="単発" name="btn">ツイート登録</button></center>
									<DIV class="box">飲み会のリマインドや<BR>面白ページや小ネタの投稿用<BR><BR></DIV>
								</TD>
							</TR>
							<TR>
								<TD>
									<center><button class="menuButton" value="単発削除" name="btn">ツイート削除</button></center>
									<DIV class="box">予約した単発ツイートの削除<BR><BR></DIV>
								</TD>
							</TR>
							<TR>
								<TD>
									<center><button class="menuButton" value="定期" name="btn">定期ツイート</button></center>
									<DIV class="box">週報や業務報告書など<BR>定期的な連絡用<BR><BR></DIV>
								</TD>
							</TR>
							<TR>
								<TD>
									<center><button class="menuButton" value="定期削除" name="btn">定期ツイート削除</button></center>
									<DIV class="box">予約した定期ツイートの削除<BR><BR></DIV>
								</TD>
							</TR>
							<TR>
								<TD>
									<center><button class="menuButton" value="スケジュール" name="btn">スケジュール確認</button></center>
									<DIV class="box">ツイートの登録状況を<BR>閲覧できます<BR><BR></DIV>
								</TD>
							</TR>
						</TABLE>
					</FORM>
				</CENTER>
			</div>
		</div>
	</BODY>
</HTML>
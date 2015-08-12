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
								<TD><DIV class="box">飲み会のリマインドや<BR>小ネタを登録します</DIV></TD>
							</TR>
							<TR>
								<TD><button class="menuButton" value="単発削除" name="btn">ツイート削除</button></TD>
								<TD><DIV class="box">予約したツイートを<BR>削除します</DIV></TD>
							</TR>
							<TR>
								<TD ><button class="menuButton" value="定期" name="btn">定期ツイート登録</button></TD>
								<TD><DIV class="box">業務報告書などの<BR>定期連絡を登録します</DIV></TD>
							</TR>
							<TR>
								<TD ><button class="menuButton" value="定期削除" name="btn">定期ツイート削除</button></TD>
								<TD><DIV class="box">予約した定期ツイートを<BR>削除します</DIV></TD>
							</TR>
							<TR>
								<TD><button class="menuButton" value="スケジュール" name="btn">スケジュール確認</button></TD>
								<TD><DIV class="box">ツイートの登録状況を<BR>閲覧します</DIV></TD>
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
									<DIV class="box">飲み会のリマインドや<BR>小ネタを登録します<BR><BR></DIV>
								</TD>
							</TR>
							<TR>
								<TD>
									<center><button class="menuButton" value="単発削除" name="btn">ツイート削除</button></center>
									<DIV class="box">予約したツイートを削除します<BR><BR></DIV>
								</TD>
							</TR>
							<TR>
								<TD>
									<center><button class="menuButton" value="定期" name="btn">定期ツイート</button></center>
									<DIV class="box">業務報告書などの<BR>定期連絡を登録します<BR><BR></DIV>
								</TD>
							</TR>
							<TR>
								<TD>
									<center><button class="menuButton" value="定期削除" name="btn">定期ツイート削除</button></center>
									<DIV class="box">登録定期ツイートを削除します<BR><BR></DIV>
								</TD>
							</TR>
							<TR>
								<TD>
									<center><button class="menuButton" value="スケジュール" name="btn">スケジュール確認</button></center>
									<DIV class="box">ツイートの登録状況を<BR>閲覧します<BR><BR></DIV>
								</TD>
							</TR>
						</TABLE>
					</FORM>
				</CENTER>
			</div>
		</div>
	</BODY>
</HTML>
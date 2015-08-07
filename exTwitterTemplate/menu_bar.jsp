
<!-- bodyタグ内でインクルードして使うファイル -->

<%@page contentType="text/html; charset=UTF-8" %>

	<div id="header1">
		<table>
			<tr>
				<FORM METHOD="POST" ACTION="../Controller" style="display:inline">
					<td width="800">
						<button class="headerButton" name = "btn" VALUE="メニュー" >メニュー</button>
						<button class="headerButton" name = "btn" VALUE="単発" >単発ツイート</button>
						<button class="headerButton" name = "btn" VALUE="定期" >定期ツイート</button>
					<td align="right">
						<button class="headerButton" name = "btn" VALUE="ログアウト" >ログアウト</button>
				</form>
			</tr>
		</table>
	</div>
	<div id="header2">
		<div class="menu">
		    <label for="Panel1"><span class="css-bar"></span>MENU</label>
		    <input type="checkbox" id="Panel1" class="on-off" />
		    <ul>
	 			<li>
					<FORM METHOD="POST" ACTION="../Controller" name="myForm">
	 					<input type="hidden" name = "btn" VALUE="メニュー" >
	 					<a href="#" onclick="document.myForm.submit()">メニュー画面へ</a>
	 				</form>
	 			</li>
	 			<li>
	 				<FORM METHOD="POST" ACTION="../Controller" name="myForm2">
	 					<input type="hidden" name = "btn" VALUE="単発" >
	 					<a href="#" onclick="document.myForm2.submit()">単発ツイート</a>
	 				</form>
	 			</li>
	 			<li>
	 				<FORM METHOD="POST" ACTION="../Controller" name="myForm3">
	 					<input type="hidden" name = "btn" VALUE="定期" >
	 					<a href="#" onclick="document.myForm3.submit()">定期ツイート</a>
	 				</form>
	 			</li>
	 			<li>
	 				<FORM METHOD="POST" ACTION="../Controller" name="myForm4">
	 					<input type="hidden" name = "btn" VALUE="ログアウト" >
	 					<a href="#" onclick="document.myForm4.submit()">ログアウト</a>
	 				</form>
	 			</li>
		    </ul>
		</div>
	</div>

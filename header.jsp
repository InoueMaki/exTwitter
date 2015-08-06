<%@page contentType="text/html; charset=UTF-8" %>
	<meta charset="utf-8" name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no">
	<!--[if lt IE 9]>
		<script src="http://css3-mediaqueries-js.googlecode.com/svn/trunk/css3-mediaqueries.js"></script>
	<![endif]-->
	<link rel="stylesheet" type="text/css" href="style.css">
	<script type="text/javascript" src="http://www.google.com/jsapi"></script>
	<script type="text/javascript">google.load("jquery", "1.7");</script>
</HEAD>
<BODY>
	<div id="header1">
		<table>
			<tr>
				<FORM METHOD="POST" ACTION="Controller" style="display:inline">
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
					<FORM METHOD="POST" ACTION="Controller" name="myForm">
	 					<input type="hidden" name = "btn" VALUE="メニュー" >
	 					<a href="#" onclick="document.myForm.submit()">メニュー画面へ</a>
	 				</form>
	 			</li>
	 			<li>
	 				<FORM METHOD="POST" ACTION="Controller" name="myForm2">
	 					<input type="hidden" name = "btn" VALUE="単発" >
	 					<a href="#" onclick="document.myForm2.submit()">単発ツイート</a>
	 				</form>
	 			</li>
	 			<li>
	 				<FORM METHOD="POST" ACTION="Controller" name="myForm3">
	 					<input type="hidden" name = "btn" VALUE="定期" >
	 					<a href="#" onclick="document.myForm3.submit()">定期ツイート</a>
	 				</form>
	 			</li>
	 			<li>
	 				<FORM METHOD="POST" ACTION="Controller" name="myForm4">
	 					<input type="hidden" name = "btn" VALUE="ログアウト" >
	 					<a href="#" onclick="document.myForm4.submit()">ログアウト</a>
	 				</form>
	 			</li>
		    </ul>
		</div>
	</div>

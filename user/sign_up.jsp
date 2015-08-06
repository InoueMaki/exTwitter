<%@page contentType="text/html; charset=UTF-8"%>

<!-- ユーザー登録画面 -->

<HTML>

	<HEAD>
		<TITLE>ユーザー登録</TITLE>
		<LINK rel="stylesheet" type="text/css" href="../style.css">
	</HEAD>
	
	<BODY>
		<BR><BR>
		<CENTER ID="a">
			<H1>ユーザー登録</H1>
			
			<!-- ユーザ名・パスワード入力 -->
			<FORM METHOD="POST" onsubmit="return check()" ACTION="../Controller">
				<TABLE class="login" border="0">
					<TR><TD><INPUT type="text" name="user_name" name="user_name" style="font-size:30px;" required size=50 minlength=1 maxlength=16 pattern="^[0-9a-zA-Z]+$" placeholder="ユーザ名" autocomplete="off" autofocus>
					<TR><TD><INPUT type="password" name="pass1" id="pass1" style="font-size:30px;"required size=50 minlength=4 maxlength=16 autocomplete="off" pattern="^[0-9a-zA-Z]+$" placeholder="パスワード" >
					<TR><TD><INPUT type="password" name="pass2" id="pass2" style="font-size:30px;"required size=50 minlength=4 maxlength=16 autocomplete="off" pattern="^[0-9a-zA-Z]+$" placeholder="パスワード確認入力" >
					<TR>
						<TD>
						<button value="ユーザー登録" name="btn" style="font-size:40px;color:black" >登録</button>
						<input type="reset" style="font-size:20px;color:black">
				</TABLE>
			</FORM>
	</BODY>
	
	<script type="text/javascript">
	function check(){
		var pass1 = document.getElementById("pass1").value;
		var pass2 = document.getElementById("pass2").value;
		if(pass1 === pass2){
			if(confirm("登録してよろしいですか？")){
				return true;
			}else{
				return false;
			}
		}else{
			alert("「パスワード」と「パスワード確認」が一致しません\nもう一度記入してください");
			return false;
		}
	}

	</script>	
</HTML>

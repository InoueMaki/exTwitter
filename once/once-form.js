
		//チェックボックスの状態によって表示を切り替える
	 	function chkdisp(obj) {
 			if( obj.checked ){	//チェックされている
 		 		document.getElementById('select_t').style.display = "block";	//表示
  			}
  			else {	//されていない
  				document.getElementById('select_t').style.display = "none";	//非表示
			}
		}
		
		//画面遷移後最初に読み込まれる関数
		function disp(){
			var flgInt = document.getElementById("flg").value;//Javaで処理した値を挿入
			document.getElementById('select_t').style.display = "none";	//日付指定は初期状態で非表示
			var checkbox = document.getElementById("chk1");
			checkbox.checked = false;	//チェックボックスは初期状態ではチェックしない
			if(flgInt != 0){
				window.alert('ツイートの登録を完了しました');	//フラグが投稿成功状態の場合ダイアログを表示
			}
		}
		
		//日付が未来かチェックする
		function checkDate(Obj){
			 var now = new Date();
			 var chkDate = new Date(Obj.year.value , (Obj.month.value-1) , Obj.day.value , Obj.hour.value, Obj.minute.value, 59);
			 if(now.getTime() > chkDate.getTime()){
			 	return true;
			 }
			 else{
			 	return false;
			 }
		}
		
		//ツイート登録確認ダイアログを表示
		function twbtn(Obj){
		
			if(errorCheck(Obj)){
				if(window.confirm('ツイート登録しますか？')){
					return true;
				}
			}
			
			return false;
			
		}
		
		
		//エラーチェックする関数
		function errorCheck(Obj){
			
			var chk = document.getElementById("chk1");
			
			var errorMessage = "入力内容に以下の誤りがあります。\n\n";
			var noError = 1;
			
			if(hasNoValueError(Obj)){
				errorMessage = errorMessage + "・ツイート内容がありません。\n　記入してください\n\n";
				noError = 0;
			}
			if(hasTextError(Obj)){
				errorMessage = errorMessage+"・登録できない文字列が含まれています。\n　登録できない文字列は「RT」「#」「@」\n";
				errorMessage = errorMessage+"　先頭に記述できない文字列は「d」「m」「dm」「D」「M」「DM」です。\n\n";
				noError =0;
			}
			if(hasDateError(Obj)){
				errorMessage = errorMessage + "・存在しない月日を入力しています。\n\n";
				noError = 0;
			}
			if(hasTimeError(Obj)){
				errorMessage = errorMessage + "・不正な時間を入力しています。\n\n";
				noError = 0;
			}
			if(chk.checked && checkDate(Obj)){
				errorMessage = errorMessage+"・日時が現在よりも過去になっています。\n\n";
				noError =0;
			}
			
			if(noError==1){
				return true;
			}else{
				alert(errorMessage);
				return false;
			}
		}
		
		//本文の記述がないか
		function hasNoValueError(Obj){
			
			var text = Obj.text.value;
			
			var trimedText = text.replace(/\s+/g,"");
	
			if(trimedText==""){
				return true;
			}
	
			return false;
		}
		
		//禁止文字列がないか
		function hasTextError(Obj){
	
			var text = Obj.text.value;
			
			var hasTabooWord = 0;
	
			//禁止されている文字列の検査
			if(text.indexOf("RT ")!=-1 || text.indexOf("#")!=-1 || text.indexOf("@")!=-1){
				hasTabooWord = 1;
			}
	
			//文字列の先頭の空白文字を削除
			var trimedText = text.replace(/^\s+/g, "");
			
			//先頭で禁止されている文字列の検査１
			if(trimedText.substring(0,1)=="d" || trimedText.substring(0,1)=="m" || trimedText.substring(0,2)=="dm"){
			hasTabooWord = 1;
	}
		
			//先頭で禁止されている文字列の検査2
			if(trimedText.substring(0,1)=="D" || trimedText.substring(0,1)=="M" || trimedText.substring(0,2)=="DM"){
				hasTabooWord = 1;
			}
	
			if(hasTabooWord == 1){
				return true;//禁止文字列を含んでいる
			}else{
				return false;
			}
		}
		
		//存在しない時間かどうか
		function hasTimeError(Obj) {

			var hour = Obj.hour.value;
			var minute = Obj.minute.value;

			if(hour.match(/^-?[0-9]+$/) && 0<=hour && hour<=23){
				if(minute.match(/^-?[0-9]+$/) && 0<=minute && minute<=59){
					return false;
				}
			}
			return true;
		}
		
		//存在しない日付かどうかチェック
		function hasDateError(Obj) {

			var year = Obj.year.value;
			var month = Obj.month.value;
			var day = Obj.day.value;

			var dt = new Date(year, month - 1, day);
			if(dt == null || dt.getFullYear() != year || dt.getMonth() + 1 != month || dt.getDate() != day) {
				return true;//存在しない日付あり
			}
			return false;
		}
		
		window.onload = disp;
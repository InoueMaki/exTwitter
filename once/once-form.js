
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
			
			if(hasTextError(Obj)){
				errorMessage = errorMessage+"・投稿できない文字列が含まれています。\n投稿できない文字列は「RT」「#」「@」「D」「M」「DM」です。\n\n";
				noError =0;
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
		
		//禁止文字列がないか
		function hasTextError(Obj){
	
			var text = Obj.text.value;
			var hasTabooWord = 0;
	
			if(text.indexOf("RT")!=-1 || text.indexOf("#")!=-1 || text.indexOf("@")!=-1 || text.indexOf("D")!=-1 || text.indexOf("M")!=-1 || text.indexOf("DM")!=-1){
				hasTabooWord = 1;
			}
			if(hasTabooWord == 1){
				return true;//禁止文字列を含んでいる
			}else{
				return false;
			}
		}
		
		window.onload = disp;

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
			 	return false;
			 }
			 else{
			 	return true;
			 }
		}
		
		//ツイート登録確認ダイアログを表示
		function twbtn(Obj){
			if(window.confirm('ツイート登録しますか？')){
				if(checkDate(Obj)){
						return true;
				}else{
						window.alert('日時の指定が過去になっています');
						return false;
				}
			}
			else{
				window.alert('キャンセルしました');
				return false;
			}
		}
		
		window.onload = disp;
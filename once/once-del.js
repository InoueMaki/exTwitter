	
	//画面遷移許可フラグを宣言
	var jmpFlg = 0;
	
		//ツイート登録確認ダイアログを表示
		function delBtn(i){
			if(window.confirm('ツイート削除しますか？')){
				var element = document.getElementById("del"+i);
				element.name="del";
				jmpFlg = 1;//画面遷移許可フラグ
				
			}
			else{
				window.alert('キャンセルしました');
			}
		}
		
		
		//onsubmitで呼ばれる関数
		//「jmpFlg」は「delBtn」でOKをクリックすると「1」になる
		function jump(){
			if(jmpFlg==1){
				return true;
			}else{
				return false;
			}
		}
		
		window.onload = disp;
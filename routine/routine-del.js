
		var jmp=0;
		
		//<!--削除の確認ダイアログ-->
		function delComfirmDialog(i){
			var tr=document.getElementById(i);
			var child = tr.childNodes;
			var str="以下のツイートを削除してよろしいですか？\n\n"+
					"タイトル\n"+child[1].innerHTML+"\n\n"+
					"本文\n"+child[6].value+"\n\n"+
					"開始日\n"+child[3].innerHTML+"\n\n"+
					"終了日\n"+child[4].innerHTML+"\n\n"+
					"時刻\n"+child[5].innerHTML+"\n\n";
					
					if(confirm(str)){
						child[7].name="del";
						jmp=1;
					}
		}
		
		//<!--onsubmitで動く関数-->
		function jump(){
			if(jmp==1){
				return true;
			}else{
				return false;
			}
		}


		//<!--削除完了ダイアログ-->
		function doneDialog(){
			var flg=0;
			<% out.print("flg=" + flg + ";" );%>
			if(flg==2){
				alert("削除完了しました！");
			}
		}
		
		//<!--画面ロード時に実行する関数設定-->
		window.onload = doneDialog;
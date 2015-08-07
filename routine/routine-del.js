
		var jmp=0;
		
		//<!--削除の確認ダイアログ-->
		function delComfirmDialog(i){
			var children=document.getElementById(i).children;
			var str="以下のツイートを削除してよろしいですか？\n\n"+
					"タイトル\n"+children[1].innerHTML+"\n\n"+
					"本文\n"+children[6].value+"\n\n"+
					"時刻\n"+children[3].innerHTML+"\n\n"+
					"開始日\n"+children[4].innerHTML+"\n\n"+
					"終了日\n"+children[5].innerHTML+"\n\n";
					
					
					if(confirm(str)){
						children[7].name="del";
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

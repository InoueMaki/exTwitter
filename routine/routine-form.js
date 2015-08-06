		
		/* 日付を指定する入力フォームを追加する関数 */
		var i = 1; /* i-1 = 入力フォームを追加した回数 */
		function addElement() { 
			if(document.getElementById("seldays").childNodes.length<=31){
				Tr  = document.createElement("Tr");
				Td1  = document.createElement("Td");
				Td2  = document.createElement("Td");	
				Td3  = document.createElement("Td");
				Td1.innerHTML="<button onclick='removeElement("+i+")'>削除</button> "
				Td2.innerHTML="<input type=\"number\" class=\"inp_2num\" name=\"days\" id=\"num"+i+"\""+"value=1  max=31 min=1 step=1 >日</input>";

				Tr.appendChild(Td1);
				Tr.appendChild(Td2);
				Tr.appendChild(Td3); 
				Tr.id = i;
				var objTBL = document.getElementsByTagName("tbody").item(1); 
				objTBL.appendChild(Tr);
				i = i+1;
			}
		} 
	
		/*追加した要素を削除する関数*/
		function removeElement(id) { 
			var element = document.getElementById(id);
			var tbl = document.getElementsByTagName("tbody").item(1);
			tbl.removeChild(element); 	
		} 

		
		/*「曜日指定」と「日付指定」で表示切替する関数*/
		function entryChange(){
			radio = document.getElementsByName('entryPlan');
			if(radio[0].checked) {
				document.getElementById('week').style.display = "";
				document.getElementById('month').style.display = "none";

			}else if(radio[1].checked) {
				document.getElementById('week').style.display = "none";
				document.getElementById('month').style.display = "";

			}
		}
		
		
		/*入力情報確認ダイアログを表示する関数*/
		function tweet(){
			var days=[];
			radio = document.getElementsByName('entryPlan');
			
			/*ツイート周期の文字列を生成する。*/
			/*ラジオボタンによる表示切替に合わせてダイアログに表示する文字列を切り替える。*/
			if(radio[0].checked){
				
				/*-指定曜日の受け取り*/
				var strDays="指定した曜日\n";
				var dayList=["","月","火","水","木","金","土","日"];
				
				for(var j=1;j<=7;j++){
					if(document.getElementById("chk"+j).checked){
						days[days.length]=dayList[j];
					}
				}
			}else{	
				/*指定日付の受け取り*/
				var strDays="指定した日付\n";
				for(var j=0;j<i;j++){
					if(document.getElementById("num"+j)) {
						days[days.length]=document.getElementById("num"+j).value;
					}
				}
				if(document.getElementById("monthend").checked){
					days[days.length]="月末"
				}
			}
			/*データから文字列生成*/
			for(var j=0;j<days.length;j++){
				//<!--文字の間にコンマを挟むための処理-->
				if(j!=0){
					strDays=strDays+",";
				}
				strDays=strDays+days[j];
			}
			/*ツイート周期の文字列の生成終わり*/
			
			/*ダイアログに表示する文字列の生成*/
			var strDialog="ツイートの内容は以下でよろしいですか？\n\n\n"+
					"タイトル\n"+document.getElementById("title").value+"\n\n"+
					"本文\n"+document.getElementById("text").value+"\n\n"+
					"開始日\n"+document.getElementById("start_year").value+"年"+document.getElementById("start_month").value+"月"+document.getElementById("start_day").value+"日\n\n"+
					"終了日\n"+document.getElementById("end_year").value+"年"+document.getElementById("end_month").value+"月"+document.getElementById("end_day").value+"日\n\n"+
					"時刻\n"+document.getElementById("tweet_hour").value+"時"+document.getElementById("tweet_hour").value+"分\n\n"+
					strDays;
					
					return confirm(strDialog);
			}/*入力情報確認ダイアログ関数終わり*/
		
		
		/*「平日」にチェックが入った時の処理*/
		function chkWeek(){
			wd = document.getElementById("chk8");
			if(wd.checked){
				for(var j=1;j<=5;j++){
					document.getElementById("chk"+j).checked=true;
				}
			}else{
				for(var j=1;j<=5;j++){
					document.getElementById("chk"+j).checked=false;
				}
			}
		}
		
		
		/*「月末のみ」にチェックが入った時の処理*/
		function onlyMonthend(){
			ome = document.getElementById("onlyMonthend");
			if(ome.checked){
				for(var j=0;j<i;j++){
					document.getElementById("num"+j).disabled = "true";
				}
				
			}else{
				for(var j=0;j<i;j++){
					document.getElementById("num"+j).disabled = "";
				}
			}
		}
		
		
		/*エラーチェックし、エラーない時ツイート登録関数を呼び出す関数*/
		function errorCheck(){
		
			var err=1;
			
			/*ラジオボタンが「曜日指定」、かつ、チェックボックスに何もない時のチェック*/
			var radio = document.getElementsByName('entryPlan');
			if(radio[0].checked){
				for(var j=1;j<=7;j++){
					if(document.getElementById("chk"+j).checked){
						err=0;
					}
				}
			}else{
				err=0;
			}
			/*エラーあったらメッセージ表示、なかったらtweet登録関数呼び出し*/
			if(err==1){
				alert("曜日指定をしてください");
				return false;
			}else{
				return tweet();
			}
		}
		
		
		/*登録完了ダイアログ*/
		function doneDialog(){
			var flg=document.getElementById("flg").value;
			if(flg==1){
				alert("登録完了しました！");
			}
		}
		
		
		/*関数登録*/
		addOnloadEvent(entryChange);
		addOnloadEvent(doneDialog);
		
		/*画面開いたときに行う関数の登録をする関数*/
		function addOnloadEvent(fnc){  
			if ( typeof window.addEventListener != "undefined" ){
				window.addEventListener( "load", fnc, false );  
			}else if ( typeof window.attachEvent != "undefined" ){  
			    window.attachEvent( "onload", fnc );  
			}
		}	
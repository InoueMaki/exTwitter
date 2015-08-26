		
		document.write("<script type='text/javascript' src='routine-error-check.js'></script>");
		
		//日付を指定する入力フォームを追加する関数
		var i = 1; //i-1 = 入力フォームを追加した回数 
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
	
		//追加した要素を削除する関数
		function removeElement(id) { 
			var element = document.getElementById(id);
			var tbl = document.getElementsByTagName("tbody").item(1);
			tbl.removeChild(element); 	
		} 

		
		//「曜日指定」と「日付指定」で表示切替する関数
		function entryChange(){
			radio = document.getElementsByName('entryPlan');
			if(radio[0].checked) {
				document.getElementById('week').style.display = "";
				document.getElementById('month').style.display = "none";

			}else if(radio[1].checked) {
				document.getElementById('week').style.display = "none";
				document.getElementById('month').style.display = "";

			}else if(radio[2].checked){
				document.getElementById('week').style.display = "none";
				document.getElementById('month').style.display = "none";
				
			}
		}
		
		
		//////入力情報確認ダイアログを表示する関数
		function tweet(){
			var days=[];
			radio = document.getElementsByName('entryPlan');
			
			//ツイート周期の文字列を生成する。
			//ラジオボタンによる表示切替に合わせてダイアログに表示する文字列を切り替える。
			if(radio[0].checked){
				
				//指定曜日の受け取り
				var strDays="指定した曜日\n";
				var dayList=["","月","火","水","木","金","土","日"];
				
				for(var j=1;j<=7;j++){
					if(document.getElementById("chk"+j).checked){
						days[days.length]=dayList[j];
					}
				}
			}else if(radio[1].checked){	
				//指定日付の受け取り//
				var strDays="指定した日付\n";
				for(var j=0;j<i;j++){
					if(document.getElementById("num"+j)) {
						days[days.length]=document.getElementById("num"+j).value;
					}
				}
				if(document.getElementById("monthend").checked){
					days[days.length]="月末"
				}
			}else{
				var strDays="指定した日付\n月末のみ";
			}
			//データから文字列生成
			for(var j=0;j<days.length;j++){
				//文字の間にコンマを挟むための処理
				if(j!=0){
					strDays=strDays+",";
				}
				strDays=strDays+days[j];
			}
			//ツイート周期の文字列の生成終わり
			
			//ダイアログに表示する文字列の生成
			var strDialog="ツイートの内容は以下でよろしいですか？\n\n\n"+
					"タイトル\n"+document.getElementById("title").value+"\n\n"+
					"本文\n"+document.getElementById("text").value+"\n\n"+
					"開始日\n"+document.getElementById("start_year").value+"年"+document.getElementById("start_month").value+"月"+document.getElementById("start_day").value+"日\n\n"+
					"終了日\n"+document.getElementById("end_year").value+"年"+document.getElementById("end_month").value+"月"+document.getElementById("end_day").value+"日\n\n"+
					"時刻\n"+document.getElementById("tweet_hour").value+"時"+document.getElementById("tweet_minute").value+"分\n\n"+
					strDays;
					
					return confirm(strDialog);
			}
			//////入力情報確認ダイアログ関数終わり
		
		
		//「平日」にチェックが入った時の処理
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
		

		//////エラーチェックし、エラーない時ツイート登録関数を呼び出す関数
		function errorCheck(){
			
			var noError = 1;
			var errorMessage = "入力内容に誤りがあります。\n以下の項目を修正してください。\n\n";
			
			//入力項目取得
			var title = document.getElementById("title").value;
			var text = document.getElementById("text").value;
			var startYear = document.getElementById("start_year").value;
			var startMonth = document.getElementById("start_month").value;
			var startDay = document.getElementById("start_day").value;
			var endYear = document.getElementById("end_year").value;
			var endMonth = document.getElementById("end_month").value;
			var endDay = document.getElementById("end_day").value;
			var hour = document.getElementById("tweet_hour").value;
			var minute = document.getElementById("tweet_minute").value;
			
			//各項目にエラーがあるかチェック
			
			if(hasNoValueError(title,text)){
				errorMessage = errorMessage + "・入力されていない項目があります。\n　記入してください\n\n";
				noError = 0;
			}
			if(hasTextError(text)){
				errorMessage = errorMessage + "・登録できない文字列が含まれています。\n　登録できない文字列は「RT」「#」「@」\n";
				errorMessage = errorMessage + "　先頭に記述できない文字列は「d」「m」「dm」「D」「M」「DM」です。\n\n";
				noError = 0;
			} 
			if(hasDateError1(endYear,endMonth,endDay)){
				errorMessage = errorMessage + "・「期間」の終了が現在より過去になっています。\n\n";
				noError = 0;
			}
			if(hasDateError2(startYear,startMonth,startDay,endYear,endMonth,endDay)){
				errorMessage = errorMessage + "・「期間」の開始が終了よりも過去または同じになっています。\n\n";
				noError = 0;
			}
			if(hasDateError3(startYear,startMonth,startDay)){
				errorMessage = errorMessage + "・「期間」の開始で存在しない月日を入力しています。\n\n";
				noError = 0;
			}
			if(hasDateError3(endYear,endMonth,endDay)){
				errorMessage = errorMessage + "・「期間」の終了で存在しない月日を入力しています。\n\n";
				noError = 0;
			}
			if(hasTimeError(hour,minute)){
				errorMessage = errorMessage + "・不正な時間を入力しています。\n\n";
				noError = 0;
			}
			
			if(radio[0].checked){//曜日指定 or 日付指定
				if(hasWeekSelectError()){
					errorMessage = errorMessage + "・曜日が選択されていません。\n\n";
					noError = 0;
				}
				
			}else if(radio[1].checked){
				if(hasOverlapError()){
					errorMessage = errorMessage + "・日付指定に被りがあります。\n\n";
					noError = 0;
				}
			}
			
			
			if(noError==1){
				 return tweet();
			}else{
				alert(errorMessage);
				return false;
			}
		}
		//////エラーチェック関数終わり
		
		//登録完了ダイアログ
		function doneDialog(){
			var flg=document.getElementById("flg").value;
			if(flg==1){
				alert("登録完了しました！");
			}
		}
		
		
		//関数登録
		addOnloadEvent(entryChange);
		addOnloadEvent(doneDialog);
		
		//画面開いたときに行う関数の登録をする関数
		function addOnloadEvent(fnc){  
			if ( typeof window.addEventListener != "undefined" ){
				window.addEventListener( "load", fnc, false );  
			}else if ( typeof window.attachEvent != "undefined" ){  
			    window.attachEvent( "onload", fnc );  
			}
		}	

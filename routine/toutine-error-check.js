//ラジオボタンが「曜日指定」、かつ、チェックボックスに何もない時のチェック
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
			
//禁止文字列がないか
function hasTextError(){
}

//期間の終了が現在より過去でないか
function hasDateError1(){
}

//期間の終了が開始より過去でないか
function hasDateError2(){
}

//期間の月日が存在しないものになっていないか
function hasDateError3(){

}

//日付指定で重複がないか
function hasOverlapError(){

}

//曜日指定がされているか
function hasWeekSelectError(){

}

//禁止文字列がないか
function hasTextError(text){
	
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

//期間の終了が現在より過去でないか
function hasDateError1(endYear,endMonth,endDay){
	
	var now = new Date();
	var inputDate = new Date(endYear,endMonth,endDay,23,59,59);
	
	if(now.getTime() > inputDate.getTime()){
				return true;//終了が現在よりも過去になっている
			}
			else{
				return false;
			}
}

//期間の終了が開始より過去でないか
function hasDateError2(startYear,startMonth,startDay,endYear,endMonth,endDay){

	var startDate = new Date(startYear,startMonth,startDay,23,59,59);
	var endDate = new Date(endYear,endMonth,endDay,23,59,59);
	
	if(startDate.getTime() >= endDate.getTime()){
				return true;//終了が開始よりも過去になっている
			}
			else{
				return false;
			}
}


//存在しない日付かどうかチェック(この関数は拾ったもの、参考：http://www.hoge256.net/2007/08/64.html)
function hasDateError3(year, month, day) {

	var dt = new Date(year, month - 1, day);
	if(dt == null || dt.getFullYear() != year || dt.getMonth() + 1 != month || dt.getDate() != day) {
		return true;//存在しない日付あり
	}
	return false;
}


//日付指定で重複がないか
function hasOverlapError(){

	var days = document.getElementsByName("days");

	var flg = 0;
	
	for(var k=0; k<days.length-1; k++){
		for(var j=k+1; j<days.length; j++){
			if(days[k].value==days[j].value){
				return true;
			}
		}
	}
	
	return false;
}


//曜日指定がされているか
function hasWeekSelectError(){

	var radio = document.getElementsByName('entryPlan');
	
	if(radio[0].checked){
		for(var j=1;j<=7;j++){
			if(document.getElementById("chk"+j).checked){
				return false;
			}
		}
		
			return true;//曜日指定がない
		
	}else{
		return false;
	}
	return false;
}

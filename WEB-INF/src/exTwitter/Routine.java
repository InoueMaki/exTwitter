package exTwitter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.http.HttpSession;

public class Routine {

	/**
	 * 定期ツイートの情報を取得し、sessionに保存
	 * @param session
	 */
	public void getRoutineBean(HttpSession session) {
		// 定期ツイート一覧の情報取得
		ArrayList<RoutineBean> tweetList = new ArrayList<RoutineBean>();
		tweetList = getRoutineTweet();

		// beanをセッション情報に保存
		session.setAttribute("tweetList", tweetList);
	}


	/**
	 * DBに登録されている有効な定期ツイートの一覧を取得
	 * DBに接続できなかったとき「null」が返る。
	 * @return　ツイート情報一覧のArrayList
	 */
	private ArrayList<RoutineBean> getRoutineTweet() {

		// 必要なインスタンスを生成
		ArrayList<RoutineBean> twt = new ArrayList<RoutineBean>();
		DBManager dbm = new DBManager();
		Date date = new Date();
		SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");

		// データべースの操作開始

		String qry = "select * from routine where posted=0 and end_date>'"
			+ sdfDate.format(date) + "'";

		try {
			// DBコネクションの確立
			dbm.getConnection("excite");

			// 定期ツイートのリザルトセットを取得
			ResultSet rs = dbm.getResultSet(qry);

			// リザルトセットをアレイリストに直す
			int i = 0;
			while (rs.next()) {
				// アレイリストにBeanのインスタンスをadd
				twt.add(new RoutineBean());
				// addされたインスタンスに検索結果の各要素を代入
				twt.get(i).setRoutineId(rs.getInt("routine_id"));
				twt.get(i).setTitle(rs.getString("title"));
				twt.get(i).setText(rs.getString("text"));
				twt.get(i).setPosted(rs.getInt("posted"));
				twt.get(i).setStartDate(rs.getString("start_date"));
				twt.get(i).setEndDate(rs.getString("end_date"));
				twt.get(i).setPostTime(rs.getString("post_time").substring(0,5));
				i++;
			}

		} catch (SQLException e) {
			e.printStackTrace();
			twt = null;

		}finally{
			dbm.closeConnection();
		}

		return twt;
	}


	/**
	 * routineTBLに定期ツイートを登録する。
	 * 例外発生時はfalseを返す。
	 * @(定期ツイート情報)
	 * @param title
	 * @param text
	 * @param startYear
	 * @param startMonth
	 * @param startDay
	 * @param endYear
	 * @param endMonth
	 * @param endDay
	 * @param tweetHour
	 * @param tweetMinute
	 * @param entryPlan
	 * @param monthEnd
	 * @param weekdaysStr
	 * @param daysStr
	 */
	public boolean insertRoutineTweet(String title, String text, String startYear,
			String startMonth, String startDay, String endYear,
			String endMonth, String endDay, String tweetHour,
			String tweetMinute, String entryPlan, String monthEnd,
			String[] weekdaysStr, String[] daysStr) {

		String[] strDays;

		//weekdayStrかdaysStrのどちらかしか使わないため、使う方をstrDaysにコピー
		if(entryPlan.compareTo("hoge1")==0){
			strDays = weekdaysStr;
		}else{
			strDays = daysStr;
		}

		//曜日や日付をint型で扱いたいので、変換してアレイリストへ
		ArrayList<Integer> intDays = new ArrayList<Integer>();
		for(int i=0; i<strDays.length;i++){
			if(strDays[i]!=null){
				try {
					intDays.add(Integer.parseInt(strDays[i]));
				} catch (NumberFormatException e) {
					e.printStackTrace();
					return false;
				}
			}
		}

		//「月末」にチェックが入っているときその情報追加
		if(monthEnd!=null && entryPlan.compareTo("hoge2")==0){
			intDays.add(0);
		}

		//numberingTBLからID取得
		int routineID = getRoutineId();

		//クエリ生成
		String qry = "INSERT INTO routine VALUES("+routineID+",'"+
		text+"',"+0+",'"+startYear+"-"+formatStr(startMonth)+"-"+formatStr(startDay)+
		"','"+endYear+"-"+formatStr(endMonth)+"-"+formatStr(endDay)+"','"+
		formatStr(tweetHour)+":"+formatStr(tweetMinute)+":00','"+title+"')";

		boolean bool;

		DBManager dbm = new DBManager();
		try {

			dbm.getConnection("excite");

			//クエリ実行
			int count = dbm.exeUpdate(qry);
			System.out.println(qry);
			System.out.println(count);

			//ラジオボタンの状況によって挿入すべきツイート周期の情報が異なる。
			//情報の種類によって、操作するテーブルを決める。（monthlyTBL or weeklyTBL）
			if(entryPlan.compareTo("hoge1")==0){
				insertIntoWeekly(routineID,intDays);
			}else{
				insertIntoMonthly(routineID,intDays);
			}

			bool = true;

		} catch (SQLException e) {
			e.printStackTrace();
			bool = false;

		}finally{
			dbm.closeConnection();
		}

		return bool;
	}


	/**
	 * routineIdを取得する
	 * DBに接続できなかったとき -1 を返す。
	 * @return routineId
	 */
	private int getRoutineId() {

		int routineId = -1;

		DBManager dbm = new DBManager();

		try {

			dbm.getConnection("excite");

			ResultSet rs = dbm.getResultSet("SELECT * FROM numbering");

			while(rs.next()){
				routineId = rs.getInt("routine_id");
			}

			dbm.exeUpdate("UPDATE numbering SET routine_id="+(routineId+1));

		} catch (SQLException e) {
			e.printStackTrace();
			routineId = -1;

		}finally{
			dbm.closeConnection();
		}

		return routineId;
	}

	/**
	 * 指定された定期ツイートに対応する日付をmonthlyTBLに挿入する。
	 * 失敗時はfalseを返す。
	 * @param routineID
	 * @param days
	 */
	private boolean insertIntoMonthly(int routineID,ArrayList<Integer> days) {

		String qry = "INSERT INTO monthly values";

		for(int i=0;i<days.size();i++){
			qry = qry+"("+routineID+","+days.get(i)+")";
			if(i!=days.size()-1){//最後以外はコンマ挿入
				qry = qry+",";
			}
		}

		System.out.println(qry);

		boolean bool;
		DBManager dbm = new DBManager();

		try {
			dbm.getConnection("excite");

			int count = dbm.exeUpdate(qry);
			System.out.println(count);

			bool = true;

		} catch (SQLException e) {
			e.printStackTrace();
			bool = false;

		}finally{
			dbm.closeConnection();
		}

		return bool;
	}

	/**
	 * 指定された定期ツイートに対応する曜日をweeklyTBLに挿入する。
	 * @param routineID
	 * @param days
	 */
	private boolean insertIntoWeekly(int routineID,ArrayList<Integer>  days) {

		String qry = "INSERT INTO weekly values";

		//クエリ編集
		for(int i=0;i<days.size();i++){
			qry = qry+"("+routineID+","+days.get(i)+")";
			if(i!=days.size()-1){//最後以外はコンマ挿入
				qry = qry+",";
			}
		}

		System.out.println(qry);

		boolean bool;
		DBManager dbm = new DBManager();

		try {
			dbm.getConnection("excite");

			int count=dbm.exeUpdate(qry);
			System.out.println(count);

			bool = true;

		} catch (SQLException e) {
			e.printStackTrace();
			bool = false;

		}finally{
			dbm.closeConnection();
		}

		return bool;
	}

	/**
	 * 指定された定期ツイートを削除する
	 * @param routineId
	 */
	public boolean deleteRoutineTweet(String routineId) {

		//postedを1に変更(検索にひっかからなくするため)
		String qry = "UPDATE routine SET posted = 1 where routine_id = "+ routineId;

		System.out.println("qry");

		boolean bool;
		DBManager dbm = new DBManager();

		try {
			dbm.getConnection("excite");

			int count = dbm.exeUpdate(qry);
			System.out.println(count);

			bool = true;

		} catch (SQLException e) {
			e.printStackTrace();
			bool = false;

		}finally{
			dbm.closeConnection();
		}

		return bool;
	}

	/**
	 * DBに書き込むために月日や時間の文字列を修正する。<br>
	 * 1ケタのものを2ケタにする。他はそのまま。<br><br>
	 * 例：<br> 1→01　 5→05　 12→12<br>
	 * @param str
	 * @return ２ケタのstr
	 */
	private static String formatStr(String str) {
		if(str.length()==1){
			return "0"+str;
		}else{
			return str;
		}
	}
}

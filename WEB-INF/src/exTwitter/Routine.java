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

		System.out.println("\ngetRoutineBean()\n");

		// 必要なインスタンスを生成
		ArrayList<RoutineBean> tweetList = new ArrayList<RoutineBean>();
		DBManager dbm = new DBManager();
		Date date = new Date();
		SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");

		try {
			dbm.getConnection();

			//仮クエリ
			dbm.createPreparedStatement( "select * from routine where posted=0 and end_date>?");
			//仮クエリ補完
			dbm.setString(1,sdfDate.format(date));

			// 定期ツイートのリザルトセットを取得
			ResultSet rs = dbm.getRSByPreSmt();

			// リザルトセットをアレイリストに直す
			while (rs.next()) {
				RoutineBean rBean = new RoutineBean();
				rBean.setRoutineId(rs.getInt("routine_id"));
				rBean.setTitle(rs.getString("title"));
				rBean.setText(rs.getString("text"));
				rBean.setPosted(rs.getInt("posted"));
				rBean.setStartDate(rs.getString("start_date"));
				rBean.setEndDate(rs.getString("end_date"));
				rBean.setPostTime(rs.getString("post_time").substring(0,5));

				tweetList.add(rBean);
			}

			// beanをセッション情報に保存
			session.setAttribute("tweetList", tweetList);

		} catch (SQLException e) {
			e.printStackTrace();
			tweetList = null;

		}finally{
			dbm.closeConnection();
		}

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

		System.out.println("\ninsertRoutineTweet()\n");

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
		int routineId = getRoutineId();

		boolean bool;

		DBManager dbm = new DBManager();
		try {
			dbm.getConnection();

			//日付・時間を表すStringのフォーマット修正
			String startDate = startYear+"-"+formatStr(startMonth)+"-"+formatStr(startDay);
			String endDate = endYear+"-"+formatStr(endMonth)+"-"+formatStr(endDay);
			String tweetTime = formatStr(tweetHour)+":"+formatStr(tweetMinute)+":00";

			//仮クエリ作成
			String qry = "INSERT INTO routine VALUES(?,?,0,?,?,?,?)";
			dbm.createPreparedStatement(qry);

			//仮クエリ補完
			dbm.setInt(1,routineId);
			dbm.setString(2,text);
			dbm.setString(3,startDate);
			dbm.setString(4,endDate);
			dbm.setString(5,tweetTime);
			dbm.setString(6,title);

			//クエリ実行
			dbm.exeUpdateByPreSmt();

			//ラジオボタンの状況によって挿入すべきツイート周期の情報が異なる。
			//情報の種類によって、操作するテーブルを決める。（monthlyTBL or weeklyTBL）
			if(entryPlan.compareTo("hoge1")==0){
				insertIntoWeekly(routineId,intDays);
			}else{
				insertIntoMonthly(routineId,intDays);
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

		System.out.println("\ngetRoutineId()\n");

		int routineId = -1;

		DBManager dbm = new DBManager();

		try {
			dbm.getConnection();

			dbm.createPreparedStatement("SELECT * FROM numbering");
			ResultSet rs = dbm.getRSByPreSmt();

			while(rs.next()){
				routineId = rs.getInt("routine_id");
			}

			//仮クエリ
			dbm.createPreparedStatement("UPDATE numbering SET routine_id=?");
			//クエリ補完
			dbm.setInt(1,routineId+1);

			dbm.exeUpdateByPreSmt();

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

		System.out.println("\ninsertIntoMonthly()\n");

		boolean bool;
		DBManager dbm = new DBManager();

		try {
			dbm.getConnection();

			/*String qry = "INSERT INTO monthly values";

			for(int i=0;i<days.size();i++){
				qry = qry+"("+routineID+","+days.get(i)+")";
				if(i!=days.size()-1){//最後以外はコンマ挿入
					qry = qry+",";
				}
			}*/

			//仮クエリ
			String qry = "INSERT INTO monthly values(?,?)";
			dbm.createPreparedStatement(qry);
			//クエリ補完
			dbm.setInt(1,routineID);
			for(int i=0;i<days.size();i++){
				dbm.setInt(2, days.get(i));
				dbm.addBatch();
			}

			dbm.exeBatch();

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

		System.out.println("\ninsertIntoWeekly()\n");

		boolean bool;
		DBManager dbm = new DBManager();

		try {
			dbm.getConnection();

			/*String qry = "INSERT INTO weekly values";

			//クエリ編集
			for(int i=0;i<days.size();i++){
				qry = qry+"("+routineID+","+days.get(i)+")";
				if(i!=days.size()-1){//最後以外はコンマ挿入
					qry = qry+",";
				}
			}*/

			//仮クエリ
			String qry = "INSERT INTO weekly values(?,?)";
			dbm.createPreparedStatement(qry);
			//クエリ補完
			for(int i=0;i<days.size();i++){
				dbm.setInt(1,routineID);
				dbm.setInt(2, days.get(i));
				dbm.addBatch();
			}

			dbm.exeBatch();

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

		System.out.println("\ndeleteRoutineTweet()\n");

		boolean bool;
		DBManager dbm = new DBManager();

		try {
			dbm.getConnection();

			//仮クエリ
			dbm.createPreparedStatement("UPDATE routine SET posted = 1 where routine_id = ?");
			//仮クエリ補完
			dbm.setString(1,routineId);

			dbm.exeUpdateByPreSmt();

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

package exBot;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import exTwitter.DBManager;

public class ExBot {

	// main関数
	public static void main(String[] args) {

		// ツイート本文格納
		ArrayList<String> tweets = new ArrayList<String>();

		// 予定時刻
		long search = System.currentTimeMillis();

		// 時間フォーマット指定
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		// 開始時刻表示
		System.out.println("startTime = " + sdf.format(search));

		// BOT無限ループ
		while (true) {
			// 現在時刻よりも予定時間が過去にある
			if (search < System.currentTimeMillis()) {
				System.out.println(sdf.format(search));
				tweets.clear();

				// 次回検索時間設定
				search += 1000 * 60 * 5;

				// 定期ツイート収集
				tweets.addAll(getRoutineTweet());

				// 単発ツイート収集・投稿済みフラグON
				tweets.addAll(getOnceTweet());

				// ツイート投稿
				Tweet tweet =new Tweet();
				int i;
				for (i = 0; i < tweets.size(); i++) {
					System.out.println("tweets\t" + tweets.get(i));
					tweet.sendTweet(tweets.get(i));
				}
			}
		}

	}

	// 定期ツイート収集
	public static ArrayList<String> getRoutineTweet() {
		ArrayList<String> tweets = new ArrayList<String>();

		// 今日の日時
		java.sql.Date date = new java.sql.Date(System.currentTimeMillis());

		Calendar cal = Calendar.getInstance();
		// 今日の週
		int week = cal.get(Calendar.DAY_OF_WEEK);
		// 今日の日
		int day = cal.get(Calendar.DATE);

		// 過去5分さかのぼって収集
		Time start_time = new Time(System.currentTimeMillis() - 1000 * 60 * 5);
		Time end_time = new Time(System.currentTimeMillis());

		// クエリの生成
		String monthly = "routine inner join monthly on routine.routine_id=monthly.routine_id ";
		String weekly = "routine inner join weekly  on routine.routine_id=weekly.routine_id  ";
		String span = "start_date <='" + date + "' && end_date >= '" + date
				+ "' && posted=0";
		String time = "post_time >= '" + start_time + "' && post_time <'"
				+ end_time + "'";

		String qry1 = "SELECT text FROM " + monthly + "WHERE (" + span
				+ "&& day=" + day + " && " + time + ");";
		
		// 月末用クエリ（day=0）
		String qry2 = "SELECT text FROM " + monthly + "WHERE (" + span
		+ "&& day=0 && " + time + ");";
		
		String qry3 = "SELECT text FROM " + weekly + "WHERE (" + span
				+ "&& day=" + week + " && " + time + ");";

		// コネクション確立
		DBManager DBM = new DBManager();
		DBM.getConnection();

		// monthly
		ResultSet rs1 = DBM.getResultSet(qry1);
		try {
			while (rs1.next()) {
				tweets.add(rs1.getString("text"));
			}
		} catch (SQLException e) {
			System.err.println("定期ツイート検索できません");
		}
		
		//monthly月末
		if(isLastDay()){
			ResultSet rs2 = DBM.getResultSet(qry2);
			try {
				while (rs2.next()) {
					tweets.add(rs2.getString("text"));
				}
			} catch (SQLException e) {
				System.err.println("定期ツイート検索できません");
			}
		}
		
		// weekly
		ResultSet rs3 = DBM.getResultSet(qry3);
		try {
			while (rs3.next()) {
				tweets.add(rs3.getString("text"));
			}
		} catch (SQLException e) {
			System.err.println("定期ツイート検索できません");
		}

		// コネクション切断
		DBM.closeConnection();

		return tweets;
	}

	// 単発ツイート収集
	public static ArrayList<String> getOnceTweet() {
		ArrayList<String> tweets = new ArrayList<String>();
		ArrayList<Integer> ids = new ArrayList<Integer>();
		
		// 時間フォーマット指定
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		// 予定時刻
		String now = sdf.format(System.currentTimeMillis());

		
		
		// クエリ作成
		String qry1 = "SELECT text,once_id from once where posted =0 && reserve_time < '"
				+ now + "';";
		// コネクション確立
		DBManager DBM = new DBManager();
		DBM.getConnection();
		ResultSet rs1 = DBM.getResultSet(qry1);
		try {
			while (rs1.next()) {
				tweets.add(rs1.getString("text"));
				ids.add(rs1.getInt("once_id"));
			}
		} catch (SQLException e) {
			System.err.println("単発ツイート検索できません");
		}

		// 投稿完了に更新
		String conf = "";
		int i;
		// IDから条件を作成
		for (i = 0; i < ids.size() - 1; i++) {
			conf += "once_id=" + ids.get(i) + "||";
		}
		// IDが取得されない場合偽
		if (i < ids.size()) {
			conf += "once_id=" + ids.get(i);

			// クエリ作成
			String qry2 = "UPDATE once set posted=1 where " + conf + ";";

			// 更新
			int count = DBM.exeUpdate(qry2);

			System.out.println(count + "件を投稿済みにしました");
		}
		// コネクション切断
		DBM.closeConnection();

		return tweets;
	}
	
	
	private static boolean isLastDay(){
		// Calendar型インスタンス
		Calendar cal = Calendar.getInstance();
		// 月末
	    int last =cal.getActualMaximum(Calendar.DATE);
	    // 今日
	    int day  = cal.get(Calendar.DATE);
	    // 一致するときtrueを返す
	    if(last==day)return true;
		return false;
	}
}

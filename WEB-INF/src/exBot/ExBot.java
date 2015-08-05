package exBot;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import exTwitter.DBManager;

/**
 * DBから投稿条件に該当するツイートを収集しツイートする
 * 
 * @author excite
 * 
 */
public class ExBot {

	/**
	 * <b>main</b><br/>
	 * public static void main(String[] args)<br/>
	 * <blockquote> BOT本体<br>
	 * 5分おきに検索条件に該当するツイートをDBから収集し TwitterAPIに投稿する </blockquote>
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		// ツイート本文格納
		ArrayList<String> tweets = new ArrayList<String>();
		// 投稿予定時刻
		long search = System.currentTimeMillis();
		// 時間フォーマット指定
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		// プログラム稼働時刻表示
		System.out.println("startTime = " + sdf.format(search));

		// ツイッターAPI接続
		Tweet tweet = new Tweet();

		// BOT無限ループ
		while (true) {
			
			// 条件：現在時刻よりも投稿予定時間が過去にある
			if (search < System.currentTimeMillis()) {
				// 検索開始をコンソール表示
				System.out.println("-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-");
				// 投稿予定時刻表示
				System.out.println("searchTime = "+sdf.format(search));
				// ツイート本文格納クリア
				tweets.clear();

				// 次回検索時間を5分後に設定
				search += 1000 * 60 * 5;

				// 定期ツイート収集
				tweets.addAll(getRoutineTweet());
				// 単発ツイート収集・投稿済みフラグON
				tweets.addAll(getOnceTweet());

				// 収集ツイート全件投稿
				for (int i = 0; i < tweets.size(); i++) {
					System.out.println("tweets\t" + tweets.get(i));
					tweet.sendTweet(tweets.get(i));
				}
			}
		}

	}

	// 定期ツイート収集
	private static ArrayList<String> getRoutineTweet() {
		ArrayList<String> tweets = new ArrayList<String>();

		// 現在のの日時(sql.Date)
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
		String span = "start_date <='" + date + "' and end_date >= '" + date
				+ "' and posted=0";
		String time = "post_time >= '" + start_time + "' and post_time <'"
				+ end_time + "'";

		// 日付指定クエリ
		String qry1 = "SELECT text FROM " + monthly + "WHERE (" + span
				+ " and day=" + day + " and " + time + ");";
		// 日付指定月末用クエリ（day=0）
		String qry2 = "SELECT text FROM " + monthly + "WHERE (" + span
				+ " and day=0 and " + time + ");";
		// 曜日指定クエリ
		String qry3 = "SELECT text FROM " + weekly + "WHERE (" + span
				+ " and day=" + week + " and " + time + ");";

		// コネクション確立
		DBManager DBM = new DBManager();
		try {
			DBM.getConnection("excite.db");
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// monthly
		try {
			ResultSet rs1;
			if (!isLastDay()) {
				// 通常monthly
				rs1 = DBM.getResultSet(qry1);
			} else {
				// 月末monthly
				rs1 = DBM.getResultSet(qry2);
			}
			while (rs1.next()) {
				tweets.add(rs1.getString("text"));
			}
		} catch (SQLException e) {
			System.err.println("定期ツイート検索できません");
			e.printStackTrace();
		}

		// weekly
		try {
			ResultSet rs2 = DBM.getResultSet(qry3);
			while (rs2.next()) {
				tweets.add(rs2.getString("text"));
			}
		} catch (SQLException e) {
			System.err.println("定期ツイート検索できません");
			e.printStackTrace();
		}

		// コネクション切断
		DBM.closeConnection();
		return tweets;
	}

	// 単発ツイート収集
	private static ArrayList<String> getOnceTweet() {
		ArrayList<String> tweets = new ArrayList<String>();
		ArrayList<Integer> ids = new ArrayList<Integer>();

		// 時間フォーマット指定
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		// 予定時刻
		String now = sdf.format(System.currentTimeMillis());

		// クエリ作成
		String qry1 = "SELECT text,once_id from once where (posted =0 and reserve_time < '"
				+ now + "');";
		// コネクション確立
		DBManager DBM = new DBManager();
		try {
			DBM.getConnection("excite.db");
		} catch (SQLException e) {
			e.printStackTrace();
		}

		try {
			ResultSet rs1 = DBM.getResultSet(qry1);
			while (rs1.next()) {
				tweets.add(rs1.getString("text"));
				ids.add(rs1.getInt("once_id"));
			}
		} catch (SQLException e) {
			System.err.println("単発ツイート検索できません");
			e.printStackTrace();
		}

		// 収集したツイートを投稿完了に更新
		// 更新用条件文の作成

		int i;
		String conf = "";
		// idsから条件を作成(全体-1件まで)
		for (i = 0; i < ids.size() - 1; i++) {
			conf += "once_id=" + ids.get(i) + " or ";
		}
		// 1件以上のidが取得される(条件文'or'のため最後の1件のみこちらで対応)
		if (i < ids.size()) {
			conf += "once_id=" + ids.get(i);

			// クエリ作成
			String qry2 = "UPDATE once set posted=1 where " + conf + ";";

			// 更新
			int count = 0;
			try {
				count = DBM.exeUpdate(qry2);
			} catch (SQLException e) {
				e.printStackTrace();
			}

			System.out.println(count + "件を投稿済みにしました");
		}
		// コネクション切断
		DBM.closeConnection();

		return tweets;
	}

	/**
	 * <b>isLastDay</b><br>
	 * private static boolean isLastDay()<br/>
	 * <blockquote> 今日が月末かどうかを返す </blockquote>
	 * 
	 * @return 月末:true その他:false
	 */
	private static boolean isLastDay() {
		// Calendar型インスタンス
		Calendar cal = Calendar.getInstance();
		// 月末
		int last = cal.getActualMaximum(Calendar.DATE);
		// 今日
		int day = cal.get(Calendar.DATE);
		// 一致するときtrueを返す
		if (last == day)
			return true;
		return false;
	}
}

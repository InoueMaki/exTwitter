package exTwitter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import exTwitter.persist.PersistenceException;
import exTwitter.persist.QueryRunner;
import exTwitter.persist.ResultMapper;

/**
 * 単発ツイートのサービスクラス. {@link Once}の書き換え版サンプル
 * 
 * @author Hiroaki Ohtake
 */
public class OnceService {

	/**
	 * 単発ツイート一覧を表示するためのBeanを作る。 作成したBeanはセッション情報に保存する<br>
	 * DBに接続できなかったときfalseを返す。
	 * 
	 * @param session
	 */
	public boolean getOnceBean(HttpSession session) {
		String query = "select * from once where posted = 0";
		List<OnceBean> onceList = QueryRunner.select(query, new ResultMapper<OnceBean>() {
			@Override
			public OnceBean convert(ResultSet rs) throws SQLException {
				OnceBean data = new OnceBean();
				data.setOnceId(rs.getInt("once_id"));
				// サブストリングを取っているのは、「秒」を表示させないようにするため
				data.setReserveTime(rs.getString("reserve_time").substring(0, 16));
				data.setPosted(rs.getInt("posted"));
				data.setText(rs.getString("text"));
				return data;
			}
		});
		if (onceList.isEmpty()) {
			return false;
		}
		// よくわかんないけど、もともとセッションに保存されてなかったような..
		return true;
	}

	/**
	 * DBに単発ツイートを登録する。 DBに接続できなかったときfalseを返す。
	 * 
	 * @param （入力された単発ツイートの情報）
	 * @param text
	 * @param chk
	 * @param year
	 * @param month
	 * @param day
	 * @param hour
	 * @param minute
	 * 
	 * MEMO: パラメータ多すぎるので、入力された情報をBeanにまとめたほうがいい
	 */
	public boolean insertOnceTweet(String text, String chk, String year, String month, String day, String hour,
			String minute) {

		// String qry = "";
		int onceId = getOnceIdFromDB();
		String reserveTime = "";

		// 時間指定があるかないか
		if (chk == null) {
			Date date = new Date();
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			reserveTime = sdf1.format(date);

		} else {
			// reserveTime = year + "-" + formatStr(month) + "-" + formatStr(day) + " " + formatStr(hour) + ":" + formatStr(minute) + ":00";
			// String.format()で一発でいける
			reserveTime = String.format("%s-%s-%s %s:%s:00", year, formatStr(month), formatStr(day), formatStr(hour), formatStr(minute));
		}

		// qry = new String(
		//		"insert into once values (" + onceId + ", '" + text + "', '" + reserveTime + "', " + 0 + " ); ");
		// new Stringは基本的につかわない
		// なんでかっていうと毎回メモリ確保処理が入るから(たぶん最近はJVMが最適化するけど)、普通に""で宣言したほうがいい
		
		// クエリ組み立てもString.format()が楽
		// # ほんとはプリペアードクエリ使うべき
		String query = String.format("insert into once values (%d, '%s', '%s', 0)", onceId, text, reserveTime); 
		System.out.println(query); // デバッグ用

		int count = QueryRunner.update(query);
		System.out.println("onceInsertCount:" + count);
		return true;
	}
	

	/**
	 * DBに単発ツイートを登録する.<br>
	 * 登録したツィートの数を返す(0なら登録失敗)
	 * 
	 * @param onceFrom HTMLのフォームから入力された単発ツィートの情報
	 */
	public int insertOnceTweetFromForm(OnceForm onceForm) {

		int onceId = getOnceIdFromDB();
		String reserveTime = "";

		// 時間指定があるかないか
		if (onceForm.getCheck() == null) {
			Date date = new Date();
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			reserveTime = sdf1.format(date);
		} else {
			reserveTime = String.format("%s-%s-%s %s:%s:00", onceForm.getYear(), formatStr(onceForm.getMonth()),
					formatStr(onceForm.getDay()), formatStr(onceForm.getHour()), formatStr(onceForm.getMinute()));
		}

		String query = String.format("insert into once values (%d, '%s', '%s', 0)", onceId, onceForm.getText(), reserveTime); 

		int count = QueryRunner.update(query);
		System.out.println("onceInsertCount:" + count);
		return count;
	}

	/**
	 * DBからonceIdを取得する。 DBに接続できなかったとき -1 を返す。
	 * 
	 * @return onceId
	 */
	private int getOnceIdFromDB() {
		List<Integer> onceIds = QueryRunner.select("SELECT * FROM numbering", new ResultMapper<Integer>() {
			@Override
			public Integer convert(ResultSet rs) throws SQLException {
				return rs.getInt("once_id");
			}
		});
		// 取得できなかった場合-1を返して終了
		if (onceIds.isEmpty()) {
			return -1;
		}
		int onceId = onceIds.get(0);
		// 更新処理
		QueryRunner.update(String.format("UPDATE numbering SET once_id=%d", onceId + 1));
		return onceId;
	}

	/**
	 * 指定された単発ツイートをDBから削除する。 DBに接続できなかったときfalseを返す。
	 * 
	 * @param onceId
	 */
	public boolean deleteOnceTweet(String onceId) {


		// postedを1に変更(検索にひっかからなくするため)
		String query = String.format("UPDATE once SET posted = 1 where once_id = %s", onceId);
		System.out.println(query);
		// DBエラーのハンドリングのサンプル
		// 基本的にSQLExceptionとかは真面目にやるとtry-catchばかりになる(てかなってる)ので、
		// 非キャッチ例外(RuntimeExceptionのサブクラス)で投げ返してキャッチしなくてもOKなようにしとくのがいいっす
		try {
			int count = QueryRunner.update(query);
			System.out.println(count);
		} catch (PersistenceException e) {
			System.err.println("DBでなにかエラーがあったよ");
			System.err.println(e.getMessage());
			return false;
		}
		return true;
	}

	/**
	 * DBに書き込むために月日や時間の文字列を修正する。<br>
	 * 1ケタのものを2ケタにする。他はそのまま。<br>
	 * <br>
	 * 例：<br>
	 * 1→01 5→05 12→12<br>
	 * 
	 * @param str
	 * @return ２ケタのstr
	 * @author matsuda
	 */
	private static String formatStr(String str) {
		if (str.length() == 1) {
			return "0" + str;
		} else {
			return str;
		}
	}
	
	/**
	 * フォームからの値を受け取るBean.<br>
	 * めんどいのでここに書くけどちゃんとやるときは別クラスに分ける.<br>
	 * Beanはeclipseですぐgetterやsetter,toStringやhashCode生成できるし気軽に作っていい感じ
	 * @author hiroaki
	 *
	 */
	class OnceForm {
		private String text;
		private String check;
		private String year;
		private String month;
		private String day;
		private String hour;
		private String minute;
		
		public String getText() {
			return text;
		}
		public void setText(String text) {
			this.text = text;
		}
		public String getCheck() {
			return check;
		}
		public void setCheck(String check) {
			this.check = check;
		}
		public String getYear() {
			return year;
		}
		public void setYear(String year) {
			this.year = year;
		}
		public String getMonth() {
			return month;
		}
		public void setMonth(String month) {
			this.month = month;
		}
		public String getDay() {
			return day;
		}
		public void setDay(String day) {
			this.day = day;
		}
		public String getHour() {
			return hour;
		}
		public void setHour(String hour) {
			this.hour = hour;
		}
		public String getMinute() {
			return minute;
		}
		public void setMinute(String minute) {
			this.minute = minute;
		}
	}
}

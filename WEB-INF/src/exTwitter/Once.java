package exTwitter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.http.HttpSession;

public class Once {

	public static ArrayList<OnceBean> onceList;	//DB情報格納クラスを格納するリスト
	
	public void getOnceBean(HttpSession session) {
		
			onceList = new ArrayList<OnceBean>();	//onceListを初期化
			DBManager DBM = new DBManager();	//DB接続クラスのインスタンス
			ResultSet rs = DBM.getResultSet("select * from once where posted = 0");	//クエリを投げた結果のResultSet取得

			try {
				while(rs.next()){	//resultSetの最後まで繰り返す
					OnceBean data = new OnceBean();	//DB情報格納クラス
					data.setOnceId(rs.getInt("once_id"));	//once_idの取得
					data.setReserveTime(rs.getString("reserve_time").substring(0,16));	//reserve_timeの取得
					data.setPosted(rs.getInt("posted"));	//postedの取得
					String text = rs.getString("text");
					data.setText(text);	//textの取得
					onceList.add(data);	//Listに要素追加
				}
			} catch (SQLException e) {
				System.err.println("error:getOnceBean()");
				e.printStackTrace();
			}
			
			DBM.closeConnection();
	
	}
	

	public void insertOnceTweet(String text, String chk, String year,
			String month, String day, String hour, String minute) {
		/*引数を加工してDBの登録する。
		 * 出来たSQL文をDBManagerに投げる。
		 * window.onloadの関係でフラグが欲しいときはここで作ってセッションに保存*/
		String qry = "";
		int onceId = getOnceIdFromDB();
		String reserveTime = "";
		
		//時間指定があるかないか
		if(chk==null){
			Date date = new Date();
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			reserveTime = sdf1.format(date);
		}else{
			reserveTime = year + "-" + formatStr(month) + "-" + formatStr(day) +" " + formatStr(hour) + ":" + formatStr(minute) + ":00";
		}
		
		qry = new String("insert into once values (" + onceId + ", '" + text + "', '" + reserveTime + "', " + 0 + " ); ");
		System.out.println(qry);	//デバッグ用

		DBManager dbm = new DBManager();
		int count=0;
		count = dbm.exeUpdate(qry);
		System.out.println("onceInsertCount:"+count);

	}
	

	private int getOnceIdFromDB() {
		
		int onceId = -1;
		
			DBManager dbm = new DBManager();
			ResultSet rs = dbm.getResultSet("SELECT * FROM numbering");
			
			try {
				while(rs.next()){
					onceId = rs.getInt("once_id");
				}
			} catch (SQLException e) {
				e.printStackTrace();
				System.err.println("error:getOnceIdFromDB()");
			}
			dbm.exeUpdate("UPDATE numbering SET once_id="+(onceId+1));

		return onceId;
	}
	

	public void deleteOnceTweet(String onceId) {
		/*引数で指定されたツイートをDBから削除(posted変更)*/
		//DB接続の準備
		DBManager dbm = new DBManager();
		dbm.getConnection();
		//postedを1に変更(検索にひっかからなくするため)
		String qry = "UPDATE once SET posted = 1 where once_id = "+ onceId;
		System.out.println(qry);
		int count = dbm.exeUpdate(qry);
		
		System.out.println(count);
		
		//接続クローズ
		dbm.closeConnection();
	}
	
	
	private static String formatStr(String str) {
		if(str.length()==1){
			return "0"+str;
		}else{
			return str;
		}
	}
}

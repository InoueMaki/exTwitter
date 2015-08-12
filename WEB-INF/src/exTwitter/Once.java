package exTwitter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.http.HttpSession;

/**
 * @author matsuda
 * 単発ツイートのクラス
 *
 */
public class Once {

	public static ArrayList<OnceBean> onceList;	//DB情報格納クラスを格納するリスト

	/**
	 * 単発ツイート一覧を表示するためのBeanを作る。
	 * 作成したBeanはセッション情報に保存する。
	 * DBに接続できなかったときfalseを返す。
	 * @param session
	 */
	public boolean getOnceBean(HttpSession session) {

		System.out.println("\ngetOnceBean()\n");

		onceList = new ArrayList<OnceBean>();	//onceListを初期化

		DBManager dbm = new DBManager();	//DB接続クラスのインスタンス

		boolean bool =false;

		try {

			dbm.getConnection();

			dbm.createPreparedStatement("select * from once where posted = 0");
			ResultSet rs = dbm.getRSByPreSmt();	

			while(rs.next()){	//resultSetの最後まで繰り返す
				OnceBean data = new OnceBean();
				data.setOnceId(rs.getInt("once_id"));
				data.setReserveTime(rs.getString("reserve_time").substring(0,16));	//サブストリングを取っているのは、「秒」を表示させないようにするため
				data.setPosted(rs.getInt("posted"));
				data.setText(rs.getString("text"));
				onceList.add(data);//リストに要素を追加
			}

			bool = true;

		}catch(SQLException e){
			e.printStackTrace();

		}finally{
			dbm.closeConnection();
		}

		return bool;
	}


	/**
	 * DBに単発ツイートを登録する。
	 * DBに接続できなかったときfalseを返す。
	 * @param （入力された単発ツイートの情報）
	 * @param text
	 * @param chk
	 * @param year
	 * @param month
	 * @param day
	 * @param hour
	 * @param minute
	 */
	public boolean insertOnceTweet(String text, String chk, String year,
			String month, String day, String hour, String minute) {

		System.out.println("\ninsertOnceTweet()\n");

		boolean bool = false;
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


		DBManager dbm = new DBManager();

		try {
			dbm.getConnection();

			//仮クエリ
			dbm.createPreparedStatement("insert into once values (?,?,?,0)");

			//仮クエリを補完
			dbm.setInt(1, onceId);
			dbm.setString(2, text);
			dbm.setString(3, reserveTime);

			dbm.exeUpdateByPreSmt();

			bool = true;

		} catch (SQLException e) {
			e.printStackTrace();

		}finally{
			dbm.closeConnection();
		}

		return bool;
	}


	/**
	 * DBからonceIdを取得する。
	 * DBに接続できなかったとき -1 を返す。
	 * @return onceId
	 */
	private int getOnceIdFromDB() {

		System.out.println("\ngetOnceId()\n");

		int onceId = -1;

		DBManager dbm = new DBManager();

		try{
			dbm.getConnection();

			dbm.createPreparedStatement("SELECT * FROM numbering");
			
			for(int i=0;i<5;i++){//once_idの更新を試行回数5回でトライ
				try {
					ResultSet rs = dbm.getRSByPreSmt();

					while(rs.next()){
						onceId = rs.getInt("once_id");
					}

					//仮クエリ
					dbm.createPreparedStatement("UPDATE numbering SET once_id=?");
					//仮クエリ補完
					dbm.setInt(1,onceId+1);

					dbm.exeUpdateByPreSmt();

					System.out.println("success 'update once_id\n'");
					break;

				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("try again 'update once_id\n'");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			onceId = -1;

		}finally{
			dbm.closeConnection();
		}

		return onceId;
	}


	/**
	 * 指定された単発ツイートをDBから削除する。
	 * DBに接続できなかったときfalseを返す。
	 * @param onceId
	 */
	public boolean deleteOnceTweet(String onceId) {

		System.out.println("\ndeleteOnceTweet()\n");

		boolean bool = false;

		//DB接続の準備
		DBManager dbm = new DBManager();

		try{
			dbm.getConnection();

			//仮クエリ
			dbm.createPreparedStatement("UPDATE once SET posted = 2 where once_id =?");
			//仮クエリ補完
			dbm.setString(1, onceId);

			dbm.exeUpdateByPreSmt();

			bool = true;

		}catch(SQLException e){
			e.printStackTrace();

		}finally{
			dbm.closeConnection();
		}

		return bool;
	}

	/**
	 * DBに書き込むために月日や時間の文字列を修正する。<br>
	 * 1ケタのものを2ケタにする。他はそのまま。<br><br>
	 * 例：<br>
	 * 1→01　
	 * 5→05　
	 * 12→12<br>
	 * @param str
	 * @return ２ケタのstr
	 * @author matsuda
	 */
	private static String formatStr(String str) {
		if(str.length()==1){
			return "0"+str;
		}else{
			return str;
		}
	}
}

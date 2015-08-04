package exTwitter;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.http.HttpSession;

import exTwitter.DBManager;

/**
 * @author matsuda
 * Userクラス。<br>
 * ログインメソッド、ログアウトメソッドがある。<br>
 * メインメソッドでは、ハッシュ値のパスワードテストユーザーを登録できる。
 *
 */
public class User {

	public boolean login(String userName, String password) {

		Boolean bool = false;
		
		//passwordのハッシュ値計算
		Hash hash = new Hash();
		String hashPW = hash.hash(password);
		
		// クエリ生成
		String qry = "SELECT * from user where " + "(user_name = '" + userName
				+ "' " + "and password = '" + hashPW + "');";
		
		try {
			// DB接続
			DBManager dbm = new DBManager();
			
			dbm.getConnection("excite");

			ResultSet rs = dbm.getResultSet(qry);
			
			// 該当ユーザ存在
			if (rs.next()) {
				bool = true;
			}
			
			// コネクション切断
			dbm.closeConnection();
			
		} catch (Exception e) {
			System.err.println("login Error");
		}
		return bool;
	}

	public void logout(HttpSession session) {
		if(null != session){
			session.invalidate();
			System.out.println("-------------session.invalidate-------------");
		}
	}
	
	//テストユーザー登録用。PWハッシュ対応。
	//userName,passwordを入力してから実行すること。
	public static void main(String[] args) {
		
		String userName = "aiueo";
		String password = "aiueo";
		
		Hash hash = new Hash();
		String hashPW =  hash.hash(password);
		
		DBManager dbm = new DBManager();
		
		try {
			dbm.getConnection("C:/servletbook/apache-tomcat/excite");
			
			String qry = "INSERT INTO user VALUES(121,'"+hashPW+"',1,'"+userName+"')";
			
			dbm.exeUpdate(qry);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}

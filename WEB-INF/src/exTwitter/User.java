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

	/**
	 * ユーザー認証とセッション開始をする
	 * @param userName
	 * @param password
	 * @return
	 */
	public boolean login(String userName, String password) {
		System.out.println("\nlogin()\n");
		//passwordのハッシュ値計算
		Hash hash = new Hash();
		String hashPW = hash.hash(password);
		
		boolean bool;
		DBManager dbm = new DBManager();
		
		try {
			dbm.getConnection();
			
			//仮クエリ
			dbm.createPreparedStatement("SELECT * from user where (user_name = ? and password = ?)");
			//仮クエリ補完
			dbm.setString(1,userName);
			dbm.setString(2,hashPW);
			
			ResultSet rs = dbm.getRSByPreSmt();
			
			if (rs.next()) {//ユーザーが存在する時
				bool = true;
				System.out.println("\nlogin success");
			}else{//存在しない時
				bool = false;
				System.out.println("\nlogin denied");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			bool = false;
			
		}finally{
			dbm.closeConnection();
		}
		
		return bool;
	}

	/**
	 * ログアウト処理としてセッションを無効にする
	 * @param session
	 */
	public void logout(HttpSession session) {
		System.out.println("\nlogout()");
		if(null != session){
			session.invalidate();
			System.out.println("SessionInvalidate\n");
		}
	}
	
	//ユーザー登録用。PWハッシュ対応。
	//userName,passwordを入力してから実行すること。
	public void signup(String userName, String password) {
		
		Hash hash = new Hash();
		String hashPW =  hash.hash(password);
		
		DBManager dbm = new DBManager();
		
		try {
			dbm.getConnection();
			dbm.createPreparedStatement("SELECT max(user_id) from user");
			
			ResultSet rs = dbm.getRSByPreSmt();
			
			int userId = 0;
			while(rs.next()){
				userId = rs.getInt("max(user_id)");
			}
			
			System.out.println("userID : "+userId);
			
			//仮クエリ
			dbm.createPreparedStatement("INSERT INTO user VALUES(?,?,1,?)");
			//仮クエリ補完
			dbm.setInt(1,userId+1);
			dbm.setString(2, hashPW);
			dbm.setString(3, userName);
			
			dbm.exeUpdateByPreSmt();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}

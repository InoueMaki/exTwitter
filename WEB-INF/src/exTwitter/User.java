package exTwitter;

import java.sql.ResultSet;

import javax.servlet.http.HttpSession;

import exTwitter.DBManager;

public class User {

	public boolean login(String userName, String password) {

		Boolean bool = false;
		
		//passwordのハッシュ値計算
		//Hash hash = new Hash();
		//String hashedPass = hash.hash(password);
		
		// クエリ生成
		String qry = "SELECT * from user where " + "(user_name = '" + userName
				+ "' " + "and password = '" + password + "');";
		
		try {
			// DB接続
			DBManager dbm = new DBManager();
			dbm.getConnection();
			
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
		}
	}
	
}

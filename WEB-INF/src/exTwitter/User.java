package exTwitter;

import java.sql.ResultSet;

import javax.servlet.http.HttpSession;

import exTwitter.DBManager;

public class User {

	public boolean login(String userName, String password) {
		/*ログインできるかチェック（ユーザー名、パスワード照合）
		 * そうじゃないならfalseをリターン。
		 * 「request.setAttribute("err","ユーザ名とパスワードが一致しません");」は、Controller内で行う。
		 * */
		Boolean bool = false;
		// セレクト文クエリ生成
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
			rs.close();
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

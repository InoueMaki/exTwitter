package exTwitter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBManager {
	// イニシャライザ
	private Connection con;
	private Statement smt;
	private ResultSet rs;
	private boolean ret = false;	// commit or rollback
	{
		String url = "jdbc:mysql://localhost/excite";
		String user = "root";
		String password = "excite";
		// 旧 getConnection ＋ オートコミットオフ
		while (true) {

			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(url, user, password);
				con.setAutoCommit(false);
				smt = con.createStatement();
				break;
			} catch (Exception e) {
				try {
					Thread.sleep(1000);
					System.err.println("Now Sleeping");
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}

		}
	}

	// getConnection(やらなくていいよ)
	public void getConnection() {
		String url = "jdbc:mysql://localhost/excite";
		String user = "root";
		String password = "excite";
		while (true) {
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(url, user, password);
				con.setAutoCommit(false);
				smt = con.createStatement();
				break;
			} catch (Exception e) {
				try {
					Thread.sleep(1000);
					System.err.println("Now Sleeping");
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		}
	}

	/*
	 * input : Select文のクエリ output: ResultSet
	 */
	public ResultSet getResultSet(String qry) {
		try {
			qry = qry.trim();
			if (qry.endsWith(";")){
				qry=qry.substring(0, qry.length()-1);
			}
			qry+=" for update;";
			rs = smt.executeQuery(qry);

		} catch (Exception e) {
			System.err.println("Select_Error");
		}
		return rs;
	}

	/*
	 * input : Update文クエリ or Insert文クエリ output: 更新行数
	 */
	public int exeUpdate(String qry) {
		int update_count = 0;
		try {
			update_count = smt.executeUpdate(qry);
		} catch (Exception e) {
			System.err.println("Update_Error");
		}
		if (update_count > 0){
			ret = true;
		}
		commit();
		return update_count;
	}

	// commit or rollback 
	public void commit() {
		try {
			if (ret == true) {
				con.commit();
			} else {
				System.out.println("rollback");
				con.rollback();
			}
			con.setAutoCommit(false);
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("commit_error");
		}
	}

	// コネクション切断
	public void closeConnection() {
		try {
			rs.close();
		} catch (Exception e) {
			// なにもしない
		}
		try {
			smt.close();
			con.close();
		} catch (Exception e) {
			System.err.println("Close_Error");
		}
	}
	
	@Override
	protected void finalize(){
		System.err.println("Instance destroy");
		try {
			rs.close();
		} catch (Exception e) {
			// なにもしない
		}
		try {
			smt.close();
			con.close();
		} catch (Exception e) {
			System.err.println("Close_Error");
		}
	}
}

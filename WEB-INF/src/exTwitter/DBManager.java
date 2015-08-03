package exTwitter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * <b>DBManager</b><br/>
 * public class DBManager<br/>
 * <blockquote>
 * SQLiteとのデータのやり取りを行う
 * </blockquote>
 * @author sato
 */
public class DBManager {
	private Connection con = null;
	private Statement smt = null;
	private ResultSet rs = null;
	private String  db_base = "";
	
	static{		
		try {
			Class.forName("org.sqlite.JDBC");
			System.out.println("Driver Load");
		} catch (ClassNotFoundException e) {
			System.err.println("Driver Load Error\n");
			e.printStackTrace();
		}
	}
	
	/**
	 * <b>DBManager</b><br/>
	 * public DBManager()<br/>
	 * <blockquote>
	 * コンストラクタ<br>
	 * プロパティファイルのロードを行う
	 * </blockquote>
	 */
	public DBManager() {
		final Properties config = new Properties();
		System.out.println("Property Load");
		try {
			// プロパティファイルの読み出し
			InputStream inputStream = new FileInputStream(new File(
					"excite.properties"));
			config.load(inputStream);
			// プロパティファイル内の変数読み出し
			db_base = config.getProperty("db_base");
			System.out.println("\tproterty: db_base='"+db_base+"'");
		} catch (IOException e) {
			System.err.println("Property Load Error\n");
			e.printStackTrace();
		}
	}

	/**
	 * <b>getConnection</b><br/>
	 * public void getConnection() throws SQLException<br/>
	 * <blockquote>
	 * コネクション生成<br>
	 * DB: sample にアクセスを行う<br>
	 * 最大3回までトライする
	 * </blockquote>
	 * @throws SQLException
	 */
	public void getConnection() throws SQLException{
		for (int i = 0; i < 3; i++) {
			try {
				this.con = DriverManager.getConnection(this.db_base+"sample");
				this.smt = con.createStatement();
				System.out.println("Create Connection");
				break;
			} catch (SQLException e) {
				System.err.println("Connection Error");
				if (i < 2) {
					System.err.println("\tTry Again");
				} else {
					System.err.println("\n");
					throw e;
				}
			}
		}
	}

	/**
	 * <b>getConnection</b><br/>
	 * public void getConnection(String DB_name) throws SQLException<br/>
	 * <blockquote>
	 * コネクション生成<br>
	 * DB名を指定しアクセスを行う<br>
	 * 最大3回までトライする
	 * </blockquote>
	 * @param データベース名
	 * @throws SQLException
	 */
	public void getConnection(String DB_name) throws SQLException {
		for (int i = 0; i < 3; i++) {
			try {
				this.con = DriverManager
						.getConnection(this.db_base + DB_name);
				this.smt = con.createStatement();
				System.out.println("Create Connection");
				break;
			} catch (SQLException e) {

				System.err.println("Connection Error");
				if (i < 2) {
					System.err.println("\tTry Again");
				} else {
					System.err.println("\n");
					throw e;
				}
			}
		}
	}

	/**
	 * <b>createTable</b><br/>
	 * public void createTable(String sql) throws SQLException<br/>
	 * <blockquote>
	 * テーブル名の生成を行う<br>
	 * すでに存在する場合例外を返す
	 * </blockquote>
	 * @param SQLクエリ
	 * @throws SQLException
	 */
	public void createTable(String sql) throws SQLException {
		System.out.print("\ttry '" + sql + "'");
		smt.execute(sql);
		System.out.println("...O.K.");
	}

	/**
	 * <b>dropTable</b><br/>
	 * public void dropTable(String sql)<br/>
	 * <blockquote>
	 * テーブルの削除を行う<br>
	 * 削除に失敗した場合スタックトレースを出力する
	 * </blockquote>
	 * @param SQLクエリ
	 */
	public void dropTable(String sql) {
		System.out.print("\ttry '" + sql + "'\t");
		try {
			smt.execute(sql);
			System.out.println("... O.K.");
		} catch (SQLException e) {
			System.err.println("\nDrop Error\tqry:'" + sql + "'\n");
			e.printStackTrace();
		}
	}

	/**
	 * <b>exeUpdate</b><br/>
	 * public int exeUpdate(String sql)<br/>
	 * <blockquote>
	 * 要素の更新・削除を行う<br>
	 * 更新・削除に失敗した場合スタックトレースを出力する
	 * </blockquote>
	 * @param SQLクエリ
	 * @return int <該当行数>
	 */
	public int exeUpdate(String sql) {
		int count = 0;
		System.out.print("\ttry '" + sql + "'\t");
		try {
			// データの挿入
			count = smt.executeUpdate(sql);
			System.out.println("... O.K.");
		} catch (SQLException e) {
			System.err.println("\nUpdate Error\tqry:'" + sql + "'\n");
			e.printStackTrace();
		}
		return count;
	}

	/**
	 * <b>getResultSet</b><br/>
	 * public ResultSet getResultSet(String sql)<br/>
	 * <blockquote>
	 * 要素の取得を行う<br>
	 * 取得に失敗した場合スタックトレースを出力する
	 * </blockquote>
	 * @param SQLクエリ
	 * @return ResultSet
	 */
	public ResultSet getResultSet(String sql) {
		System.out.print("\ttry '" + sql + "'\t");
		try {
			this.rs = smt.executeQuery(sql);
			System.out.println("... O.K.");
		} catch (SQLException e) {
			System.err.println("\nSelect Error\tqry:'" + sql + "'\n");
			e.printStackTrace();
		}
		return this.rs;
	}

	/**
	 * <b>closeConnection</b><br/>
	 * public void closeConnection()<br/>
	 * <blockquote>
	 * コネクション・ステートメントの切断を行う<br>
	 * リザルトセットが生成されていた場合切断を行う<br>
	 * コネクション・ステートメント切断に失敗した場合スタックトレースを出力する
	 * </blockquote>
	 */
	public void closeConnection() {
		System.out.println("Closing...");
		// ResultSetの切断
		if (null != this.rs) {
			try {
				this.rs.close();
				System.out.println("\tResultSet  Closed");
			} catch (SQLException e) {
				// Select文が一度も実行されていない
			}
		}
		
		// StatementとConnectionの切断
		try {
			this.smt.close();
			System.out.println("\tStatement  Closed");
			this.con.close();
			System.out.println("\tConnection Closed");
		} catch (SQLException e) {
			System.err.println("\tClose Error\n");
			e.printStackTrace();
		}
		System.out.println("Closed\n");
	}
}

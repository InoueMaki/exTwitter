package exTwitter.persist;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * <h3>DBとのコネクションを管理するクラス.</h3>
 * コネクションはシンプルにリクエストごとに毎回生成&クローズして使い捨てにする
 * @author Hiroaki Ohtake
 */
public class ConnectionManager {
	private static String jdbcUrl;
	/**
	 * プライベートコンストラクタ<br>
	 * 複数スレッドからコネクション管理が行われないように、インスタンスを生成できないようにする
	 */
	private ConnectionManager() {
	}

	static {
		loadJdbcDriver();
		jdbcUrl = "jdbc:sqlite:excite";
	}

	/**
	 * DBとのコネクションを生成して返す.
	 * <p>synchronizedで同期ロックを取り、複数スレッドから同時にコネクション要求を受けないようにする</p>
	 * @return コネクション
	 * @throws PersistenceException コネクション生成失敗時
	 */
	public static synchronized Connection getConnection() {
		try {
			return DriverManager.getConnection(jdbcUrl);
		} catch (SQLException e) {
			throw new PersistenceException("コネクションの生成に失敗.", e);
		}
	}

	/**
	 * Jdbcドライバを読み込み.
	 */
	private static void loadJdbcDriver() {
		try {
			Class.forName("org.sqlite.JDBC");
			System.out.println("Driver Load");
		} catch (ClassNotFoundException e) {
			System.err.println("Driver Load Error\n");
			e.printStackTrace();
		}
	}
}

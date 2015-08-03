package exTwitter.persist;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * クエリを実行する.
 * 
 * @author Hiroaki Ohtake
 */
public class QueryRunner {
	/** プライベートコンストラクタ */
	private QueryRunner() {}
	
	/**
	 * <h3>select文を実行し、Beanのリストに詰め替えて返す</h3>
	 * <p>サンプルコード
	 * <pre>
	 * class User {
	 *     String name;
	 *     int year;
	 * }
	 * </pre>
	 * こんな感じのBeanがあるとします。(privateとかgetterとかはめんどいので省略)
	 * <pre>
	 * select("select name, year from users", new ResultMapper<User>(ResultSet rs) {
	 *     User user = new User();
	 *     user.setName(rs.getString("name"));
	 *     user.setYear(rs.getInt("year"));
	 *     return user;
	 * });
	 * </pre>
	 * これでList<User>に詰め替えて、select文の結果を返します。
	 * 見つからなかった場合、空のリストを返します。
	 * </p>
	 * 
	 * @param <T> 詰め替え先のBean
	 * @param query クエリ文字列
	 * @param mapper {@link ResultSet}からBeanへの変換マッピング
	 */
	public static <T> List<T> select(String query, ResultMapper<? extends T> mapper) {
		Connection connection = ConnectionManager.getConnection();
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			resultSet = statement.executeQuery(query);
			// 結果用のリスト
			List<T> results = new ArrayList<T>();
			while (resultSet.next()) {
				results.add(mapper.convert(resultSet));
			}
			return results;
		} catch (SQLException e) {
			throw new PersistenceException(e);
		} finally {
			try {
				// StatementをcloseするとResultSetもcloseするはずなのでサボる
				// 生成自体に失敗してnullの可能性があるため、close前にちゃんとnullチェックしなくてはならない
				if (statement != null) {
					statement.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e1) {
				throw new PersistenceException(e1);
			}
		}
		// ちなみに上のクソみたいなコード、Java6じゃなくてJava7をつかうと、
		// try-with-resource文で自動closeってのがつかえるので、
		//
		// try (Connection connection = ConnectionManager.getConnection();
		//   Statement statement = connecction.createConnection()) {
		//   ResultSet resultSet = statement.executeQuery(query);
		//   List<T> results = new ArrayList<T>();
		//   while (resultSet.next()) {
		//	   results.add(mapper.convert(resultSet));
		//   }
		//   return results;
		// } catch (SQLException e) {
		//   throw new PersistenceException(e);
		// }
		// このくらいには短くなってすっきりする
	}
	
	/**
	 * update文やinsert文を実行する.
	 * @param query クエリ本文
	 * @return 更新(追加)した行数
	 */
	public static int update(String query) {
		Connection connection = ConnectionManager.getConnection();
		Statement statement = null;
		try {
			statement = connection.createStatement();
			return statement.executeUpdate(query);
		} catch (SQLException e) {
			throw new PersistenceException(e);
		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e1) {
				throw new PersistenceException(e1);
			}
		}
	}
}

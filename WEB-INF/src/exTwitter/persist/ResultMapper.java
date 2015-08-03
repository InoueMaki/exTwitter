package exTwitter.persist;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * {@link ResultSet}から任意のBeanクラスへの変換インターフェース.
 * @param <T> 変換先Beanのクラス
 * @author Hiroaki Ohtake
 */
public interface ResultMapper<T> {
	/**
	 * {@link ResultSet}から任意のBeanクラスへの変換をおこなう.
	 * @param rs リザルトセット
	 * @return 変換先のBeanインスタンス
	 * @throws SQLException ResultSetからの取得に失敗した時
	 */
	T convert(ResultSet rs) throws SQLException;
}

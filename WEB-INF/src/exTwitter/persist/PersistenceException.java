package exTwitter.persist;

/**
 * <h3>DB層でなんらかのエラーがあった時に利用する例外.</h3>
 * 非チェック例外のため、基本的にはキャッチせず、例外を意図的にハンドリングしたい(なんかメッセージ出すとか)ところでのみキャッチすること
 * @author Hiroaki Ohtake
 */
@SuppressWarnings("serial")
public class PersistenceException extends RuntimeException {
	public PersistenceException(Throwable cause) {
		super(cause);
	}
	
	public PersistenceException(String message) {
		super(message);
	}

	public PersistenceException(String message, Throwable cause) {
		super(message, cause);
	}
}

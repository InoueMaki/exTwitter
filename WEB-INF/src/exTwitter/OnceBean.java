package exTwitter;

/**
 * DB情報を格納するビーンクラス
 *
 */
public class OnceBean {
	
	private int onceId;
	private String text;
	private String reserveTime;
	private int posted;

	
	
	public int getOnceId() {
		return onceId;
	}
	public void setOnceId(int onceId) {
		this.onceId = onceId;
	}
	public String getText() {
		return text;
	}
	public void setText(String text){
		if (text.length() > 25){
			this.text = text.substring(0,25) + "…";
		}
		else this.text = text;
	}
	public String getReserveTime() {
		return reserveTime;
	}
	public void setReserveTime(String reserveTime) {
		this.reserveTime = reserveTime;
	}
	public int getPosted() {
		return posted;
	}
	public void setPosted(int posted) {
		this.posted = posted;
	}

}

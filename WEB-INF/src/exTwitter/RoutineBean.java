package exTwitter;

public class RoutineBean {
	
	private int routineId;
	private String title;
	private String snippet;
	private String text;
	private int posted;
	private String startDate;
	private String endDate;
	private String postTime;
	
	//以下、ゲッターセッターメソッド
	public int getRoutineId() {
		return routineId;
	}
	public void setRoutineId(int routineId) {
		this.routineId = routineId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
		if(text.length()>7){
			snippet = text.substring(0,7)+"…";
		}else{
			snippet = text;
		}
	}
	public int getPosted() {
		return posted;
	}
	public void setPosted(int posted) {
		this.posted = posted;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getPostTime() {
		return postTime;
	}
	public void setPostTime(String postTime) {
		this.postTime =postTime;
	}
	public String getSnippet(){
		return snippet;
	}
	public void setSnippet(String snippet){
		this.snippet = snippet;
	}
	
	public void sysoutBean(){
		System.out.println(this.routineId);
		System.out.println(this.title);
		System.out.println(this.text);
		System.out.println(this.posted);
		System.out.println(this.startDate);
		System.out.println(this.endDate);
		System.out.println(this.postTime);
	}
}

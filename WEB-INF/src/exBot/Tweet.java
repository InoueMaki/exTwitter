package exBot;

import java.util.Calendar;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

/**
 * twitterAPIにアクセスし処理を行う
 * @author sato
 */
public class Tweet {
	private Twitter twitter;
	Calendar cal;
	/**
	 * <b>Tweet</b><br/>
	 * public Tweet()<br/>
	 * <blockquote> コンストラクタ<br>
	 * プロパティファイルのロードを行う </blockquote>
	 */
	public Tweet() {
		twitter = new TwitterFactory().getInstance();
	}
	
	/**
	 * <b>sendTweet</b><br/>
	 * public void sendTweet(String text)<br/>
	 * テキストをツイッターに投稿する
	 * @param text
	 */
	public void sendTweet(String text){
		
		cal= Calendar.getInstance();
		try{
			System.out.print("ツイート内容:");
			twitter.updateStatus(text);
			System.out.println("ツイート成功:" + cal.getTime().toString());
			
		} catch(TwitterException e){
			System.err.println("ツイート失敗："+ cal.getTime().toString());
			e.printStackTrace();
		}
	}
	
}

package exBot;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;


public class Tweet {
	
	//Twitterアカウント情報
	//customerKey
	//accessToken

	public void sendTweet(String text){
		
		Twitter twitter = new TwitterFactory().getInstance();
		twitter.setOAuthConsumer(consumerKey, consumerSecret);
		twitter.setOAuthAccessToken(new AccessToken(accessToken,accessTokenSecret));
		
		try{
			
			twitter.updateStatus(text);
			System.out.println("ツイート成功");
			
		} catch(TwitterException e){
			
			System.err.println("ツイート失敗："+text);
			System.err.println(e.getMessage());
		}
		
	}
}

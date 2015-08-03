package exTwitter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@SuppressWarnings("serial")
public class Controller extends HttpServlet {
	
	public void doPost(HttpServletRequest request,
						HttpServletResponse response)
						throws ServletException,IOException{
		
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/plain;charset=UTF-8");
		
		//セッション取得
		HttpSession session = request.getSession(false);
		
		//押されたボタンの情報を取得
		String command = request.getParameter("btn");
		
		String url="";
		
		//以下ボタンごとの処理
		if(command!=null && command .equals("ログイン")){
				String userName = request.getParameter("user_name");
				String password =request.getParameter("password");
				User user = new User();
				
				if(user.login(userName,password)){
					session = request.getSession(true);
					session.setAttribute("loginFlag", "login");
					url = "menuUI.jsp";
				}else{
					request.setAttribute("err","ユーザ名とパスワードが一致しません");
					url = "loginUI.jsp";
				}
			
		}else if(hasSession(session)){//ログイン以外、かつ、sessionある時
			
				if(command .equals("ログアウト")){
					User user = new User();
					user.logout(session);
					url = "loginUI.jsp";
				
				}else if(command .equals("メニュー")){
					url = "menuUI.jsp";
			
				}else if(command .equals("単発")){
					Once once = new Once();
					once.getOnceBean(session);
					url = "OnceUI.jsp";
			
				}else if(command .equals("単発登録")){
					String text = request.getParameter("text");
					String chk = request.getParameter("chk1");
					String year = request.getParameter("year");
					String month = request.getParameter("month");
					String day = request.getParameter("day");
					String hour = request.getParameter("hour");
					String minute = request.getParameter("minute");
				
					Once once = new Once();
					once.insertOnceTweet(text,chk,year,month,day,hour,minute);
					once.getOnceBean(session);
					
					session.setAttribute("onceflg", 1);
				
					url = "OnceUI.jsp";
			
				}else if(command .equals("単発削除")){
					String onceId = request.getParameter("del");
					System.out.println(onceId);
					Once once = new Once();
					once.deleteOnceTweet(onceId);
					once.getOnceBean(session);
					
					session.setAttribute("delflg", 1);
				
					url = "OnceDelUI.jsp";
			
				}else if(command .equals("定期")){
					Routine routine = new Routine();
					routine.getRoutineBean(session);
					
					url = "RoutineUI.jsp";
				
				}else if(command .equals("定期登録")){
					String title = request.getParameter("title");
					String text = request.getParameter("text");
					String startYear = request.getParameter("st_y");
					String startMonth = request.getParameter("st_m");
					String startDay = request.getParameter("st_d");
					String endYear = request.getParameter("end_y");
					String endMonth = request.getParameter("end_m");
					String endDay = request.getParameter("end_d");
					String tweetHour = request.getParameter("twt_h");
					String tweetMinute = request.getParameter("twt_m");
					String entryPlan = request.getParameter("entryPlan");
					String monthEnd = request.getParameter("monthend");
					String[] weekdays = request.getParameterValues("chk");
					String[] days = request.getParameterValues("days");
				
					Routine routine = new Routine();
					routine.insertRoutineTweet(title, text, startYear, startMonth,
						startDay, endYear, endMonth, endDay, tweetHour,
						tweetMinute, entryPlan, monthEnd, weekdays, days);
					routine.getRoutineBean(session);
					session.setAttribute("flg", 1);
				
					url = "RoutineUI.jsp";
				
				}else if(command .equals("定期削除")){
					String routineId = request.getParameter("del");
				
					Routine routine = new Routine();
					routine.deleteRoutineTweet(routineId);
					routine.getRoutineBean(session);
				
					url = "RoutinedelUI.jsp";
				
				}else{
					PrintWriter out = response.getWriter();
					out.println("正常に処理が行われませんでした。");
				}
			
		}else{//セッション切れの時
			url="redirect.jsp";
		}
		
		//画面遷移
		RequestDispatcher rdisp = request.getRequestDispatcher(url);
		rdisp.forward(request, response);
		
	}

	/**
	 * @param session
	 * @return セッションあり→true、なし→false
	 */
	private boolean hasSession(HttpSession session) {
		if(session.getAttribute("loginFlag")!=null){
			return true;
		}else{
			return false;
		}
	}
}
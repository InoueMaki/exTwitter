package exTwitter;

import java.io.IOException;
import java.util.Calendar;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author matsuda doPostメソッドでjspからデータの受け取り、次のjspに遷移を行うクラス。
 *         再読み込みをされた場合に同じ処理を2度行わないようにするため、遷移にはリダイレクトを使う。
 */
@SuppressWarnings("serial")
public class Controller extends HttpServlet {

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/plain;charset=UTF-8");

		// セッション取得
		HttpSession session = request.getSession(true);

		// 押されたボタンの情報を取得
		String command = request.getParameter("btn");
		System.out.println("command = " + command);

		String url = "";

		// 以下ボタンごとの処理
		if(command == null){
			url = "exTwitterTemplate/error.jsp";
		}
		else if (command.equals("ログイン")) {

			String userName = request.getParameter("user_name");
			String password = request.getParameter("password");
			User user = new User();

			if (user.login(userName, password)) {
				session = request.getSession(true);
				session.setAttribute("loginFlag", "login");
				url = "exTwitterTemplate/menu.jsp";
			} else {
				session.setAttribute("err", "ユーザ名とパスワードが一致しません");
				url = "user/login.jsp";
			}

		} else if (command.equals("ログアウト")) {
			User user = new User();
			user.logout(session);
			url = "user/login.jsp";

		} else if ( command.equals("ユーザー登録")) {
			String userName = request.getParameter("user_name");
			String password = request.getParameter("pass1");

			User user = new User();
			user.signup(userName, password);

			url = "user/login.jsp";

		} else if (session != null && hasSession(session)) {// ログイン以外、かつ、sessionある時

			if (command.equals("メニュー")) {
				url = "exTwitterTemplate/menu.jsp";

			} else if (command.equals("単発")) {
				Once once = new Once();
				once.getOnceBean(session);
				url = "once/once_form.jsp";

			} else if (command.equals("単発登録")) {
				String text = request.getParameter("text");
				String chk = request.getParameter("chk1");
				String year = request.getParameter("year");
				String month = request.getParameter("month");
				String day = request.getParameter("day");
				String hour = request.getParameter("hour");
				String minute = request.getParameter("minute");

				Once once = new Once();
				once.insertOnceTweet(text, chk, year, month, day, hour, minute);
				once.getOnceBean(session);

				session.setAttribute("onceflg", 1);

				url = "once/once_form.jsp";

			} else if (command.equals("単発削除")) {
				String onceId = request.getParameter("del");
				System.out.println(onceId);
				Once once = new Once();
				once.deleteOnceTweet(onceId);
				once.getOnceBean(session);

				url = "once/once_del.jsp";

			} else if (command.equals("定期")) {
				Routine routine = new Routine();
				routine.getRoutineBean(session);

				url = "routine/routine_form.jsp";

			} else if (command.equals("定期登録")) {
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

				url = "routine/routine_form.jsp";

			} else if (command.equals("定期削除")) {
				String routineId = request.getParameter("del");

				Routine routine = new Routine();
				routine.deleteRoutineTweet(routineId);
				routine.getRoutineBean(session);

				url = "routine/routine_del.jsp";
			} else if (command.equals("スケジュール")) {
				String strYear = request.getParameter("scheYear");
				String strMonth = request.getParameter("scheMonth");
				try {
					Scheduler sche = new Scheduler();
					if (strYear == null || strMonth == null) {
						Calendar cal = Calendar.getInstance();
						
						sche.setSchedule(session, cal.get(Calendar.YEAR),  cal.get(Calendar.MONTH)+1);
					}else{
						sche.setSchedule(session, Integer.parseInt(strYear), Integer.parseInt(strMonth));
					}
					url = "schedule/schedule.jsp";
				} catch (Exception e) {
					e.printStackTrace();
					url = "exTwitterTemplate/redirect.jsp";
				}
				// //////////////////////////////////////
				// 新しい画面が増えたときはここに追加//
				// //////////////////////////////////////

			} else {
				System.err.println("登録されていないボタン：" + command);
				url = "exTwitterTemplate/error.jsp";
			}

		} else {// セッション切れの時
			url = "exTwitterTemplate/redirect.jsp";
		}

		// 画面遷移
		response.sendRedirect(url);
	}

	//スマホで「PC版ページを表示」という操作をすると、
	//「GETメソッドがない」というエラーが出るから実装する。
	public void doGet(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/plain;charset=UTF-8");
		
		response.sendRedirect("exTwitterTemplate/error.jsp");
	}
	/**
	 * @param session
	 * @return セッションあり→true、なし→false
	 */
	private boolean hasSession(HttpSession session) {
		if (session.getAttribute("loginFlag") != null) {
			return true;
		} else {
			return false;
		}
	}
}
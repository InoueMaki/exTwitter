package exTwitter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;

import javax.servlet.http.HttpSession;

/**
 * <b>Scheduler</b><br/>
 * public class Scheduler<br/>
 * スケジュール作成に必要な情報をセッション格納する
 * 
 * @author excite
 * 
 */
public class Scheduler {
	/**
	 * カレンダー用日付配列
	 */
	public static int[] calendarDay = new int[42];
	/**
	 * カレンダー対応定期ツイートBeanリスト
	 */
	public static ArrayList<ArrayList<RoutineBean>> RoutineBeans = new ArrayList<ArrayList<RoutineBean>>();
	/**
	 * カレンダー対応単発ツイートBeanリスト
	 */
	public static ArrayList<ArrayList<OnceBean>> OnceBeans = new ArrayList<ArrayList<OnceBean>>();

	/**
	 * 
	 */
	public static int year;
	/**
	 * 表示月
	 */
	public static int month;

	/**
	 * <b>createSchedule</b><br/>
	 * public void createSchedule(HttpSession session)<br/>
	 * <blockquote> カレンダーで5週間分の日付を表示するための配列をセッションに格納する<br/>
	 * 日付に対応するDB登録情報の配列をセッションに格納する </blockquote>
	 * 
	 * @param session
	 */
	public synchronized void createSchedule(HttpSession session) {
		session.setAttribute("sche", "on");
		clearSchedule(session);
		Calendar calendar = Calendar.getInstance();
		Object objYear = session.getAttribute("year");
		Object objMonth = session.getAttribute("month");
		if ((objYear != null) && (objMonth != null)) {
			year = new Integer(objYear.toString()).intValue();
			month = new Integer(objMonth.toString()).intValue();
		} else {
			if (year == 0) {
				year = calendar.get(Calendar.YEAR);
			}
			if (month == 0) {
				month = calendar.get(Calendar.MONTH);
			}
		}
		// 1日をセット
		calendar.set(year, month, 1);
		int startWeek = calendar.get(Calendar.DAY_OF_WEEK);
		// 先月末日を取得
		calendar.set(year, month, 0);
		int beforeMonthlastDay = calendar.get(Calendar.DATE);

		// 今月末日を取得
		calendar.set(year, month + 1, 0);
		int thisMonthlastDay = calendar.get(Calendar.DATE);

		int count = 0;

		DBManager dbm = new DBManager();
		try {
			dbm.getConnection("excite.db");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		String sql;
		String presql1;
		String presql2;
		ResultSet rs;

		// 先月分の日付を格納
		for (int i = startWeek - 2; i >= 0; i--) {
			sql = String
					.format(
							"SELECT * FROM once WHERE reserve_time>='%04d-%02d-%02d 00:00:00' and reserve_time<='%04d-%02d-%02d 23:59:59' and posted<2",
							year, month, beforeMonthlastDay - i, year, month,
							beforeMonthlastDay - i);
			try {
				rs = dbm.getResultSet(sql);
				ArrayList<OnceBean> once = new ArrayList<OnceBean>();
				while (rs.next()) {
					OnceBean bean = new OnceBean();
					bean.setOnceId(rs.getInt("once_id"));
					bean.setText(rs.getString("text"));
					bean.setReserveTime(rs.getString("reserve_time"));
					bean.setPosted(rs.getInt("posted"));
					once.add(bean);
				}
				rs.close();
				OnceBeans.add(once);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			int week = (count + 1) % 7;
			if (week == 0) {
				week = 7;
			}
			presql1 = String.format(
					"SELECT routine_id FROM  weekly WHERE day=%d", week);
			if (i == 0) {
				presql2 = String.format(
						"SELECT routine_id FROM  monthly WHERE day=%d", 0);
			} else {
				presql2 = String.format(
						"SELECT routine_id FROM  monthly WHERE day=%d",
						beforeMonthlastDay - i);
			}
			ArrayList<Integer> ids = new ArrayList<Integer>();
			try {
				ArrayList<RoutineBean> routine = new ArrayList<RoutineBean>();
				rs = dbm.getResultSet(presql1);
				while (rs.next()) {
					ids.add(rs.getInt("routine_id"));
				}
				rs.close();
				rs = dbm.getResultSet(presql2);
				while (rs.next()) {
					ids.add(rs.getInt("routine_id"));
				}
				rs.close();
				for (int j = 0; j < ids.size(); j++) {
					sql = String
							.format(
									"SELECT * FROM routine WHERE start_date<='%04d-%02d-%02d' and end_date>='%04d-%02d-%02d' and routine_id=%d and posted=0",
									year, month, beforeMonthlastDay - i, year,
									month, beforeMonthlastDay - i, ids.get(j));
					rs = dbm.getResultSet(sql);

					while (rs.next()) {
						RoutineBean bean = new RoutineBean();
						bean.setRoutineId(rs.getInt("routine_id"));
						bean.setTitle(rs.getString("title"));
						bean.setText(rs.getString("text"));
						bean.setStartDate(rs.getString("start_date"));
						bean.setEndDate(rs.getString("end_date"));
						bean.setPosted(rs.getInt("posted"));
						routine.add(bean);
					}
					rs.close();

				}
				RoutineBeans.add(routine);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			calendarDay[count++] = beforeMonthlastDay - i;
		}

		// 今月分の日付を格納
		for (int i = 1; i <= thisMonthlastDay; i++) {
			sql = String
					.format(
							"SELECT * FROM once WHERE reserve_time>='%04d-%02d-%02d 00:00:00' and reserve_time<='%04d-%02d-%02d 23:59:59' and posted<2",
							year, month + 1, i, year, month + 1, i);
			try {
				rs = dbm.getResultSet(sql);
				ArrayList<OnceBean> once = new ArrayList<OnceBean>();
				while (rs.next()) {
					OnceBean bean = new OnceBean();
					bean.setOnceId(rs.getInt("once_id"));
					bean.setText(rs.getString("text"));
					bean.setReserveTime(rs.getString("reserve_time"));
					bean.setPosted(rs.getInt("posted"));
					once.add(bean);
				}
				rs.close();
				OnceBeans.add(once);
			} catch (SQLException e) {
				e.printStackTrace();
			}

			int week = (count + 1) % 7;
			if (week == 0) {
				week = 7;
			}
			presql1 = String.format(
					"SELECT routine_id FROM  weekly WHERE day=%d", week);
			if (i == thisMonthlastDay) {
				presql2 = String.format(
						"SELECT routine_id FROM  monthly WHERE day=%d", 0);
			} else {
				presql2 = String.format(
						"SELECT routine_id FROM  monthly WHERE day=%d", i);
			}
			ArrayList<Integer> ids = new ArrayList<Integer>();
			try {
				ArrayList<RoutineBean> routine = new ArrayList<RoutineBean>();
				rs = dbm.getResultSet(presql1);
				while (rs.next()) {
					ids.add(rs.getInt("routine_id"));
				}
				rs.close();
				rs = dbm.getResultSet(presql2);
				while (rs.next()) {
					ids.add(rs.getInt("routine_id"));
				}
				rs.close();
				for (int j = 0; j < ids.size(); j++) {
					sql = String
							.format(
									"SELECT * FROM routine WHERE start_date<='%04d-%02d-%02d' and end_date>='%04d-%02d-%02d' and routine_id=%d and posted=0",
									year, month + 1, i, year, month + 1, i, ids
											.get(j));
					rs = dbm.getResultSet(sql);
					while (rs.next()) {
						RoutineBean bean = new RoutineBean();
						bean.setRoutineId(rs.getInt("routine_id"));
						bean.setTitle(rs.getString("title").trim());
						bean.setText(rs.getString("text").trim());
						bean.setStartDate(rs.getString("start_date").trim());
						bean.setEndDate(rs.getString("end_date").trim());
						bean.setPosted(rs.getInt("posted"));
						routine.add(bean);
					}
					rs.close();

				}
				RoutineBeans.add(routine);
			} catch (SQLException e) {
				e.printStackTrace();
			}

			calendarDay[count++] = i;
		}

		// 翌月分の日付を格納
		int nextMonthDay = 1;
		while (count % 7 != 0) {
			sql = String
					.format(
							"SELECT * FROM once WHERE reserve_time>='%04d-%02d-%02d 00:00:00' and reserve_time<='%04d-%02d-%02d 23:59:59' and posted<2",
							year, month + 2, nextMonthDay, year, month + 2,
							nextMonthDay);
			try {
				rs = dbm.getResultSet(sql);
				ArrayList<OnceBean> once = new ArrayList<OnceBean>();
				while (rs.next()) {
					OnceBean bean = new OnceBean();
					bean.setOnceId(rs.getInt("once_id"));
					bean.setText(rs.getString("text"));
					bean.setReserveTime(rs.getString("reserve_time"));
					bean.setPosted(rs.getInt("posted"));
					once.add(bean);
				}
				rs.close();
				OnceBeans.add(once);
			} catch (SQLException e) {
				e.printStackTrace();
			}

			int week = (count + 1) % 7;
			if (week == 0) {
				week = 7;
			}
			presql1 = String.format(
					"SELECT routine_id FROM  weekly WHERE day=%d", week);
			presql2 = String.format(
					"SELECT routine_id FROM  monthly WHERE day=%d",
					nextMonthDay);
			ArrayList<Integer> ids = new ArrayList<Integer>();
			try {
				ArrayList<RoutineBean> routine = new ArrayList<RoutineBean>();
				rs = dbm.getResultSet(presql1);
				while (rs.next()) {
					ids.add(rs.getInt("routine_id"));
				}
				rs.close();
				rs = dbm.getResultSet(presql2);
				while (rs.next()) {
					ids.add(rs.getInt("routine_id"));
				}
				rs.close();
				for (int j = 0; j < ids.size(); j++) {
					sql = String
							.format(
									"SELECT * FROM routine WHERE start_date<='%04d-%02d-%02d' and end_date>='%04d-%02d-%02d' and routine_id=%d  and posted=0",
									year, month + 2, nextMonthDay, year,
									month + 2, nextMonthDay, ids.get(j));
					rs = dbm.getResultSet(sql);

					while (rs.next()) {
						RoutineBean bean = new RoutineBean();
						bean.setRoutineId(rs.getInt("routine_id"));
						bean.setTitle(rs.getString("title"));
						bean.setText(rs.getString("text"));
						bean.setStartDate(rs.getString("start_date"));
						bean.setEndDate(rs.getString("end_date"));
						bean.setPosted(rs.getInt("posted"));
						routine.add(bean);
					}
					rs.close();

				}
				RoutineBeans.add(routine);

			} catch (SQLException e) {
				e.printStackTrace();
			}

			calendarDay[count++] = nextMonthDay++;
		}
		dbm.closeConnection();

		session.setAttribute("calendarDay", calendarDay);
		session.setAttribute("OnceBeans", OnceBeans);
		session.setAttribute("RoutineBeans", RoutineBeans);
		for (int i = 0; i < OnceBeans.size(); i++) {
			int week = (i + 1) % 7;
			if (week == 0)
				week = 7;
			System.out.println(String.format("%02d日 %s曜日", calendarDay[i],
					int2week(week)));
			System.out.println("単発ツイート　" + OnceBeans.get(i).size() + "件");
			System.out.println("定期ツイート　" + RoutineBeans.get(i).size() + "件");
			for (int j = 0; j < OnceBeans.get(i).size(); j++) {
				System.out.print("　単発 \t");
				System.out.print("ID:" + OnceBeans.get(i).get(j).getOnceId());
				System.out.print("\ttext:" + OnceBeans.get(i).get(j).getText());
				System.out.print("\ttime:"
						+ OnceBeans.get(i).get(j).getReserveTime());
				System.out.println("\tposted:"
						+ OnceBeans.get(i).get(j).getPosted());
			}
			for (int j = 0; j < RoutineBeans.get(i).size(); j++) {
				System.out.print("　定期\t");
				System.out.print("ID:"
						+ RoutineBeans.get(i).get(j).getRoutineId());
				System.out.print("\ttitle:"
						+ RoutineBeans.get(i).get(j).getTitle());
				System.out.print("\ttext:"
						+ RoutineBeans.get(i).get(j).getText());
				System.out.print("\tstart:"
						+ RoutineBeans.get(i).get(j).getStartDate());
				System.out.print("\tend:"
						+ RoutineBeans.get(i).get(j).getEndDate());
				System.out.println("\tposted:"
						+ RoutineBeans.get(i).get(j).getPosted());
			}
		}
	}

	/**
	 * カレンダー削除
	 * 
	 * @param session
	 */
	private void clearSchedule(HttpSession session) {
		calendarDay = new int[42];
		RoutineBeans.clear();
		OnceBeans.clear();
		if (session.getAttribute("calendarDay") != null) {
			session.removeAttribute("calendarDay");
			session.removeAttribute("RoutineBeans");
			session.removeAttribute("OnceBeans");
		}
	}

	/**
	 * <b>int2week</b><br/>
	 * private String int2week(int num) <blockquote>
	 * カレンダー型から取得したint型の曜日を対応するStringに変換する<br/>
	 * e.g. 4 -> "水"<br/>
	 * </blockquote>
	 * 
	 * @param num
	 * @return
	 */
	private String int2week(int num) {
		switch (num) {
		case 1:
			return "日";
		case 2:
			return "月";
		case 3:
			return "火";
		case 4:
			return "水";
		case 5:
			return "木";
		case 6:
			return "金";
		case 7:
			return "土";
		default:
			return "none";
		}
	}
	
	/**
	 * yearとmonthをセットする
	 * @param strYear
	 * @param strMonth
	 * @param session 
	 */
	public void setYM(String strYear,String strMonth,HttpSession session){
		if (session.getAttribute("sche")!=null){
			int newYear = new Integer(strYear).intValue();
			int newMonth = new Integer(strMonth).intValue();
			year = newYear;
			month= newMonth-1;
		}else{
			year=0;
			month=0;
		}
	}
}

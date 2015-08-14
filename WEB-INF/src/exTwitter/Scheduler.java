package exTwitter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;

import javax.servlet.http.HttpSession;

public class Scheduler {
	ArrayList<ArrayList<OnceBean>> onceSchedule = new ArrayList<ArrayList<OnceBean>>();
	ArrayList<ArrayList<RoutineBean>> routineSchedule = new ArrayList<ArrayList<RoutineBean>>();
	int[] calendarDay = new int[42];

	public void setCalendar(int year, int month) {

		Calendar cal = Calendar.getInstance();
		int startWeek = getStartWeek(cal, year, month);
		int beforeMonthlastDay = getBeforeMonthlastDay(cal, year, month);
		int thisMonthlastDay = getThisMonthlastDay(cal, year, month);

		int count = 0;
		for (int i = startWeek - 2; i >= 0; i--) {
			calendarDay[count++] = beforeMonthlastDay - i;
		}
		for (int i = 1; i <= thisMonthlastDay; i++) {
			calendarDay[count++] = i;
		}
		int nextMonthDay = 1;
		while (count < 42) {
			calendarDay[count++] = nextMonthDay++;
		}
	}

	public int[] getCalendar() {
		return this.calendarDay;
	}

	public void setOnceSchedule(int year, int month) throws SQLException {
		Calendar cal = Calendar.getInstance();
		int startWeek = getStartWeek(cal, year, month);
		int beforeMonthlastDay = getBeforeMonthlastDay(cal, year, month);
		int thisMonthlastDay = getThisMonthlastDay(cal, year, month);

		final String once_format = "SELECT text,reserve_time,posted FROM once WHERE reserve_time>='%04d-%02d-%02d 00:00:00' and reserve_time<='%04d-%02d-%02d 23:59:59' and posted<2";

		DBManager dbm = new DBManager();
		dbm.getConnection();

		int count = 0;
		// 先月分
		for (int i = startWeek - 2; i >= 0; i--) {
			String sql = String.format(once_format, year, month,
					(beforeMonthlastDay - i), year, month,
					(beforeMonthlastDay - i));
			setOnceBeans(dbm, sql);
			count++;
		}
		// 今月分
		for (int i = 1; i <= thisMonthlastDay; i++) {
			String sql = String.format(once_format, year, month + 1, i, year,
					month + 1, i);
			setOnceBeans(dbm, sql);
			count++;
		}
		// 来月分
		int nextMonthDay = 1;
		while (count < 42) {
			String sql = String.format(once_format, year, month + 2,
					nextMonthDay, year, month + 2, nextMonthDay);
			setOnceBeans(dbm, sql);
			count++;
			nextMonthDay++;
		}
		dbm.closeConnection();
	}

	public ArrayList<ArrayList<OnceBean>> getOnceSchedule() {
		return this.onceSchedule;
	}

	public void setRoutineSchedule(int year, int month) throws SQLException {
		Calendar cal = Calendar.getInstance();
		int startWeek = getStartWeek(cal, year, month);
		int beforeMonthlastDay = getBeforeMonthlastDay(cal, year, month);
		int thisMonthlastDay = getThisMonthlastDay(cal, year, month);

		final String routine_format = "SELECT title,text,start_date,end_date,post_time FROM routine inner join %s on routine.routine_id=%s.routine_id WHERE start_date<='%04d-%02d-%02d' and end_date>='%04d-%02d-%02d' and posted=0 and day=%d";
		String sql;

		DBManager dbm = new DBManager();
		dbm.getConnection();

		int count = 0;
		// 先月分
		for (int i = startWeek - 2; i >= 0; i--) {
			int week = setWeek(count);
			ArrayList<RoutineBean> routine = new ArrayList<RoutineBean>();
			sql = String.format(routine_format, "weekly", "weekly",year, month,
					(beforeMonthlastDay - i), year, month,
					(beforeMonthlastDay - i), week);
			routine.addAll(getRoutine(dbm, sql));
			if (i == 0) {
				sql = String.format(routine_format, "monthly","monthly",year, month,
						(beforeMonthlastDay - i), year, month,
						(beforeMonthlastDay - i), 0);

			} else {
				sql = String.format(routine_format, "monthly","monthly", year, month,
						(beforeMonthlastDay - i), year, month,
						(beforeMonthlastDay - i), beforeMonthlastDay - i);
			}
			routine.addAll(getRoutine(dbm, sql));
			
			routineSchedule.add(routine);
			count++;
		}
		// 今月分
		for (int i = 1; i <= thisMonthlastDay; i++) {
			int week = setWeek(count);
			ArrayList<RoutineBean> routine = new ArrayList<RoutineBean>();
			sql = String.format(routine_format, "weekly", "weekly", year, month + 1, i,
					year, month + 1, i, week);
			routine.addAll(getRoutine(dbm, sql));
			if (i == thisMonthlastDay) {
				sql = String.format(routine_format, "monthly","monthly", year, month + 1,
						i, year, month + 1, i, 0);

			} else {
				sql = String.format(routine_format, "monthly","monthly", year, month + 1,
						i, year, month + 1, i, i);
			}
			routine.addAll(getRoutine(dbm, sql));
			routineSchedule.add(routine);
			count++;
		}
		// 来月分
		int nextMonthDay = 1;
		while (count < 42) {
			int week = setWeek(count);
			ArrayList<RoutineBean> routine = new ArrayList<RoutineBean>();
			sql = String.format(routine_format, "weekly","weekly", year, month + 2,
					nextMonthDay, year, month + 2, nextMonthDay, week);
			routine.addAll(getRoutine(dbm, sql));
			sql = String.format(routine_format, "monthly","monthly",year, month + 2,
					nextMonthDay, year, month + 2, nextMonthDay, nextMonthDay);

			routine.addAll(getRoutine(dbm, sql));
			routineSchedule.add(routine);
			count++;
			nextMonthDay++;
		}
		dbm.closeConnection();

	}

	public ArrayList<ArrayList<RoutineBean>> getRoutineSchedule() {
		return this.routineSchedule;
	}

	// 該当月一日の曜日をCalendar型に定義されるint型で返す
	private int getStartWeek(Calendar cal, int year, int month) {
		cal.set(year, month, 1); // 該当月1日目をセット
		return cal.get(Calendar.DAY_OF_WEEK);
	}

	// 該当月の先月末の日付をint型で返す
	private int getBeforeMonthlastDay(Calendar cal, int year, int month) {
		cal.set(year, month, 0); // 該当月0日目（先月末）をセット
		return cal.get(Calendar.DATE);
	}

	// 該当月末日をint型で返す
	private int getThisMonthlastDay(Calendar cal, int year, int month) {
		cal.set(year, month + 1, 0); // 翌月0日目（今月末）をセット
		return cal.get(Calendar.DATE);
	}

	private void setOnceBeans(DBManager dbm, String sql) throws SQLException {

		ResultSet rs = dbm.getResultSet(sql);
		ArrayList<OnceBean> once = new ArrayList<OnceBean>();
		while (rs.next()) {
			OnceBean bean = new OnceBean();
			bean.setText(rs.getString("text"));
			bean.setReserveTime(rs.getString("reserve_time"));
			bean.setPosted(rs.getInt("posted"));
			once.add(bean);
		}
		rs.close();
		onceSchedule.add(once);
	}

	private ArrayList<RoutineBean> getRoutine(DBManager dbm, String sql) throws SQLException {

		ResultSet rs = dbm.getResultSet(sql);
		ArrayList<RoutineBean> routine = new ArrayList<RoutineBean>();
		while (rs.next()) {
			RoutineBean bean = new RoutineBean();
			bean.setTitle(rs.getString("title"));
			bean.setText(rs.getString("text"));
			bean.setPostTime(rs.getString("post_time"));
			bean.setStartDate(rs.getString("start_date"));
			bean.setEndDate(rs.getString("end_date"));
			routine.add(bean);
		}
		rs.close();
		return routine;
	}

	private int setWeek(int count) {
		int week = count % 7;
		if (week == 0) {
			week = 7;
		}
		return week;
	}

	private static void show(Scheduler sch) {
		ArrayList<ArrayList<OnceBean>> onceBeans = sch.getOnceSchedule();
		ArrayList<ArrayList<RoutineBean>> routineBeans = sch
				.getRoutineSchedule();
		int[] calendarDay = sch.getCalendar();
		System.out.println(onceBeans.size());
		System.out.println(routineBeans.size());
		if (onceBeans.size() == routineBeans.size()) {
			for (int i = 0; i < onceBeans.size(); i++) {
				System.out.println(calendarDay[i] + "日");
				for (int j = 0; j < onceBeans.get(i).size(); j++) {
					System.out.print("　単発 \t");
					System.out.print("ID:"
							+ onceBeans.get(i).get(j).getOnceId());
					System.out.print("\ttext:"
							+ onceBeans.get(i).get(j).getText());
					System.out.print("\ttime:"
							+ onceBeans.get(i).get(j).getReserveTime());
					System.out.println("\tposted:"
							+ onceBeans.get(i).get(j).getPosted());
				}

				for (int j = 0; j < routineBeans.get(i).size(); j++) {
					System.out.print("　定期\t");
					System.out.print("ID:"
							+ routineBeans.get(i).get(j).getRoutineId());
					System.out.print("\ttitle:"
							+ routineBeans.get(i).get(j).getTitle());
					System.out.print("\ttext:"
							+ routineBeans.get(i).get(j).getText());
					System.out.print("\tstart:"
							+ routineBeans.get(i).get(j).getStartDate());
					System.out.print("\tend:"
							+ routineBeans.get(i).get(j).getEndDate());
					System.out.println("\tposted:"
							+ routineBeans.get(i).get(j).getPosted());
				}
			}
		}
	}
	
	public void setSchedule(HttpSession session, int year,int month)throws SQLException{
		Scheduler sch = new Scheduler();
		sch.setCalendar(year, month-1);
		sch.setOnceSchedule(year, month-1);
		sch.setRoutineSchedule(year, month-1);
		
		int[] calendarDay = sch.getCalendar();
		ArrayList<ArrayList<OnceBean>> onceBeans = sch.getOnceSchedule();
		ArrayList<ArrayList<RoutineBean>> routineBeans = sch
				.getRoutineSchedule();
		
		session.setAttribute("calendarDay", calendarDay);
		session.setAttribute("OnceBeans", onceBeans);
		session.setAttribute("RoutineBeans", routineBeans);
		session.setAttribute("year",year);
		session.setAttribute("month", month);
	}

	public static void main(String[] args) throws SQLException {
		int year = 2015;
		int month = 8;

		Scheduler sch = new Scheduler();
		sch.setCalendar(year, month);
		sch.setOnceSchedule(year, month);
		sch.setRoutineSchedule(year, month);
		show(sch);
	}

}

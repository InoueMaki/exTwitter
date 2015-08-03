package exTwitter;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/*
 * すごく...PHPです///
 */
@SuppressWarnings("serial")
public class Detail extends HttpServlet {
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html;charset=utf-8");

		PrintWriter out = response.getWriter();
		
		System.out.println("Servlet: Detail Start");
		DBManager DBM = new DBManager();
		try {
			DBM.getConnection("excite");
		} catch (SQLException e) {
			System.err.println("エラー:データベースに接続できません。\n\n");
			e.printStackTrace();
		}
		ResultSet rs = null;
		ResultSet rsm = null;
		ResultSet rsw = null;

		rs = DBM.getResultSet("select * from user");
		out.println("<html>");
		out.println("<head><title>SQL登録情報</title></head>");
		out.println("<body>");
		out.println("ユーザ情報<br/>");
		out.println("<table id='user' border=1>");
		out.println("<tr>");
		out.println("<th>user_id</th>");
		out.println("<th>user_name</th>");
		out.println("<th>password</th>");
		out.println("<th>valid</th>");
		try {
			if (null != rs) {
				while (rs.next()) {
					out.println("<tr>");
					out.println("<td>" + rs.getInt("user_id") + "</td>");
					out.println("<td>" + rs.getString("user_name") + "</td>");
					out.println("<td>" + rs.getString("password") + "</td>");
					out.println("<td>" + rs.getInt("valid") + "</td>");
					out.println("</tr>");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		out.println("</table>");

		rs = DBM.getResultSet("select * from once");
		out.println("<br/><br/>");
		out.println("単発ツイート情報<br/>");
		out.println("<table id='once' border=1>");
		out.println("<tr>");
		out.println("<th>once_id</th>");
		out.println("<th>text</th>");
		out.println("<th>reserve_time</th>");
		out.println("<th>posted</th>");
		try {
			if (null != rs) {
				while (rs.next()) {
					out.println("<tr>");
					out.println("<td>" + rs.getInt("once_id") + "</td>");
					out.println("<td>" + rs.getString("text") + "</td>");
					out
							.println("<td>" + rs.getString("reserve_time")
									+ "</td>");
					out.println("<td>" + rs.getInt("posted") + "</td>");
					out.println("</tr>");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		out.println("</table>");

		rs = DBM.getResultSet("select * from routine");
		out.println("<br/><br/>");
		out.println("定期ツイート情報<br/>");
		out.println("<table id='once' border=1>");
		out.println("<tr>");
		out.println("<th>once_id</th>");
		out.println("<th>title</th>");
		out.println("<th>text</th>");
		out.println("<th>start_date</th>");
		out.println("<th>end_date</th>");
		out.println("<th>post_time</th>");
		out.println("<th>span</th>");
		out.println("<th>posted</th>");

		ArrayList<String[]> routines = new ArrayList<String[]>();

		try {
			while (rs.next()) {
				String routine_list[] = new String[7];
				routine_list[0] = String.valueOf(rs.getInt("routine_id"));
				routine_list[1] = rs.getString("title");
				routine_list[2] = rs.getString("text");
				routine_list[3] = rs.getString("start_date");
				routine_list[4] = rs.getString("end_date");
				routine_list[5] = rs.getString("post_time");
				routine_list[6] = String.valueOf(rs.getInt("posted"));
				routines.add(routine_list);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			Iterator<String[]> rout_iter = routines.iterator();
			while (rout_iter.hasNext()) {
				String routine_list[] = rout_iter.next();

				out.println("<tr>");
				out.println("<td>" + routine_list[0] + "</td>");
				out.println("<td>" + routine_list[1] + "</td>");
				out.println("<td>" + routine_list[2] + "</td>");
				out.println("<td>" + routine_list[3] + "</td>");
				out.println("<td>" + routine_list[4] + "</td>");
				out.println("<td>" + routine_list[5] + "</td>");
				out.print("<td style='text-align:center'>");
				rsm = DBM
						.getResultSet("select day from monthly where routine_id="
								+ routine_list[0]);
				String day = "";
				while (rsm.next()) {
					day += int2day(rsm.getInt("day")) + ",";

				}
				out.println(trimComma(day));

				rsw = DBM
						.getResultSet("select day from weekly where routine_id="
								+ routine_list[0]);
				String week = "";
				while (rsw.next()) {
					week += int2week(rsw.getInt("day")) + ",";
				}
				out.println(trimComma(week) + "</td>");
				out.println("<td>" + routine_list[6] + "</td>");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		out.println("</table>");

		out.println("</body>");
		out.println("</html>");
		DBM.closeConnection();
	}

	/**
	 * <b>trimComma</b><br/>
	 * private String trimComma(String str)<br/>
	 * <blockquote>
	 * 文字列末尾から半角コンマを取り除く
	 * </blockquote>
	 * @param str
	 * @return String 
	 */
	private String trimComma(String str) {
		if (str.endsWith(",")) {
			str = str.substring(0, str.length() - 1);
		}
		return str;
	}

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

	private String int2day(int num) {
		if (num == 0) {
			return "月末";
		}
		return num + "日";
	}

}

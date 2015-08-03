package exTwitter;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * ServletによるDBManagerの動作確認プログラム<br/>
 * DBによるアクセスカウンターもどき<br/>
 * DBにアクセスし値をインクリメントしてから表示する<br/>
 * DB:test TBL:test
 * @author sato
 */
@SuppressWarnings("serial")
public class DBtest extends HttpServlet {
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("Servlet: DBtest Start");
		
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		out.println("<html><body><h1>アクセスカウンターもどき</h1>");
		DBManager DBM = new DBManager();
		
		try {
			DBM.getConnection("test");
		} catch (SQLException e) {
			System.err.println("エラー:データベースに接続できません。\n\n");
			e.printStackTrace();
		}
		try {
			DBM.createTable("CREATE TABLE test(counter int)");
			DBM.exeUpdate("INSERT INTO test VALUES(0)");
		} catch (SQLException e) {
			System.err.println("\n\t\tTABLE 'test' already exist");
		}

		DBM
				.exeUpdate("UPDATE test SET counter=(select max(counter) from test)+1");
		ResultSet rs = DBM.getResultSet("SELECT counter FROM test");
		try {
			while (rs.next()) {
				out.println("\tアクセス数\t<u>" + rs.getInt("counter") + "</u>");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		DBM.closeConnection();
		out.println("</body></html>");
	}

}

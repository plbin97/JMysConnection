package jmysconnection;

import java.sql.DriverManager;
import java.sql.SQLException;

import com.mysql.jdbc.Connection;

public class Configuration {
	/**
	 * @author Jerry_P
	 * 
	 *         <p>
	 *         JMysConnection Version V0.1.1
	 *         </p>
	 * 
	 *         <p>
	 *         使用前，先在这里设置一下数据库信息
	 *         </p>
	 *         <p>
	 *         请将连接驱动包：mysql-connector-java-5.1.6-bin.jar导入。
	 *         </p>
	 *         <p>
	 *         Before using, place configurate following terms
	 *         </p>
	 *         <p>
	 *         Place input the connection driver's package:mysql-connector-java-5.1.6-bin.jar
	 *         </p>
	 */
	public static Connection getConn() {
		String driver = "com.mysql.jdbc.Driver";
		String addr = "localhost"; //这里输入数据库地址 Input your database address here
		String databaseName = "name"; // 这里输入数据库名 Input your database name here
		String port = "3306"; // 这里输入端口 Input your database port here
		String username = "root"; // 这里输入数据库用户名 Your Mysql username
		String password = "password"; // 这里输入数据库密码 Your Mysql password
		String url = "jdbc:mysql://" + addr + ":" + port + "/" + databaseName;
		Connection conn = null;
		try {
			Class.forName(driver);
			conn = (Connection) DriverManager.getConnection(url, username, password);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}
}

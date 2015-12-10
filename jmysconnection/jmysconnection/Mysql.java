package jmysconnection;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import jmysconnection.Configuration;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;

/**
 * 
 * @author Jerry_P
 * 
 *         <p>
 *         JMysConnection Version V0.1.1
 *         </p>
 * 
 *         <p>
 *         使用前，请将本地信息填写如Configuration.java，使用时，请导入此类
 *         </p>
 *         <p>
 *         Before using is, place fill in some terms in Configuration.java, and place import this class if you use it
 *         </p>
 * 
 *         <p>
 *         Tips: Before calling each method, place call countSelected() and make sure the return is not 0 in order to make sure that the data existed, otherwise report errors.
 *         </p>
 *         <p>
 *         We did not defense the sql injection, place filter the character that may harm to the program or escaping the character.
 *         </p>
 *         <p>
 *         注意： 调用此类方法前，请先调用countSelected()确认返回值非0，而保证数据存在，否则会报错。
 *         </p>
 *         <p>
 *         我们并没有防范Sql注入，请在调用前确保危险字符以过滤或转义。
 *         </p>
 * 
 */

public class Mysql {
	@SuppressWarnings("rawtypes")
	/**
	 * <p>insert函数只能一次性插入一条数据</p>
	 * <p>“table”参数则是数据表名</p>
	 * <p>“title”参数则是数据表的列名，格式为“列1,列2,列3”，举个例子，假如表中有3列，分别叫“Name”、“Value”、“Date”，“title”参数则需要写成“Name,Value,Date”</p>
	 * <p>“row”蚕食则是要插入的数据，类型是ArrayList，每次插入数据的顺序都以列名对应，假如表中有3列，分别叫“Name”、“Value”、“Date”，则row[0]对应Name，row[1]对应Value，以此类推。</p>
	 * <p>Method insert just can insert one row of data when called.</p>
	 * @param table Parameter "table" is the name of table.
	 * @param title Parameter "title" is the name of each rank, and the format of this parameter is "rank1,rank2,rank3". For example, suppose we have three ranks, and each called "Name", "Value", "Date". So the parameter has to be write as "Name,Value,Date".
	 * @param row Parameter "row" is an ArrayList which contain the value you wanted to insert in. Each value in "row" is related to the parameter of "title". For example, suppose we have three ranks "Name", "Value", "Date", then row[0] relate to "Name", and row[1] relate to "Value", and the rest can be done in the same manner.
	 */
	public static void insert(String table, String title, ArrayList row) {
		Connection conn = Configuration.getConn();
		String title1 = "?";
		char d = ',';
		char[] chars = title.toCharArray();
		for (int a = 1; a < chars.length; a++) {
			if (d == chars[a]) {
				title1 = title1 + ",?";
			}
		}
		String sql = "insert into " + table + "(" + title + ") values(" + title1 + ")";
		PreparedStatement pstmt;
		try {
			pstmt = (PreparedStatement) conn.prepareStatement(sql);
			for (int a = 1; a <= row.size(); a++) {
				if (row.get(a - 1) instanceof Integer) {
					pstmt.setInt(a, (Integer) row.get(a - 1));
				} else if (row.get(a - 1) instanceof String) {
					pstmt.setString(a, (String) row.get(a - 1));
				} else if (row.get(a - 1) instanceof Timestamp) {
					pstmt.setTimestamp(a, (Timestamp) row.get(a - 1));
				}
			}
			pstmt.executeUpdate();
			pstmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * <p>
	 * 方法“update”是通过一个数据进行定位来修改单个数据。
	 * </p>
	 * <p>
	 * 参数“table”是所要修改数据的表名。
	 * </p>
	 * <p>
	 * 参数“condition”用于数据定位，举一个例子，我们要找“Name”这一列中的数值为“Jack”的那一行数据，那么“condition”参数需要写成“Name='Jack'”，切记表达式中必须使用单引号。如果需要再添加多个条件进行and或者or的逻辑判断时，请使用“&&”（and）或者“||”（or）进行分隔，比如说“Name='Jack'&&Password='123'”。一旦参数填写错误时将会报错，使用前建议先使用searchOneRowByIndex()确认。
	 * </p>
	 * <p>
	 * Method "update" use for modify one data locating by specified data.
	 * </p>
	 * 
	 * @param table
	 *            Parameter "table" is the name of table that you are going to modifying.
	 * @param condition
	 *            Parameter "condition" use for locating the position of data you are going to modify. For example, if we have to find a row by rank "Name" equals "Jack", the parameter "condition" should be write as "Name='Jack'". Be sure to keep in mind, you must use single quotes rather than double quotas. If you wanted to adding logical judgment such as "and" and "or", place use "&&" represent "and" and "||"represent"or". For example “Name='Jack'&&Password='123'”. It would report errors when you transfer a wrong data, so place use searchOneRowByIndex() method for ensuring before update() method is called.
	 * @param modificationIndex
	 *            Parameter "modificationIndex" is the rank's name that you are going to modify
	 * @param modificationData
	 *            Parameter "modificationData" is the value of data that you are going to put that data instead of present data. For example, if I want to modify a data which rank's name is "name" and modify it as "abc", the parameter "modificationIndex" should be "name" and parameter "modificationData" should be "abc".
	 */

	public static void update(String table, String condition, String modificationIndex, String modificationData) {
		Connection conn = Configuration.getConn();
		String sql = "update " + table + " set " + modificationIndex + "='" + modificationData + "' where " + condition;
		PreparedStatement pstmt;
		try {
			pstmt = (PreparedStatement) conn.prepareStatement(sql);
			pstmt.executeUpdate();
			pstmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * <p>
	 * 方法“delete”是用于利用其中一个数据定位后进行删除某行数据，
	 * </p>
	 * <p>
	 * 参数“table”是指删除目标的表名
	 * </p>
	 * <p>
	 * 参数“condition”用于数据定位，举一个例子，我们要找“Name”这一列中的数值为“Jack”的那一行数据，那么“condition”参数需要写成“Name='Jack'”，切记表达式中必须使用单引号。如果需要再添加多个条件进行and或者or的逻辑判断时，请使用“&&”（and）或者“||”（or）进行分隔，比如说“Name='Jack'&&Password='123'”。一旦参数填写错误时将会报错，使用前建议先使用searchOneRowByIndex()确认。
	 * </p>
	 * <p>
	 * Method "delete" use for delete one row of data locating by a data.
	 * </p>
	 * 
	 * @param table
	 *            Parameter "table" is the target's table name.
	 * @param condition
	 *            Parameter "condition" use for locating the position of data you are going to modify. For example, if we have to find a row by rank "Name" equals "Jack", the parameter "condition" should be write as "Name='Jack'". Be sure to keep in mind, you must use single quotes rather than double quotas. If you wanted to adding logical judgment such as "and" and "or", place use "&&" represent "and" and "||"represent"or". For example “Name='Jack'&&Password='123'”. It would report errors when you transfer a wrong data, so place use searchOneRowByIndex() method for ensuring before update() method is called.
	 */

	public static void delete(String table, String condition) {
		Connection conn = Configuration.getConn();
		String sql = "delete from " + table + " where " + condition;
		PreparedStatement pstmt;
		try {
			pstmt = (PreparedStatement) conn.prepareStatement(sql);
			pstmt.executeUpdate();
			pstmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * <p>
	 * 方法deleteAll()是用于删除一个表中的所有数据，删除后会保留表格。
	 * </p>
	 * <p>
	 * 参数“table”是所要被删除数据的表名。
	 * </p>
	 * <p>
	 * Method deleteAll() use for delete everything from specific table, and retain table when called.
	 * </p>
	 * 
	 * @param table
	 *            Parameter "table" is the name of table which you are going to empty.
	 */

	public static void deleteAll(String table) {
		Connection conn = Configuration.getConn();
		String sql = "delete from " + table;
		PreparedStatement pstmt;
		try {
			pstmt = (PreparedStatement) conn.prepareStatement(sql);
			pstmt.executeUpdate();
			pstmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * <p>
	 * 方法getAll()是用于获取一个表里面的全部数据，返回类型是String的二维数组。
	 * </p>
	 * <p>
	 * 参数“table”是所需要获取数据的表名。
	 * </p>
	 * <p>
	 * 以下是一个完全对应数据库表格的输出方式：
	 * </p>
	 * <p>
	 * Method getAll() use for obtain all data from specific table, the return type is String array with 2 dimension.
	 * </p>
	 * <p>
	 * if (data[0] != null) {
	 * </p>
	 * <p>
	 * for (int i = 0; i < data[0].length; i++) {
	 * </p>
	 * <p>
	 * for (int j = 0; j < data.length; j++) {
	 * </p>
	 * <p>
	 * System.out.print(data[j][i]+"  ");
	 * </p>
	 * <p>
	 * </p>
	 * <p>
	 * System.out.println("");
	 * </p>
	 * <p>
	 * </p>
	 * <p>
	 * </p>
	 * 
	 * @param table
	 *            Parameter "table" is the target's table name
	 * @return The return type is 2 dimensional String array. An output that correspond to the table in database as shown above
	 */

	public static String[][] getAll(String table) {
		Connection conn = Configuration.getConn();
		String sql = "select * from " + table;
		PreparedStatement pstmt;
		try {
			pstmt = (PreparedStatement) conn.prepareStatement(sql);
			java.sql.ResultSet rs = pstmt.executeQuery();
			int col = rs.getMetaData().getColumnCount();
			String data[][] = new String[col][Mysql.countAll(table)];
			int i = 0;
			while (rs.next()) {
				for (int j = 1; j <= col; j++) {
					data[j - 1][i] = rs.getString(j);
				}
				i = i + 1;
			}
			return data;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * <p>
	 * 方法searchOneRowByIndex()是利用一个数据数值来搜索一条数据。
	 * </p>
	 * <p>
	 * 参数“table”表示所要查询的表名。
	 * </p>
	 * <p>
	 * 参数“condition”用于数据定位，举一个例子，我们要找“Name”这一列中的数值为“Jack”的那一行数据，那么“condition”参数需要写成“Name='Jack'”，切记表达式中必须使用单引号。如果需要再添加多个条件进行and或者or的逻辑判断时，请使用“&&”（and）或者“||”（or）进行分隔，比如说“Name='Jack'&&Password='123'”。
	 * </p>
	 * <p>
	 * Method searchOneRowByIndex() use for selecting a row of data located by one data.
	 * </p>
	 * 
	 * @param table
	 *            Parameter "table" is the name of table that you going to search.
	 * @param condition
	 *            Parameter "condition" use for locating the position of data you are going to search. For example, if we have to find a row by rank "Name" equals "Jack", the parameter "condition" should be write as "Name='Jack'". Be sure to keep in mind, you must use single quotes rather than double quotas. If you wanted to adding logical judgment such as "and" and "or", place use "&&" represent "and" and "||"represent"or". For example “Name='Jack'&&Password='123'”.
	 * @return The return type is String array. The order of array is corresponding to the rank in table.
	 */

	public static String[] searchOneRowByIndex(String table, String condition) {
		Connection conn = Configuration.getConn();
		String sql = "select * from " + table + " where " + condition;
		PreparedStatement pstmt;
		try {
			pstmt = (PreparedStatement) conn.prepareStatement(sql);
			java.sql.ResultSet rs = pstmt.executeQuery();
			int col = rs.getMetaData().getColumnCount();
			String data[] = new String[col];
			while (rs.next()) {
				for (int j = 1; j <= col; j++) {
					data[j - 1] = rs.getString(j);
				}
				break;
			}

			return data;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * <p>
	 * 方法searchFewRowByIndex()是用于搜索全部符合条件的数据，返回类型是String二维数组。
	 * </p>
	 * <p>
	 * 参数“table”是指所要进行搜索的表格名
	 * </p>
	 * <p>
	 * 参数“index”和“indexValue”是指所用来定位数据那一列的列名，假如说有一个列的列名是“Name”，我需要所有“Name”这一列的数值等于“abc”的所有数据，那么参数“index”的值应该是“Name”，参数“indexValue”的值应该是“abc”。
	 * </p>
	 * <p>
	 * Method searchFewRowByIndex() use to search all data which match the condition, and the return type is String array with 2 dimension.
	 * </p>
	 * <p>
	 * if (data[0] != null) {
	 * </p>
	 * <p>
	 * for (int i = 0; i < data[0].length; i++) {
	 * </p>
	 * <p>
	 * for (int j = 0; j < data.length; j++) {
	 * </p>
	 * <p>
	 * System.out.print(data[j][i]+"  ");
	 * </p>
	 * <p>
	 * </p>
	 * <p>
	 * System.out.println("");
	 * </p>
	 * <p>
	 * </p>
	 * <p>
	 * </p>
	 * 
	 * @param table
	 *            Parameter "table" is the name of table you going to search.
	 * @param index
	 *            Parameters "index" and "indexValue" use for locating the data you going to search. Suppose there is a rank that names "Name", and I want all row of data that the rank "Name" value is "abc". The parameter "index" value should be "Name" and parameter "indexValue" should be "abc".
	 * @param indexValue
	 *            See parameter index.
	 * @return The return type is 2 dimensional String array. An output that correspond to the table in database as shown above.
	 */

	public static String[][] searchFewRowByIndex(String table, String index, String indexValue) {
		Connection conn = Configuration.getConn();
		String sql = "select * from " + table + " where " + index + "='" + indexValue + "'";
		PreparedStatement pstmt;
		try {
			pstmt = (PreparedStatement) conn.prepareStatement(sql);
			java.sql.ResultSet rs = pstmt.executeQuery();
			int col = rs.getMetaData().getColumnCount();
			String data[][] = new String[col][Mysql.countSelected(table, index + "," + indexValue)];
			int i = 0;
			while (rs.next()) {
				for (int j = 1; j <= col; j++) {
					data[j - 1][i] = rs.getString(j);
				}
				i = i + 1;
			}
			return data;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * <p>
	 * 方法“countAll”是用于计算所有数据的行数
	 * </p>
	 * <p>
	 * 参数“table”是指所需要进行计数的表名
	 * </p>
	 * <p>
	 * 方法“countAll”的返回值是（Integer）整形，返回值则是表中的行数。
	 * </p>
	 * <p>
	 * Method "countAll" use for counting the number of row which existed in specific table
	 * </p>
	 * 
	 * @param table
	 *            Parameter "table" is the name of specific table that you are going to counting.
	 * @return return type is integer, and the value in return is the number of row in specific table.
	 */

	public static int countAll(String table) {
		Connection conn = Configuration.getConn();
		String sql = "select * from " + table;
		PreparedStatement pstmt;
		try {
			pstmt = (PreparedStatement) conn.prepareStatement(sql);
			java.sql.ResultSet rs = pstmt.executeQuery();
			int raw = 0;
			while (rs.next()) {
				raw = raw + 1;
			}
			return raw;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return (Integer) null;
	}

	/**
	 * <p>
	 * 方法“countSelected”是用于计算所匹配条件数据的行数
	 * </p>
	 * <p>
	 * 参数“table”是指所需要进行计数的表名
	 * </p>
	 * <p>
	 * 参数“condition”是所需要统计数据的条件，其格式请参照其他方法
	 * </p>
	 * <p>
	 * 方法“countSelected”的返回值是（Integer）整形，返回值则是符合条件的行数。
	 * </p>
	 * 
	 * @param table
	 *            Parameter "table" is the name of specific table that you are going to counting.
	 * @param condition
	 *            Parameter "condition" is the qualification of counting, and place using the format as shown in previous method.
	 * @return return type is integer, and the value in return is the number of row in specific table with specific condition.
	 */

	public static int countSelected(String table, String condition) {
		Connection conn = Configuration.getConn();
		String sql = "select * from " + table + " where " + condition;
		PreparedStatement pstmt;
		try {
			pstmt = (PreparedStatement) conn.prepareStatement(sql);
			java.sql.ResultSet rs = pstmt.executeQuery();
			int raw = 0;
			while (rs.next()) {
				raw = raw + 1;
			}
			return raw;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return (Integer) null;
	}

	/**
	 * <p>
	 * 方法“custom”是用于自定义Sql的命令输入
	 * </p>
	 * <p>
	 * 参数“sql”是你所需要输入的sql命令
	 * </p>
	 * <p>
	 * Method "custom" use for custom Sql commend entry.
	 * </p>
	 * 
	 * @param sql
	 *            Parameter "sql" is the Sql commend you are going to entry.
	 */

	public static void custom(String sql) {
		Connection conn = Configuration.getConn();
		PreparedStatement pstmt;
		try {
			pstmt = (PreparedStatement) conn.prepareStatement(sql);
			pstmt.executeUpdate();
			pstmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * <p>
	 * 方法“selectTime”是用于查询数据库中Timestamp时间
	 * </p>
	 * <p>
	 * 参数“table”是搜索目标的表格
	 * </p>
	 * <p>
	 * 参数“condition”是用于定位数据的条件，举一个例子，我们要找“Name”这一列中的数值为“Jack”的那一行数据，那么“condition”参数需要写成“Name='Jack'”，切记表达式中必须使用单引号。如果需要再添加多个条件进行and或者or的逻辑判断时，请使用“&&”（and）或者“||”（or）进行分隔，比如说“Name='Jack'&&Password='123'”。
	 * </p>
	 * <p>
	 * 参数“positionOfTime”是确定Timestamp所在的列，传入值即为列名
	 * </p>
	 * 
	 * @param table
	 *            "table" is the table in database you going to select.
	 * @param condition
	 *            "condition" use for locating the position of data you are going to search. For example, if we have to find a row by rank "Name" equals "Jack", the parameter "condition" should be write as "Name='Jack'". Be sure to keep in mind, you must use single quotes rather than double quotas. If you wanted to adding logical judgment such as "and" and "or", place use "&&" represent "and" and "||"represent"or". For example “Name='Jack'&&Password='123'”.
	 * @param positionOfTime
	 *            "positionOfTime" is the location of time in row.
	 * @return method return the Timestamp object from database.
	 */

	public static Timestamp selectTime(String table, String condition, int positionOfTime) {
		Connection conn = Configuration.getConn();
		String sql = "select * from " + table + " where " + condition;
		PreparedStatement pstmt;
		try {
			pstmt = (PreparedStatement) conn.prepareStatement(sql);
			java.sql.ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				return rs.getTimestamp(positionOfTime);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * <p>
	 * 方法getAll()是用于获取一个表里面的全部数据，返回类型是String的二维数组。
	 * </p>
	 * <p>
	 * 参数“table”是所需要获取数据的表名。
	 * </p>
	 * <p>
	 * 参数“sortIndex”是排列依据，根据哪一列进行排序
	 * </p>
	 * <p>
	 * 参数“method”是用于决定排列方式，使用时，请传入值：“asc”为正序排列，“desc”为倒序排列
	 * </p>
	 * <p>
	 * 以下是一个完全对应数据库表格的输出方式：
	 * </p>
	 * <p>
	 * Method getAll() use for obtain all data from specific table, the return type is String array with 2 dimension.
	 * </p>
	 * <p>
	 * if (data[0] != null) {
	 * </p>
	 * <p>
	 * for (int i = 0; i < data[0].length; i++) {
	 * </p>
	 * <p>
	 * for (int j = 0; j < data.length; j++) {
	 * </p>
	 * <p>
	 * System.out.print(data[j][i]+"  ");
	 * </p>
	 * <p>
	 * </p>
	 * <p>
	 * System.out.println("");
	 * </p>
	 * <p>
	 * </p>
	 * <p>
	 * </p>
	 * 
	 * @param table
	 *            Parameter "table" is the target's table name
	 * @param sortIndex
	 *            Parameter "sortIndex" is the rank that you are according to sort the whole data.
	 * @param method
	 *            Parameter "method" is the method you going to sort, and please pass only two value "asc" means ascending sequence and "desc" means descending sequence
	 * @return The return type is 2 dimensional String array. An output that correspond to the table in database as shown above
	 */

	public static String[][] getAll(String table, String sortIndex, String method) {
		Connection conn = Configuration.getConn();
		String sql = "select * from " + table + " order by " + sortIndex + " " + method;
		PreparedStatement pstmt;
		try {
			pstmt = (PreparedStatement) conn.prepareStatement(sql);
			java.sql.ResultSet rs = pstmt.executeQuery();
			int col = rs.getMetaData().getColumnCount();
			String data[][] = new String[col][Mysql.countAll(table)];
			int i = 0;
			while (rs.next()) {
				for (int j = 1; j <= col; j++) {
					data[j - 1][i] = rs.getString(j);
				}
				i = i + 1;
			}
			return data;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

}

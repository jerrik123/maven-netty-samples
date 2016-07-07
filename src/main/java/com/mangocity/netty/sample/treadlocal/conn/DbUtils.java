package com.mangocity.netty.sample.treadlocal.conn;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbUtils {

	private static final String URL = "jdbc:mysql://localhost:3306/max_conn_num?characterEncoding=utf-8";

	private static final String DRIVER_CLASS_NAME = "com.mysql.jdbc.Driver";

	private static final String USER_NAME = "root";

	private static final String PASSWORD = "root";

	// 连接
	private static Connection conn;

	static {
		try {
			Class.forName(DRIVER_CLASS_NAME);
			conn = DriverManager.getConnection(URL, USER_NAME, PASSWORD);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	private static final ThreadLocal<Connection> CONNECTION_HOLDER = new ThreadLocal<Connection>() {
		@Override
		protected Connection initialValue() {
			try {
				Class.forName(DRIVER_CLASS_NAME);
				conn = DriverManager.getConnection(URL, USER_NAME, PASSWORD);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return conn;
		}

	};

	/**
	 * 让所有线程公用一个Connection
	 * 
	 * @return
	 */
	public static Connection getConnection() {
		return conn;
	}

	public static Connection getConnWithThreadLocal() {
		Connection conn = CONNECTION_HOLDER.get();
		if (null == conn) {
			try {
				conn = DriverManager.getConnection(URL, USER_NAME, PASSWORD);
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				CONNECTION_HOLDER.set(conn);
			}
		}
		return conn;
	}

	public static void closeConnection() {
		try {
			if (conn != null) {
				conn.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void closeConnectionWithThreadLocal() {
		Connection conn = CONNECTION_HOLDER.get();
		try {
			if (conn != null) {
				conn.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			CONNECTION_HOLDER.remove();
		}
	}
}

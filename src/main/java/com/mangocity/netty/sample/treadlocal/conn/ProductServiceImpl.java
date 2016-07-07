package com.mangocity.netty.sample.treadlocal.conn;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ProductServiceImpl implements IProductService {

	private static final String UPDATE_PRODUCT_SQL = "update t_product p set p.price=? where p.product_id=?";

	/**
	 * 多个线程共享同一个连接就会报该错
	 * com.mysql.jdbc.exceptions.jdbc4.MySQLNonTransientConnectionException: No
	 * operations allowed after connection closed.
	 * 
	 * 改进: 将connection存放到ThreadLocal
	 */
	@Override
	public int updateProduct(Long productId, Double price) {
		System.out.println("updateProduct begin{} productId: " + productId + " ,price: " + price);
		int row = 0;
		Connection conn = null;
		try {
			// 获取连接
			conn = DbUtils.getConnection();

			conn.setAutoCommit(false); // 关闭自动提交事务（开启事务）

			// 执行操作
			row = updateProduct(conn, UPDATE_PRODUCT_SQL, productId, price); // 更新产品
			// insertLog(conn, INSERT_LOG_SQL, "Create product."); // 插入日志
			System.out.println(row == 1 ? "更新成功" : "更新失败");
			// 提交事务
			conn.commit();
		} catch (Exception e) {
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} finally {
			// 关闭连接
			DbUtils.closeConnection();
		}
		return row;
	}

	/**
	 * 用ThreadLocal存放连接
	 */
	public int updateProductWithThreadLocal(Long productId, Double price) {
		System.out.println("updateProductWithThreadLocal begin{} productId: " + productId + " ,price: " + price);
		int row = 0;
		Connection conn = null;
		try {
			// 获取连接
			conn = DbUtils.getConnWithThreadLocal();// 关闭也是取ThreadLocal中的线程
			conn.setAutoCommit(false); // 关闭自动提交事务（开启事务）

			// 执行操作
			row = updateProduct(conn, UPDATE_PRODUCT_SQL, productId, price); // 更新产品
			// insertLog(conn, INSERT_LOG_SQL, "Create product."); // 插入日志
			System.out.println(row == 1 ? "更新成功" : "更新失败");
			// 提交事务
			conn.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 关闭连接
			/**
			 * com.mysql.jdbc.exceptions.jdbc4.MySQLNonTransientConnectionException: 
			 * Data source rejected establishment of connection,  message from server: "Too many connections"
			 * 如果不释放连接,则抛出Too many connections
			 */
			//DbUtils.closeConnectionWithThreadLocal();
		}
		return row;
	}

	private int updateProduct(Connection conn, String updateProductSQL, long productId, Double productPrice)
			throws Exception {
		PreparedStatement pstmt = conn.prepareStatement(updateProductSQL);
		pstmt.setDouble(1, productPrice);
		pstmt.setLong(2, productId);
		int rows = 0;
		rows = pstmt.executeUpdate();
		if (rows != 0) {
			System.out.println("Update product success!");
		}
		return rows;
	}

}

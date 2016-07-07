package com.mangocity.netty.sample.treadlocal.conn;

import java.util.Random;

public class ProductClient {

	public static void main(String[] args) {
		final IProductService productService = new ProductServiceImpl();
		final Random random = new Random();
		for (int i = 0; i < 1000; i++) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					//updateProduct多个线程共享连接,其他线程关闭了另外一个线程的连接(com.mysql.jdbc.exceptions.jdbc4.MySQLNonTransientConnectionException: No operations allowed after connection closed.)
					productService.updateProduct(2L, random.nextDouble());
					
					//ThreadLocal存放连接(正常)
					//productService.updateProductWithThreadLocal(2L, random.nextDouble());
				}
			}).start();
		}
	}

}

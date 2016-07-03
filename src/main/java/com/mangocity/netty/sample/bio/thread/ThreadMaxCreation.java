package com.mangocity.netty.sample.bio.thread;

import java.util.concurrent.TimeUnit;

/**
 * 创建大量线程 模拟BIO处理方式
 * 当访问两增大时,CPU很容易到达100%
 * @author YangJie
 */
public class ThreadMaxCreation {
	private static final int MAX_THREAD_NUM = 20000;

	public static void main(String[] args) {
		for(int i=0;i<MAX_THREAD_NUM;i++){
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						System.out.println("create thread name {" + Thread.currentThread().getName() + "}");
						TimeUnit.MILLISECONDS.sleep(5000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}).start();
		}
	}

}

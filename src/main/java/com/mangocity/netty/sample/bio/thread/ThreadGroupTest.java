package com.mangocity.netty.sample.bio.thread;

import java.util.concurrent.TimeUnit;

public class ThreadGroupTest {

	public static void main(String[] args) {
		ThreadGroup threadGroup = new ThreadGroup("自定义线程组");

		for (int i = 0; i < 5; i++) {
			new Thread(threadGroup, new Runnable() {
				@Override
				public void run() {
					System.out.println("start thread " + Thread.currentThread().getName());
				}
			}).start();
			System.out.println("激活线程数: " + threadGroup.activeGroupCount());
		}
		threadGroup.list();

	}

}

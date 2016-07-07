package com.mangocity.netty.sample.nio.test;

import java.util.concurrent.TimeUnit;

public class ThreadGroupClient {
	public static void main(String[] args) {
		ThreadGroup tGroup = new ThreadGroup("自定义线程组");
		Thread t = new Thread(tGroup, new Runnable() {
			@Override
			public void run() {
				try {
					TimeUnit.SECONDS.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println("thread run...");
			}
		}, "自定义线程");
		t.start();
		System.out.println("线程优先级1: " + t.getPriority());
		System.out.println(tGroup.getName());
		tGroup.list();
		tGroup.setMaxPriority(4);
		tGroup.list();
		System.out.println("线程优先级2: " + t.getPriority());
		
	}
}

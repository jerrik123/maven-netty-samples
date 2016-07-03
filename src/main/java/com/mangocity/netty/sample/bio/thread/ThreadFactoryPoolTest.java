package com.mangocity.netty.sample.bio.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

public class ThreadFactoryPoolTest {

	public static void main(String[] args) {
		ExecutorService executor = Executors.newCachedThreadPool(new ThreadFactory() {
			
			@Override
			public Thread newThread(Runnable r) {
				System.out.println("ThreadFactory begin{}...");
				System.out.println("执行线程..." + r);
				return new Thread(r);
			}
		});
		
		executor.execute(new Runnable() {
			private AtomicLong atomicLong = new AtomicLong(0);
			@Override
			public void run() {
				System.out.println("what can I do for you..." + atomicLong.incrementAndGet());
			}
		});
		
		executor.shutdown();
	}

}

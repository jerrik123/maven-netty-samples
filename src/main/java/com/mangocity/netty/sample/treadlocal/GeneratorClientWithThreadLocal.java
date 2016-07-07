package com.mangocity.netty.sample.treadlocal;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import com.mangocity.netty.sample.treadlocal.no.Generator;
import com.mangocity.netty.sample.treadlocal.no.GeneratorImpl;

public class GeneratorClientWithThreadLocal {
	private static Generator generator = new GeneratorImpl();// 线程不安全,计数不准确
	// private static Generator generator = new GeneratorWithThreadLocal();//
	// 线程安全,采用ThreadLocal

	static CountDownLatch firstLatch = new CountDownLatch(1);
	static CountDownLatch secondLatch = new CountDownLatch(2);

	static AtomicLong counter = new AtomicLong(0);

	public static void main(String[] args) throws InterruptedException {
		Runnable clientA = new Client(generator);
		Runnable clientB = new Client(generator);
		Runnable clientC = new Client(generator);

		System.out.println("first thread start.");
		new Thread(clientA).start();
		// firstLatch.await();
		System.out.println("second thread start.");
		new Thread(clientB).start();

		// secondLatch.await();
		System.out.println("third thread start.");
		new Thread(clientC).start();

		TimeUnit.SECONDS.sleep(5);
		System.out.println("总数量=============================: " + counter.get() + " ,maxNum: " + generator.getNum());
	}

	private static class Client implements Runnable {
		private Generator generator;

		@SuppressWarnings("unused")
		public Generator getGenerator() {
			return generator;
		}

		public Client(Generator generator) {
			this.generator = generator;
		}

		@SuppressWarnings("unused")
		public void setGenerator(Generator generator) {
			this.generator = generator;
		}

		@Override
		public void run() {
			for (int i = 0; i < 30000; i++) {
				System.out.println(Thread.currentThread().getName() + "-" + generator.next());
				counter();
			}
			countDownAllLatch(firstLatch, secondLatch);
			System.out.println("ThreadLocal maxNum: " + generator.getNum());
		}

	}

	private static void counter() {
		counter.incrementAndGet();
	}

	public static void countDownAllLatch(CountDownLatch... latches) {
		for (CountDownLatch latch : latches) {
			latch.countDown();
		}
	}
}

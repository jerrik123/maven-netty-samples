package com.mangocity.netty.sample.treadlocal.no;

public class GeneratorClient {

	private static Generator generator;

	public GeneratorClient() {

	}

	public static void main(String[] args) {
		generator = new GeneratorImpl();
		ClientThread clientA = new ClientThread(generator);
		ClientThread clientB = new ClientThread(generator);
		ClientThread clientC = new ClientThread(generator);

		clientA.start();
		clientB.start();
		clientC.start();

	}

	public static class ClientThread extends Thread {
		private Generator g;

		public ClientThread(Generator g) {
			this.g = g;
		}

		@Override
		public void run() {
			for (int i = 0; i < 3; i++) {
				System.out.println(Thread.currentThread().getName() + " => " + g.next());
			}
		}
	}
}

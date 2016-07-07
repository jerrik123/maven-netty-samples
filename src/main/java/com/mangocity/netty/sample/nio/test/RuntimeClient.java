package com.mangocity.netty.sample.nio.test;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class RuntimeClient {

	public static void main(String[] args) throws UnknownHostException, IOException {
		System.out.println("CPU核心数: " + Runtime.getRuntime().availableProcessors());
		System.out.println("空闲内存: " + Runtime.getRuntime().freeMemory()/1024.0);
		System.out.println("最大内存: " + Runtime.getRuntime().maxMemory()/1024.0);
		System.out.println("总内存: " + Runtime.getRuntime().totalMemory());
	}

}

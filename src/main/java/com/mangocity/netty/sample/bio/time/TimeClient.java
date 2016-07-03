package com.mangocity.netty.sample.bio.time;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class TimeClient {

	public static void main(String[] args) throws UnknownHostException, IOException {
		System.out.println("[Client A]The Client is startup.");
		Socket socket = new Socket("localhost", 8083);
		System.out.println("[Client A]build socket connection with localhost 8083.");
		BufferedReader br = null;
		PrintWriter pw = null;
		br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		pw = new PrintWriter(socket.getOutputStream(), true);
		
		pw.println("ClientA is connect your server.");
		BufferedReader brs = new BufferedReader(new InputStreamReader(System.in));
		while (true) {
			String consoleCmd = brs.readLine();
			pw.println("ClientA Message: " + consoleCmd);
			String serverResponse = br.readLine();
			System.out.println("[Client A] receive ServerResponse. " + serverResponse);
		}
	}
}

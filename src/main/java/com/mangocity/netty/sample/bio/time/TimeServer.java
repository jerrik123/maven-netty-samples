package com.mangocity.netty.sample.bio.time;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class TimeServer {
	private static final int DEFAULT_PORT = 8083;

	public static void main(String[] args) throws IOException {
		System.out.println("[server A] server is startup.");
		int port = DEFAULT_PORT;
		try {
			if (null != args && args.length > 0) {
				port = Integer.parseInt(args[0]);
			}
		} catch (NumberFormatException e) {
			port = DEFAULT_PORT;
		}
		// 开启服务端监听
		new TimeServer().listen(port);
	}

	private void listen(int port) throws IOException {
		System.out.println("[server A] the server is listen port: " + port);
		ServerSocket ssocket = null;
		try {
			ssocket = new ServerSocket(port);
			while (true) {
				final Socket socket = ssocket.accept();
				new Thread(new TimeHandler(socket)).start();
			}
		} finally{
			if(null != ssocket){
				ssocket.close();
				ssocket = null;
			}
		}
	}

	private static class TimeHandler implements Runnable {
		private Socket socket;

		public TimeHandler(Socket socket) {
			this.socket = socket;
		}

		@Override
		public void run() {
			if(null == socket) return;
			String command = null;//客户端命令
			BufferedReader br = null;
			PrintWriter pw = null;
			try {
				br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				pw = new PrintWriter(socket.getOutputStream(), true);
				while(true){
					command = br.readLine();
					System.out.println("[server A] receive message from Client {" + command + "}");
					if("QUIT".equalsIgnoreCase(command)){
						pw.println("ByeBye~");
					}else{
						pw.println("ServerA Message: " + command);
					}
				}
			
			} catch (IOException e) {
				e.printStackTrace();
			}finally{
				try {
					if(null != br){
						br.close();
						br = null;
					}
					if(null != pw){
						pw.close();
						pw = null;
					}
					if(null != socket){
						socket.close();
						socket = null;
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

}

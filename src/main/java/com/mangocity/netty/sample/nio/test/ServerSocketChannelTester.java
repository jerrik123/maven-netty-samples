package com.mangocity.netty.sample.nio.test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class ServerSocketChannelTester {

	int port = 8080;

	private Selector selector;

	private ServerSocketChannel ssc;

	private volatile boolean stop = false;

	public static void main(String[] args) throws IOException {
		ServerSocketChannelTester launcher = new ServerSocketChannelTester();
		launcher.bind();
		launcher.exec();
	}

	private void bind() throws IOException {
		selector = Selector.open();
		ssc = ServerSocketChannel.open();
		ssc.configureBlocking(false);
		ssc.socket().bind(new InetSocketAddress(port), 1024);// 最大队列长度被设置为backlog如果队列满时收到连接指示，则拒绝该连接。
		ssc.register(selector, SelectionKey.OP_ACCEPT);
		System.out.println("The server is start up at port: " + port);
	}

	private void exec() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (!stop) {
					try {
						selector.select(1000);
						Set<SelectionKey> selectedKeys = selector.selectedKeys();
						Iterator<SelectionKey> it = selectedKeys.iterator();
						SelectionKey key = null;
						while (it.hasNext()) {
							key = it.next();
							it.remove();
							try {
								handleEvent(key);
							} catch (Exception e) {
								if (key != null) {
									key.cancel();
									if (key.channel() != null)
										key.channel().close();
								}
							}
						}
					} catch (Throwable t) {
						t.printStackTrace();
					}
				}
			}
		}).start();
	}

	/**
	 * 小心处理if的层级关系
	 * 
	 * @param key
	 * @throws IOException
	 */
	private void handleEvent(SelectionKey key) throws IOException {
		if (key.isValid()) {
			if (key.isAcceptable()) {
				ServerSocketChannel servSocetChannel = (ServerSocketChannel) key.channel();
				SocketChannel socketChannel = servSocetChannel.accept();
				socketChannel.configureBlocking(false);
				socketChannel.register(selector, SelectionKey.OP_READ);
			}
			if (key.isReadable()) {
				// Read the data
				SocketChannel sc = (SocketChannel) key.channel();
				ByteBuffer readBuffer = ByteBuffer.allocate(1024);
				int readBytes = sc.read(readBuffer);
				if (readBytes > 0) {
					readBuffer.flip();
					byte[] bytes = new byte[readBuffer.remaining()];
					readBuffer.get(bytes);
					String body = new String(bytes, "UTF-8");
					System.out.println("收到客户端请求消息 : " + body);
					String responseMsg = "您的消息服务端已经收到";
					doWrite(sc, responseMsg);
				} else if (readBytes < 0) {
					// 对端链路关闭
					key.cancel();
					sc.close();
				} else
					; // 读到0字节，忽略
			}
		}
	}

	private void doWrite(SocketChannel sc, String response) throws IOException {
		if (response != null && response.trim().length() > 0) {
			byte[] bytes = response.getBytes();
			ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
			writeBuffer.put(bytes);
			writeBuffer.flip();
			sc.write(writeBuffer);
		}
	}

}

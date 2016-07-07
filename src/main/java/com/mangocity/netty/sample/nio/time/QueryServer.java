package com.mangocity.netty.sample.nio.time;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class QueryServer {
	int port = 8080;

	public static void main(String[] args) {
		new Thread().start();
	}

	public static class NIOTimeServerThread implements Runnable {
		private int port;
		private Selector selector;
		private ServerSocketChannel servSocketChannel;

		private volatile boolean stop = false;

		NIOTimeServerThread(int port) {
			try {
				this.port = port;
				// step 1:打开一个ServerSocketChannel
				servSocketChannel = servSocketChannel.open();

				// step 2:ServerSocketChannel绑定端口
				servSocketChannel.bind(new InetSocketAddress(port), 1024);

				// step 3:配置成非阻塞模式
				servSocketChannel.configureBlocking(false);

				// step 4:打开一个selector
				selector = Selector.open();

				// step 5:注册ServerSocketChannel的ACCEPT事件到selector上,准备接受客户端请求
				servSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

				System.out.println("The QueryServer is start up at port: " + port);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void run() {
			while (!stop) {
				try {
					// step 1: 1s轮询一次
					selector.select(1000);

					// step 2: 迭代已经就绪的事件
					Set<SelectionKey> selectKeySet = selector.selectedKeys();
					Iterator<SelectionKey> iterator = selectKeySet.iterator();
					SelectionKey selectedKey = null;
					while (iterator.hasNext()) {
						selectedKey = iterator.next();
						iterator.remove();//每监听到一个事件就移除一个事件
						handleEvent(selectedKey);//处理事件
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (selector != null) {
				try {
					selector.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		private void handleEvent(SelectionKey key) throws IOException {
			if(key.isValid()){
				if(key.isAcceptable()){
					ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
					SocketChannel sc = ssc.accept();
					sc.configureBlocking(false);
					// Add the new connection to the selector
					sc.register(selector, SelectionKey.OP_READ);
				}
			}
			
		}
	}

}

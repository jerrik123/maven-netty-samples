package com.mangocity.netty.sample.treadlocal.no;

public class GeneratorImpl implements Generator {
	private static int num = 0;

	@Override
	public int next() {
		num += 1;
		return num;
	}
	
	public int getNum(){
		return num;
	}

}

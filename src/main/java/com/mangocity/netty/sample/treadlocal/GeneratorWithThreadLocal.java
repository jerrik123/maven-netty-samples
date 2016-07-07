package com.mangocity.netty.sample.treadlocal;

import com.mangocity.netty.sample.treadlocal.no.Generator;

public class GeneratorWithThreadLocal implements Generator {
	private static final ThreadLocal<Integer> CONTAINER = new ThreadLocal<Integer>() {
		@Override
		protected Integer initialValue() {
			return 0;
		}
	};

	@Override
	public int next() {
		CONTAINER.set(CONTAINER.get() + 1);
		return CONTAINER.get();
	}

	@Override
	public int getNum() {
		return CONTAINER.get();
	}
	
	

}

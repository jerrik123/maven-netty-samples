package com.mangocity.netty.sample.treadlocal.conn;

public interface IProductService {
	
	int updateProduct(Long productId,Double price);
	
	int updateProductWithThreadLocal(Long productId,Double price);
}

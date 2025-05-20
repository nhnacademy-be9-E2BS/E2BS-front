package com.nhnacademy.front.product.product.adaptor;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "product-service", url = "${product.book.member.url}")
public interface ProductAdaptor {
}

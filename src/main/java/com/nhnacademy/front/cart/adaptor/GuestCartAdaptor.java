package com.nhnacademy.front.cart.adaptor;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "guest-cart-adaptor", url = "${guest.url}")
public interface GuestCartAdaptor {
}

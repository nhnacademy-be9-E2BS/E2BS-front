package com.nhnacademy.front.cart.service.impl;

import org.springframework.stereotype.Service;

import com.nhnacademy.front.cart.adaptor.GuestCartAdaptor;
import com.nhnacademy.front.cart.service.GuestCartService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GuestCartServiceImpl implements GuestCartService {

	private final GuestCartAdaptor guestCartAdaptor;



}

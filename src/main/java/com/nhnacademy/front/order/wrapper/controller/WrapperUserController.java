package com.nhnacademy.front.order.wrapper.controller;

import org.springframework.stereotype.Controller;

import com.nhnacademy.front.order.wrapper.service.WrapperService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class WrapperUserController {

	private final WrapperService wrapperService;


}

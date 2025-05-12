package com.nhnacademy.front.order.wrapper.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.nhnacademy.front.order.wrapper.service.WrapperService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/mypage/wrappers")
public class WrapperAdminController {

	private final WrapperService wrapperService;


}

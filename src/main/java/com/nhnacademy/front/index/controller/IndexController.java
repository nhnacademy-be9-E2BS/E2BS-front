// package com.nhnacademy.front.index.controller;
//
// import org.springframework.stereotype.Controller;
// import org.springframework.ui.Model;
// //
// // import com.nhnacademy.front.common.annotation.JwtTokenCheck;
// // import com.nhnacademy.front.index.service.IndexService;
// //
// // import lombok.RequiredArgsConstructor;
// //
// // @Controller
// // @RequiredArgsConstructor
// // public class IndexController {
// //
// // 	private final IndexService indexService;
// //
// // 	@GetMapping("/")
// // 	@JwtTokenCheck
// // 	public String getIndex(Model model) {
// // 		model.addAttribute("BestSellerList", indexService.getBestSellerProducts());
// // 		model.addAttribute("BlogBestList", indexService.getBlogBestProducts());
// // 		model.addAttribute("ItemNewAllList", indexService.getNewItemsProducts());
// // 		model.addAttribute("NewSpecialItemsList", indexService.getNewSpecialItemsProducts());
// // 		model.addAttribute("memberName", "회원 이름");
// //
// // 		return "home";
// // 	}
// // }

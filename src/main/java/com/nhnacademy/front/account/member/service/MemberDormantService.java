package com.nhnacademy.front.account.member.service;

import java.time.Duration;
import java.util.Objects;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.nhnacademy.front.account.member.adaptor.DoorayAdaptor;
import com.nhnacademy.front.account.member.adaptor.MemberDormantAdaptor;
import com.nhnacademy.front.account.member.exception.DormantProcessingException;
import com.nhnacademy.front.account.member.model.dto.request.RequestDoorayAuthenticationDTO;
import com.nhnacademy.front.account.member.model.dto.request.RequestDormantDoorayNumberDTO;
import com.nhnacademy.front.account.member.model.dto.request.RequestDormantEmailNumberDTO;
import com.nhnacademy.front.account.member.model.dto.response.ResponseMemberEmailDTO;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberDormantService {

	private final RedisTemplate<String, Object> redisTemplate;
	private final MemberDormantAdaptor memberDormantAdaptor;
	private final DoorayAdaptor doorayAdaptor;
	private final JavaMailSender javaMailSender;

	public String createDoorayAuthenticationNumber(String memberId) {
		int authenticationNumber = (int)(Math.random() * 900000) + 100000;

		String doorayDormantKey = "doorayDormantNumber:" + memberId;
		redisTemplate.opsForValue().set(doorayDormantKey, authenticationNumber, Duration.ofSeconds(180));

		return String.valueOf(authenticationNumber);
	}

	public String createEmailAuthenticationNumber(String memberId) {
		int authenticationNumber = (int)(Math.random() * 900000) + 100000;

		String emailDormantKey = "emailDormantNumber:" + memberId;
		redisTemplate.opsForValue().set(emailDormantKey, authenticationNumber, Duration.ofSeconds(180));

		return String.valueOf(authenticationNumber);
	}

	public boolean checkDoorayAuthenticationNumber(RequestDormantDoorayNumberDTO requestDormantDoorayNumberDTO,
		String memberId) {
		String doorayDormantKey = "doorayDormantNumber:" + memberId;
		if (Objects.isNull(redisTemplate.opsForValue().get(doorayDormantKey))) {
			throw new DormantProcessingException("휴면 해제 과정에서 오류가 발생했습니다. 다시 시도해 주세요.");
		}

		Integer number = (Integer)redisTemplate.opsForValue().get(doorayDormantKey);
		Integer authenticationNumber = Integer.parseInt(requestDormantDoorayNumberDTO.getDormantDoorayNumber());

		return Objects.equals(number, authenticationNumber);
	}

	public boolean checkEmailAuthenticationNumber(RequestDormantEmailNumberDTO requestDormantEmailNumberDTO,
		String memberId) {
		String emailDormantKey = "emailDormantNumber:" + memberId;
		if (Objects.isNull(redisTemplate.opsForValue().get(emailDormantKey))) {
			throw new DormantProcessingException("휴면 해제 과정에서 오류가 발생했습니다. 다시 시도해 주세요.");
		}

		Integer number = (Integer)redisTemplate.opsForValue().get(emailDormantKey);
		Integer authenticationNumber = Integer.parseInt(requestDormantEmailNumberDTO.getDormantEmailNumber());

		return Objects.equals(number, authenticationNumber);
	}

	public void sendDoorayMessageAuthenticationNumber(RequestDoorayAuthenticationDTO requestDoorayAuthenticationDTO) {
		doorayAdaptor.sendDoorayAuthenticationNumber(requestDoorayAuthenticationDTO,
			"3204376758577275363", "4081630330794795988", "b42JstgjQf-uqLBb3dj_yg");
	}

	public void changeMemberStateActive(String memberId, HttpServletRequest request) {
		ResponseEntity<Void> response = memberDormantAdaptor.changeDormantMemberStateActive(memberId);
		if (!response.getStatusCode().is2xxSuccessful()) {
			throw new DormantProcessingException("휴면 해제 과정에서 오류가 발생했습니다. 다시 시도해 주세요.");
		}

		if (request.getSession().getAttribute("dormantMemberId") != null) {
			request.getSession().removeAttribute("dormantMemberId");
		}

		if (request.getSession().getAttribute("memberState") != null) {
			request.getSession().removeAttribute("memberState");
		}

	}

	/**
	 * 회원의 이메일을 가져오는 메서드
	 */
	public String getMemberEmail(String memberId) {
		ResponseEntity<ResponseMemberEmailDTO> response = memberDormantAdaptor.getMemberEmail(memberId);
		if (!response.getStatusCode().is2xxSuccessful() || Objects.isNull(response.getBody())) {
			throw new DormantProcessingException("휴면 해제 과정에서 오류가 발생했습니다. 다시 시도해 주세요.");
		}

		return response.getBody().getCustomerEmail();
	}

	/**
	 * 해당 회원에게 이메일을 보내는 메서드
	 */
	public void sendEmailAuthenticationNumber(String customerEmail, String authenticationNumber) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(customerEmail);
		message.setSubject("[E2BS] 인증번호 안내");
		message.setText(String.format("[E2BS] 인증번호 [%s] 타인에게 알려주지 마세요.\n3분 이내에 입력해주세요.", authenticationNumber));
		javaMailSender.send(message);
	}

}

package com.kyh.system.controller;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.kyh.system.mapper.UserAccessLogMapper;
import com.kyh.system.model.UserAccessLog;
import com.kyh.system.model.UserAuth;
import com.kyh.system.service.UserService;

@Controller
public class LoginController {

	@Autowired
	private UserService userService;

	@GetMapping("/")
    public RedirectView redirectToLogin() {
        return new RedirectView("/login/");
    }

	@RequestMapping(value = "/login", method = { RequestMethod.POST, RequestMethod.GET })
	public String login() {
		return "/login/login";
	}

	//userlogin
	@RequestMapping(value = "/login/userLogin", method = { RequestMethod.POST, RequestMethod.GET })
	public ModelAndView userLogin(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		ModelAndView model = new ModelAndView();
		UserAuth userAuth = new UserAuth();
		userAuth.setUserCode(request.getParameter("userCode"));
		userAuth.setPassword(request.getParameter("password"));

		UserAuth result = userService.getUserByUserCode(userAuth.getUserCode());

		if (result == null) {
		    model.addObject("MSG", "該当ユーザーが存在しません。");
		    model.setViewName("/login/login");
		    return model;
		}
		String md5Password = DigestUtils
		        .md5DigestAsHex(userAuth.getPassword().getBytes(StandardCharsets.UTF_8))
		        .toUpperCase();

		if (!result.getPassword().equals(md5Password)) {
		    model.addObject("MSG", "パスワードが間違っています。");
		    model.setViewName("/login/login");
		    return model;
		}

		session.setAttribute("user", result);
		
		UserAccessLog accessLog = new UserAccessLog();
		accessLog.setUserId(result.getUserId());
		accessLog.setGamenId("login.jsp");
		accessLog.setStartTime(new Date());
		userAccessLogMapper.insert(accessLog);

		Cookie cookie = new Cookie("userCode", result.getUserCode());
		cookie.setPath("/");
		response.addCookie(cookie);

		model.setViewName("login/index");
		return model;
	}
	
	@Autowired
	private UserAccessLogMapper userAccessLogMapper;

	// index.html
	//	<div class="content">
	//	    <iframe src="welcome" id="iframe" width="100%" height="100%" frameborder="0"></iframe>
	//  </div>
	@RequestMapping(value = "/login/welcome", method = { RequestMethod.POST, RequestMethod.GET })
	public String welcome() {
		return "/login/welcome";
	}
}

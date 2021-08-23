
package com.te.timex.controller;

import java.io.UnsupportedEncodingException;

import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sun.mail.imap.Utility;
import com.sun.xml.messaging.saaj.packaging.mime.MessagingException;
import com.te.timex.model.User;
import com.te.timex.repository.UserRepository;
import com.te.timex.service.UserService;

import javassist.NotFoundException;
import net.bytebuddy.utility.RandomString;

@Controller
@RequestMapping("/account")
public class AccountController {
	@Autowired
	private UserService userService;

	@Autowired
	private UserRepository userRepository;
	
    @Autowired
    private JavaMailSender mailSender;

	@GetMapping("/login")
	public String login() {
		return "account/login";// login 페이지로 이동
	}

	@PostMapping("/loginInfo")
	public ResponseEntity loginInfo(Authentication authentication) {

		// 로그인한 정보로 email주소 가져온다
		String email = authentication.getName();

		// 로그인한 email정보로 user_id 가져온다
		User user = userRepository.findByEmail(email);
		int user_id = user.getId();

		return new ResponseEntity(user_id, HttpStatus.OK);

	}

	@GetMapping("/register")
	public String register() {
		System.out.println("register");
		return "account/register";// register페이지로 이동
	}


	@GetMapping("/forgot_password")
	public String showForgotPasswordForm() {
		// https://www.codejava.net/frameworks/spring-boot/spring-security-forgot-password-tutorial
	    return "account/password";
	}

     
	@PostMapping("/forgot_password")
	public String processForgotPassword(HttpServletRequest request, Model model) throws javax.mail.MessagingException {
	
		String email = request.getParameter("email");
	    String token = RandomString.make(30);
	     
	    try {
	        userService.updateResetPasswordToken(token, email);
	
	        String siteURL = request.getRequestURL().toString();
            String getsiteURL =  siteURL.replace(request.getServletPath(), "");
	        String resetPasswordLink = getsiteURL + "/account/reset_password?token=" + token;
	        System.out.println("here");
System.out.println(resetPasswordLink);
	        sendEmail("kellylee30043@gmail.com", resetPasswordLink);

	        model.addAttribute("message", "We have sent a reset password link to your email. Please check.");
	         
	    } catch (NotFoundException ex) {
	        model.addAttribute("error", ex.getMessage());
	    } catch (UnsupportedEncodingException | MessagingException e) {
	        model.addAttribute("error", "Error while sending email");
	    }
	         
	    return "account/password";
	}

	@GetMapping("/reset_password")
	public String showResetPasswordForm(@Param(value = "token") String token, Model model) {
		System.out.println("get reset_password");
	    User user = userService.getByResetPasswordToken(token);
	    model.addAttribute("token", token);
	     
	    if (user == null) {
	        model.addAttribute("message", "Invalid Token");
	        return "message";
	    }
	     
	    return "account/resetpassword";
	}
	
	@PostMapping("/reset_password")
	public String processResetPassword(HttpServletRequest request, Model model) {
		System.out.println("post reset_password");
	    String token = request.getParameter("token");
	    String password = request.getParameter("password");
	     System.out.println("here password = "+password);
	    User user = userService.getByResetPasswordToken(token);
	    System.out.println(user.toString());
	    model.addAttribute("title", "Reset your password");
	     
	    if (user == null) {
	        model.addAttribute("message", "Invalid Token");
	     //   return "message";
	    } else {           
	    	userService.updatePassword(user, password);
	         
	        model.addAttribute("message", "You have successfully changed your password.");
	    }
	     
	    return "redirect:/account/login";
	}
	
	public void sendEmail(String recipientEmail, String link)
            throws MessagingException, UnsupportedEncodingException, javax.mail.MessagingException {
        MimeMessage message = mailSender.createMimeMessage();              
        MimeMessageHelper helper = new MimeMessageHelper(message);
     //   System.out.println("***************************5");
        helper.setFrom("contact@shopme.com", "Shopme Support");
        helper.setTo(recipientEmail);
      
        String subject = "Here's the link to reset your password";
         System.out.println("link = "+link);
        String content = "<p>Hello,</p>"
                + "<p>You have requested to reset your password.</p>"
                + "<p>Click the link below to change your password:</p>"
                + "<p><a href=\"" + link + "\">Change my password</a></p>"
                + "<br>"
                + "<p>Ignore this email if you do remember your password, "
                + "or you have not made the request.</p>";
         
        helper.setSubject(subject);

        helper.setText(content, true);

        mailSender.send(message);
    }

	@PostMapping("/register")
	public String register(User user) {
		System.out.println("here");
		System.out.println(user);
		userService.save(user);
		return "redirect:/";
	}
}
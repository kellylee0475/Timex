
package com.te.timex.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.sun.xml.messaging.saaj.packaging.mime.MessagingException;
import com.te.timex.model.User;
import com.te.timex.repository.UserRepository;
import com.te.timex.service.UserService;
import com.te.timex.validator.UserValidator;

import javassist.NotFoundException;
import net.bytebuddy.utility.RandomString;

@Controller
@RequestMapping("/account")
public class AccountController {
	
	@Value("${profilePhoto_path}")
	private String download_path;
	
	@Autowired
	private UserService userService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private UserValidator userValidator;

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

	@GetMapping("/profile")
	public String profile(Authentication authentication, Model model) {
		// 로그인한 정보로 email주소 가져온다
		String email = authentication.getName();

		// 로그인한 email정보로 user_id 가져온다
		User user = userRepository.findByEmail(email);
		System.out.println("profile controller");
		System.out.println(user);
		// System.out.println(user.toString());
		model.addAttribute(user);
		return "account/profile";
	}

	@PostMapping("/profile")
	public String saveProfile(Model model, @Valid User user, BindingResult bindingResult) {
	
		
		System.out.println("postmapping");
		userValidator.validate(user, bindingResult);
		if (bindingResult.hasErrors()) {
			System.out.println("has errors");
			return "account/profile";
		}
		int id = user.getId();
		String firstname = user.getFirstname();
		String lastname = user.getLastname();
		String email = user.getEmail();
		String address = user.getAddress();
		int zipcode = user.getZipcode();
		String memo = user.getMemo();
		String city = user.getCity();

		userRepository.setUserInfoById(firstname, lastname, email, address, zipcode, memo, city, id);
		model.addAttribute("message", "Changes have been saved");
		return "account/profile";
	}

	@PostMapping("/photo")
	public String savePhoto(User user, @RequestParam("image") MultipartFile multipartFile) throws IOException {
	
		int id = user.getId();
		String ext = FilenameUtils.getExtension(multipartFile.getOriginalFilename());
		String fileName = "profilePhoto_userId" + id+"."+ext;
		String upload_path = download_path;

		userRepository.setUserInfoById(fileName,upload_path, id);

		saveFile(upload_path, fileName, multipartFile);

		return "redirect:/account/profile";
	}

	public static void saveFile(String upload_path, String fileName, MultipartFile multipartFile) throws IOException {
		Path uploadPath = Paths.get(upload_path);

		if (!Files.exists(uploadPath)) {
			Files.createDirectories(uploadPath);
		}
	
		try (InputStream inputStream = multipartFile.getInputStream()) {
	
			Path filePath = uploadPath.resolve(fileName);
		
			Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException ioe) {
			throw new IOException("Could not save image file: " + fileName, ioe);
		}
	
	}

	@GetMapping("/register")
	public String register() {
	
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
			String getsiteURL = siteURL.replace(request.getServletPath(), "");
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
		System.out.println("here password = " + password);
		User user = userService.getByResetPasswordToken(token);
		System.out.println(user.toString());
		model.addAttribute("title", "Reset your password");

		if (user == null) {
			model.addAttribute("message", "Invalid Token");
			System.out.println("here? no token");
			// return "error/error";
		} else {
			userService.updatePassword(user, password);

			model.addAttribute("message", "You have successfully changed your password.");
		}

		// return "redirect:/account/login";
		return "error/error";
	}

	public void sendEmail(String recipientEmail, String link)
			throws MessagingException, UnsupportedEncodingException, javax.mail.MessagingException {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		// System.out.println("***************************5");
		helper.setFrom("contact@shopme.com", "Shopme Support");
		helper.setTo(recipientEmail);

		String subject = "Here's the link to reset your password";
		System.out.println("link = " + link);
		String content = "<p>Hello,</p>" + "<p>You have requested to reset your password.</p>"
				+ "<p>Click the link below to change your password:</p>" + "<p><a href=\"" + link
				+ "\">Change my password</a></p>" + "<br>" + "<p>Ignore this email if you do remember your password, "
				+ "or you have not made the request.</p>";

		helper.setSubject(subject);

		helper.setText(content, true);

		mailSender.send(message);
	}

	@PostMapping("/register")
	public String register(@Valid User user, BindingResult result, Model model) {
System.out.println(user.getEmail());
		User check = userRepository.findByEmail(user.getEmail());
		System.out.println("here2");
		
		try {

			if(check.equals(null) || check==null ) {
			
					if (result.hasErrors()) {
						System.out.println("has error");
						return "redirect:/account/register";
					}

					userService.save(user);
					return "account/login";
					
				}else {			
					model.addAttribute("message","Already exisiting email");
					
					return "account/register";
				} 
	        
	    } catch (Exception e) {
	        e.printStackTrace();
	        System.out.println("Idon'tknow");
	        System.out.println(user.toString());
	        userService.save(user);
			return "account/login";
	       
	    }
	
		
	
	}
}
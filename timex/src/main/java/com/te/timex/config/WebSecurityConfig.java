package com.te.timex.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity // Spring Security를 활성화한다는 의미의 어노테이션입니다.
@EnableGlobalAuthentication
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {// WebSecurityConfigurerAdapter는 Spring Security의
																		// 설정파일로서의 역할을 하기 위해 상속해야 하는 클래스입니다.

	@Autowired
	private DataSource dataSource; // application.properties에서 사용한 database

	@Override
	public void configure(WebSecurity web) { // static 하위 폴더 (css, js, img)는 무조건 접근이 가능해야하기 때문에 인증을 무시해야합니다.
		web.ignoring().antMatchers("/css/**", "/js/**", "/img/**", "/assets/**");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable().authorizeRequests()
				.antMatchers("/", "/account/register", "/account/forgot_password", "/account/reset_password")
				.permitAll().anyRequest().authenticated().and().formLogin().loginPage("/account/login").permitAll()
				.defaultSuccessUrl("/time") // 로그인 성공 후 리다이렉트 주소
				// .failureUrl("/login?error=true")//if login fail
				.and()
				.logout().permitAll();
		// .http.csrf().disable();
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.jdbcAuthentication().dataSource(dataSource).passwordEncoder(passwordEncoder())
				.usersByUsernameQuery("select email, password, enabled from users where email=?")
				.authoritiesByUsernameQuery(
						"select u.email, r.name " + "from user_role ur inner join users u on ur.user_id=u.id "
								+ "inner join role r on ur.role_id=r.id " + "where u.email = ?;");
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
// private final LoginService loginService; // 후에 사용할 유저 정보를 가져올 클래스입니다.
//------------------------------------------------------------------------------------------------------------------------------------------------------	
//	 @Override
//	  public void configure(WebSecurity web) { // static 하위 폴더 (css, js, img)는 무조건 접근이 가능해야하기 때문에 인증을 무시해야합니다.
//	    web.ignoring().antMatchers("/css/**", "/js/**", "/img/**");
//	  }
//
// 
//   @Override
//   protected void configure(HttpSecurity http) throws Exception{
//	   http
//       .authorizeRequests() // anyMatchers를 통해 경로 설정과 권한 설정이 가능
//         .antMatchers("/login", "/signup", "/user").permitAll() // permitAll() : 누구나 접근이 가능
// //        .antMatchers("/").hasRole("USER") // USER, ADMIN만 접근 가능
////         .antMatchers("/admin").hasRole("ADMIN") // hasRole() : 특정 권한이 있는 사람만 접근 가능
//         .anyRequest().authenticated() // 나머지 요청들은 권한의 종류에 상관 없이 권한이 있어야 접근 가능
//     .and() 
//       .formLogin() // 로그인에 관한 설정을 의미합니다.
//         .loginPage("/login") // 로그인 페이지 링크
//         .failureUrl("/login?error=true")//if login fail
//         .defaultSuccessUrl("/home") // 로그인 성공 후 리다이렉트 주소
//    .and()
//       .logout() // 로그아웃에 관한 설정을 의미합니다.
//        .logoutSuccessUrl("/login") // 로그아웃 성공시 리다이렉트 주소
//	    .invalidateHttpSession(true) // 로그아웃 이후 세션 전체 삭제 여부
// ;
//	   System.out.println("This is WebSecurityConfig File");
//   }
//   

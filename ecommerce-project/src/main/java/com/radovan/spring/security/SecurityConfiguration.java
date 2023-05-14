package com.radovan.spring.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.radovan.spring.security.handler.LoginSuccessHandler;
import com.radovan.spring.service.impl.UserDetailsImpl;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

	@Bean
	public AuthenticationManager authenticationManager() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService());
		authProvider.setPasswordEncoder(passwordEncoder());
		return new ProviderManager(authProvider);
	}

	@Bean
	protected SecurityFilterChain configure(HttpSecurity http) throws Exception {

		http.formLogin()

				.loginPage("/login").successHandler(new LoginSuccessHandler()).loginProcessingUrl("/login")
				.usernameParameter("email").passwordParameter("password").permitAll();

		http.logout().permitAll().logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
				.logoutSuccessUrl("/login?logout").and().csrf().disable();

		http

				.authorizeHttpRequests()
				.requestMatchers("/login").anonymous()
				.requestMatchers("/loginErrorPage", "/products/**", "/aboutUs", "/suspensionPage").permitAll()
				.requestMatchers("/", "/home", "/registerComplete", "/registerFail", "/loginPassConfirm",
						"/suspensionChecker").permitAll()
				.requestMatchers("/get*").hasAnyAuthority("ADMIN", "ROLE_USER")
				.requestMatchers("/admin/**").hasAuthority("ADMIN")
				.requestMatchers("/cart/**", "/order/**").hasAuthority("ROLE_USER")
				.requestMatchers("/userRegistration").anonymous().requestMatchers("/register").anonymous()
				.anyRequest().authenticated();

		return http.build();
	}

	@Bean
	public UserDetailsService userDetailsService() {
		return new UserDetailsImpl();
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public WebSecurityCustomizer resourcesCustomizer() {
		return (web) -> web.ignoring().requestMatchers("/resources/**", "/static/**", "/images/**", "/css/**",
				"/js/**");
	}

}

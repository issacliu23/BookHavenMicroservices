package com.ncs.nusiss.userservice;

import com.ncs.nusiss.userservice.entity.AppUser;
import com.ncs.nusiss.userservice.service.AppUserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@EnableEurekaClient
public class UserServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserServiceApplication.class, args);
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public WebClient.Builder getWebClientBuilder() {
		return WebClient.builder();
	}
	/*
	@Bean
	CommandLineRunner run (AppUserService appUserService) {
		return args -> {
			appUserService.signUpUser(new AppUser("johndoe@gmail.com", "1234"));
			appUserService.signUpUser(new AppUser("joebiden@gmail.com", "1234"));
			appUserService.signUpUser(new AppUser("donaldtrump@gmail.com", "1234"));
			appUserService.signUpUser(new AppUser("willsmith@gmail.com", "1234"));
		};
	}
	*/

}

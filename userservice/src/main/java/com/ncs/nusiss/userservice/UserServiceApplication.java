package com.ncs.nusiss.userservice;

import com.ncs.nusiss.userservice.entity.User;
import com.ncs.nusiss.userservice.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.PostConstruct;

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
	CommandLineRunner run (UserService userService) {
		return args -> {
			userService.saveUser(new User(null, "johndoe@gmail.com", "1234"));
			userService.saveUser(new User(null, "joebiden@gmail.com", "1234"));
			userService.saveUser(new User(null, "donaldtrump@gmail.com", "1234"));
			userService.saveUser(new User(null, "willsmith@gmail.com", "1234"));
		};
	}

}

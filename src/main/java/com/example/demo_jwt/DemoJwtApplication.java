package com.example.demo_jwt;

import com.example.demo_jwt.constants.Constants;
import com.example.demo_jwt.dto.RoleDTO;
import com.example.demo_jwt.dto.UserDTO;
import com.example.demo_jwt.models.Role;
import com.example.demo_jwt.models.User;
import com.example.demo_jwt.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;

@SpringBootApplication
public class DemoJwtApplication {
	private static final String ADMIN = "admin";
	public static void main(String[] args) {
		SpringApplication.run(DemoJwtApplication.class, args);
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	@Bean
	CommandLineRunner run(UserService userService) {
		return args -> {
			// create role
			userService.saveRole(new RoleDTO(null, Constants.ROLE_USER));
			userService.saveRole(new RoleDTO(null, Constants.ROLE_ADMIN));

			// create user
			userService.saveUser(new UserDTO(null, "Dam Tan", ADMIN, "1234", new ArrayList<>()));
			userService.saveUser(new UserDTO(null, "Nguyen Hung", "user", "1234", new ArrayList<>()));

			// add role to user
			userService.addRoleToUser(ADMIN, Constants.ROLE_ADMIN);
			userService.addRoleToUser(ADMIN, Constants.ROLE_USER);
			userService.addRoleToUser("user",  Constants.ROLE_USER);
		};
	}
}

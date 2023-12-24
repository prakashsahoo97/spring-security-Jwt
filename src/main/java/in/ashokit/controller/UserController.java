package in.ashokit.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.ashokit.entitty.User;
import in.ashokit.payload.UserRequest;
import in.ashokit.payload.UserResponse;
import in.ashokit.service.UserService;
import in.ashokit.util.JwtUtil;

@RestController
@RequestMapping("/user")
public class UserController {
	@Autowired
	private UserService userService;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtUtil jwtUtil;

	@PostMapping("/save")
	public ResponseEntity<String> saveUser(@RequestBody User user) {
		userService.saveUser(user);
		return ResponseEntity.ok("User created");

	}

	@PostMapping("/login")
	public ResponseEntity<UserResponse> loginUser(@RequestBody UserRequest userRequest) {

		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(userRequest.getUsername(), userRequest.getPassword()));

		String token = jwtUtil.generateToken(userRequest.getUsername());

		return ResponseEntity.ok(new UserResponse(token, "GENERATED BY MR.RAGHU -NIT"));
	}

	@PostMapping("/welcome")
	public ResponseEntity<String> accessUserData(Principal p) {
		return ResponseEntity.ok("Hello user:" + p.getName());
	}

}
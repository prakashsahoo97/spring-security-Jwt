package in.ashokit.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import in.ashokit.entitty.User;
import in.ashokit.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {
	@Autowired
	private UserRepository userRepo;
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Override
	public Integer saveUser(User user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		return userRepo.save(user).getUserId();
	}

	@Override
	public User findByUsername(String username) {
		Optional<User> user = userRepo.findByEmail(username);
		if (user.isPresent())
			return user.get();
		return null;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		User user = findByUsername(username);
		if (user == null)
			throw new UsernameNotFoundException(
					new StringBuffer().append("User name ").append(username).append(" not found!").toString());

		List<GrantedAuthority> authorities = user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role))
				.collect(Collectors.toList());

		return new org.springframework.security.core.userdetails.User(username, user.getPassword(), authorities);
	}
	
	

}

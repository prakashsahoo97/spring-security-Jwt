package in.ashokit.service;

import in.ashokit.entitty.User;

public interface UserService {
	Integer saveUser(User user);
	User findByUsername(String userName);
}

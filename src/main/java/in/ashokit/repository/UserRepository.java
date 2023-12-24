package in.ashokit.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import in.ashokit.entitty.User;


public interface UserRepository extends JpaRepository<User, Integer> {
	Optional<User> findByEmail(String email);

}

package simpleAuction.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import simpleAuction.entity.User;

public interface UserRepository extends CrudRepository<User, Long> {

	Page<User> findAll(Pageable pageable);
	Page<User> findByLastNameContainsIgnoreCase(String lastName, Pageable pageable);
	User findFirstByLogin(String login);
}

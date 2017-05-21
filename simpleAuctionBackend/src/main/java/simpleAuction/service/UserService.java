package simpleAuction.service;

import java.util.HashMap;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import simpleAuction.SimpleAuctionBackendApplication;
import simpleAuction.entity.User;
import simpleAuction.repository.UserRepository;
import simpleAuction.utils.UserForm;

@Service
@Transactional
public class UserService {

	@Autowired
	UserRepository userRepo;
	
	@Autowired
	SimpleAuctionBackendApplication config;
	
	public Page<User> findAll(PageRequest page) {
		return userRepo.findAll(page);
	}

	public Page<User> findByLastName(String lastname, PageRequest page) {
		return userRepo.findByLastNameContainsIgnoreCase(lastname, page);
	}
	
	public User save(UserForm user) {
		User newUser = new User(user.getFirstName(), user.getLastName(), user.getLogin(), user.getPassword());
		
		return userRepo.save(newUser);
	}
	
	public Map<String,String> authenticate(UserForm user) {
		User existingUser = userRepo.findFirstByLogin(user.getLogin());
		Map<String,String> out = new HashMap<>();
		if (existingUser != null && user.getPassword().equals(existingUser.getPassword())) {
			out.put("Message", "Successfully authenticated");
			out.put("Type", "Success");
			out.put("AuthKey", config.basicAuthPassword());
			out.put("UserID", existingUser.getId().toString());
			out.put("UserLogin", existingUser.getLogin());
			return out;
		} else {
			out.put("Message", "Failed to authenticate");
			out.put("Type", "Failure");
			return out;
		}
			
	}

}

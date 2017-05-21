package simpleAuction.controller;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import simpleAuction.entity.User;
import simpleAuction.service.UserService;
import simpleAuction.utils.UserForm;

@CrossOrigin
@RestController
public class UserController {

	@Autowired
	UserService userService;

	@RequestMapping(value = "/users", method = RequestMethod.GET)
	public Page<User> users(
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "limit", defaultValue = "10") int limit) {
		
		return userService.findAll(new PageRequest(page, limit));
	}

	@RequestMapping(value = "/user", method = RequestMethod.GET)
	public Page<User> userByName(
			@RequestParam(value = "name") String lastname,
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "limit", defaultValue = "10") int limit) {
		
		return userService.findByLastName(lastname, new PageRequest(page, limit));
	}
	
	@RequestMapping(value = "/user", method = RequestMethod.POST)
	public User userAdd(@RequestBody final @Valid UserForm user) {
		
		return userService.save(user);
	}
	
	@RequestMapping(value = "/user/login", method = RequestMethod.POST)
	public Map<String,String> userLogin(@RequestBody final UserForm user) {
		return userService.authenticate(user);
	}

}

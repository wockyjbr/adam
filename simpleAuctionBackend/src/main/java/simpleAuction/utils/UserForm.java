package simpleAuction.utils;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class UserForm {
	
	@NotNull
    @Size(min=2, max=30)
	private String firstName;
	
	@NotNull
    @Size(min=2, max=50)
	private String lastName;
	
	@NotNull
    @Size(min=2, max=30)
	private String login;
	
	@NotNull
	@Size(min=3, max=30)
	private String password;

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "UserForm [firstName=" + firstName + ", lastName=" + lastName + ", login=" + login + "]";
	}


}

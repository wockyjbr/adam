package simpleAuction;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import javax.ws.rs.HttpMethod;

import org.h2.server.web.WebServlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import simpleAuction.entity.Auction;
import simpleAuction.entity.User;
import simpleAuction.repository.AuctionRepository;
import simpleAuction.repository.UserRepository;

@SpringBootApplication
public class SimpleAuctionBackendApplication {
	
	private String uuid  = UUID.randomUUID().toString();
	
	public static void main(String[] args) {
		System.out.println("Starting SimpleAuctionBackendApplication");
		SpringApplication.run(SimpleAuctionBackendApplication.class, args);
		System.out.println("SimpleAuctionBackendApplication started. Go to: http://localhost:8080");
	}
	
	@Bean
	public ServletRegistrationBean h2servletRegistration() {
	    ServletRegistrationBean registration = new ServletRegistrationBean(new WebServlet());
	    registration.addUrlMappings("/console/*");
	    return registration;
	}
	
	@Bean
	public String basicAuthPassword(){
		return uuid;
	}
	
	@Configuration
	@EnableWebSecurity
	public class SecurityConfig extends WebSecurityConfigurerAdapter {

		@Autowired
		public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
			auth.inMemoryAuthentication().withUser("user").password(basicAuthPassword()).roles("USER");
		}

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http
			.csrf().disable()
			.authorizeRequests()
			.antMatchers(HttpMethod.OPTIONS,"/**").permitAll()
			.antMatchers("/user/login").permitAll()
			.antMatchers(HttpMethod.POST, "/user").permitAll()
			.and().formLogin();
		}
	}
	
	@Bean
	public CommandLineRunner demo(UserRepository userRepo, AuctionRepository auctionRepo) {	
		return (args) -> {
			System.out.println("Loading initial data to H2 database:");
			System.out.println("-------------------------------");
			
			User user1 = new User("John", "Doe", "johnyD", "123");
			User user2 = new User("Elton", "John", "ej", "123");
			User user3 = new User("Pani", "Basia", "baska", "aaa");
			User user4 = new User("Will", "Smith", "willie", "bbb");
			User user5 = new User("Wilson", "Smith", "willie", "bbb");
			userRepo.save(user1);
			userRepo.save(user2);
			userRepo.save(user3);
			userRepo.save(user4);
			userRepo.save(user5);

			System.out.println("Initial Users:");
			System.out.println("-------------------------------");
			for (User user : userRepo.findAll())
				System.out.println(user.toString());
			
			Date today = new Date();
			Date tomorrow = new Date();
			Date in2days = new Date();
			Calendar c = Calendar.getInstance(); 
			c.setTime(tomorrow); 
			c.add(Calendar.DATE, 1);
			tomorrow = c.getTime();
			c.setTime(in2days); 
			c.add(Calendar.DATE, 2);
			in2days = c.getTime();
			auctionRepo.save(new Auction("Old carpet", 300.0, "Beautiful old carpet from 1749", true, today, tomorrow, user1));
			auctionRepo.save(new Auction("Bike - like a new one!", 200.0, "This is a bike, old but looks like new.", true, today, tomorrow, user1));
			auctionRepo.save(new Auction("Jimmy Hendrix Vinyl", 20.0, "Vinyl.", true, today, in2days, user5));
			auctionRepo.save(new Auction("Brand new Toaster", 28.0, "Made in China.", true, today, in2days, user4));
			auctionRepo.save(new Auction("Turbo Chewing Gum", 28.0, "mMMMmmmmmm", true, today, in2days, user4));
			auctionRepo.save(new Auction("Something really amazing", 28.0, ":)", true, today, in2days, user4));
			Auction closedAuction = new Auction("A bag of dirt", 1.0, "Bag 100% cotton. Dirt inside.", true, today, today, user2);
			closedAuction.setIsOpen(false);
			auctionRepo.save(closedAuction);
			Auction auctionWithBidders = new Auction("TicTac from 1986", 500.0, "TicTac.", true, today, tomorrow, user2);
			auctionWithBidders.setBidders(Arrays.asList(user3, user4, user3, user5, user3, user5, user3));
			auctionWithBidders.setTopBidder(user3);
			auctionRepo.save(auctionWithBidders);
			
			System.out.println("Initial Auctions:");
			System.out.println("-------------------------------");
			for (Auction auction : auctionRepo.findAll())
				System.out.println(auction.toString());

		};
	}
	
}

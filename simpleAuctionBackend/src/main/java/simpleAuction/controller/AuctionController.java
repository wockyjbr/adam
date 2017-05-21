package simpleAuction.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import simpleAuction.entity.Auction;
import simpleAuction.entity.User;
import simpleAuction.service.AuctionService;
import simpleAuction.utils.AuctionForm;
import simpleAuction.utils.SafeUser;

@CrossOrigin
@RestController
public class AuctionController {

	@Autowired
	AuctionService auctionService;
	
	@RequestMapping(value = "auctions", method = RequestMethod.GET)
	public Page<Auction> auctions(
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "limit", defaultValue = "10") int limit){
		
		return auctionService.findAll(new PageRequest(page, limit));
	}
	
	@RequestMapping(value = "auctions/opened", method = RequestMethod.GET)
	public Page<Auction> auctionsOpened(
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "limit", defaultValue = "10") int limit){
		
		return auctionService.findByState(true, new PageRequest(page, limit));
	}
	
	@RequestMapping(value = "auctions/closed", method = RequestMethod.GET)
	public Page<Auction> auctionsClosed(
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "limit", defaultValue = "10") int limit){
		
		return auctionService.findByState(false, new PageRequest(page, limit));
	}
	
	@RequestMapping(value = "auction/{id}/bidders", method = RequestMethod.GET)
	public List<SafeUser> auctionBidders(@PathVariable Long id){
		
		return auctionService.auctionBidders(id);
	}
	
	@RequestMapping(value = "auction/{id}/topBidder", method = RequestMethod.GET)
	public SafeUser auctionTopBidder(@PathVariable Long id){
		
		return auctionService.auctionTopBidder(id);
	}
	
	@RequestMapping(value = "auction", method = RequestMethod.POST)
	public Auction auctionAdd(@RequestBody final @Valid AuctionForm auction){
		
		return auctionService.auctionAdd(auction);
	}
	
	@RequestMapping(value = "auction/{id}/close", method = RequestMethod.PUT)
	public Auction auctionUpdateClose(@PathVariable Long id, @RequestBody final User user){

		return auctionService.auctionClose(id, user);
	}
	
	@RequestMapping(value = "auction/{id}/bid", method = RequestMethod.PUT)
	public Auction auctionUpdateBidding(@PathVariable Long id, @RequestBody final User user){
		
		return auctionService.auctionBid(id, user);
	}

}

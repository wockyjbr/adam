package simpleAuction.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import simpleAuction.entity.Auction;
import simpleAuction.entity.User;
import simpleAuction.repository.AuctionRepository;
import simpleAuction.repository.UserRepository;
import simpleAuction.utils.AuctionForm;
import simpleAuction.utils.SafeUser;

@Service
@Transactional
public class AuctionService {

	@Autowired
	AuctionRepository auctionRepo;

	@Autowired
	UserRepository userRepo;

	public Page<Auction> findAll(PageRequest page) {
		Page<Auction> auctions = auctionRepo.findAll(page);
		updateStateOfCurrentAuctions(auctions);
		return auctions;
	}

	public Page<Auction> findByState(Boolean state, PageRequest page) {
		Page<Auction> auctions = auctionRepo.findByIsOpen(state, page);
		updateStateOfCurrentAuctions(auctions);
		return auctions;
	}
	
	private void updateStateOfCurrentAuctions(Page<Auction> auctions) {
		Date today = new Date();
		for(Auction a : auctions){
			if (a.getCloseDate().before(today)) {
				a.setIsOpen(false);
				auctionRepo.save(a);
			}
		}
	}

	public List<SafeUser> auctionBidders(Long id) {
		Auction auctionToUpdate = auctionRepo.findOne(id);
		List<SafeUser> auctionBidders = auctionToUpdate.getBidders().stream()
				.map(b -> new SafeUser(b.getId(), b.getLogin())).collect(Collectors.<SafeUser> toList());
		return auctionBidders;
	}

	public SafeUser auctionTopBidder(Long id) {
		Auction auctionToUpdate = auctionRepo.findOne(id);
		User topBidder = auctionToUpdate.getTopBidder();
		SafeUser topBidderOutput = new SafeUser(topBidder.getId(), topBidder.getLogin());
		return topBidderOutput;
	}

	public Auction auctionAdd(AuctionForm auction) {
		User owner = userRepo.findOne(auction.getOwnerId());
		Auction newAuction = new Auction(auction.getTitle(), auction.getPrice(), auction.getDescription(), true,
				new Date(), calculateExpiryDate(auction.getValidity()), owner);
		return auctionRepo.save(newAuction);
	}

	private Date calculateExpiryDate(Integer days) {
		Date expiryDate = new Date();
		Calendar c = Calendar.getInstance(); 
		c.setTime(expiryDate); 
		c.add(Calendar.DATE, days);
		expiryDate = c.getTime();
		return expiryDate;
	}

	public Auction auctionClose(Long id, User user) {
		Auction auctionToUpdate = auctionRepo.findOne(id);
		if (auctionToUpdate.getOwner().equals(user) && auctionToUpdate.getIsOpen() == true) {
			auctionToUpdate.setCloseDate(new Date());
			auctionToUpdate.setIsOpen(false);
			return auctionRepo.save(auctionToUpdate);
		} else
			return null;
	}

	public Auction auctionBid(Long id, User user) {
		Auction auctionToUpdate = auctionRepo.findOne(id);
		if (!auctionToUpdate.getOwner().equals(user) && auctionToUpdate.getIsOpen() == true
				&& (auctionToUpdate.getTopBidder() == null || !auctionToUpdate.getTopBidder().equals(user))) {
			User fullUser = userRepo.findOne(user.getId());
			auctionToUpdate.addBidder(fullUser);
			auctionToUpdate.setPrice(increasePrice(auctionToUpdate.getPrice()));
			auctionToUpdate.setTopBidder(fullUser);
			return auctionRepo.save(auctionToUpdate);
		} else
			return null;
	}

	private Double increasePrice(Double price) {
		return price * 1.1;
	}

}

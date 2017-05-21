package simpleAuction.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Auction {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String title;
	
	private Double price;
	
	private String description;
	
	private Boolean isOpen;
	
	private Date openDate;
	
	private Date closeDate;
	
	@OneToOne
	private User owner;
	
	@OneToOne
	private User topBidder;
	
	@ManyToMany
	@JoinTable(name = "auction_user", joinColumns = @JoinColumn(name = "auction_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
	@JsonIgnore
	private List<User> bidders;
	
	protected Auction(){}
	
	public Auction(String title, Double price, String description, Boolean isOpen, Date openDate, Date closeDate, User owner) {
		this.title = title;
		this.price = price;
		this.description = description;
		this.isOpen = isOpen;
		this.openDate = openDate;
		this.closeDate = closeDate;
		this.owner = owner;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getIsOpen() {
		return isOpen;
	}

	public void setIsOpen(Boolean isOpen) {
		this.isOpen = isOpen;
	}

	public Date getOpenDate() {
		return openDate;
	}

	public void setOpenDate(Date openDate) {
		this.openDate = openDate;
	}

	public Date getCloseDate() {
		return closeDate;
	}

	public void setCloseDate(Date closeDate) {
		this.closeDate = closeDate;
	}

	public User getTopBidder() {
		return topBidder;
	}

	public void setTopBidder(User topBidder) {
		this.topBidder = topBidder;
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	public List<User> getBidders() {
		return bidders;
	}

	public void setBidders(List<User> bidders) {
		this.bidders = bidders;
	}
	
	public void addBidder(User bidder) {
		this.bidders.add(bidder);
	}
	
	@Override
	public String toString() {
		return String.format("Auction[id=%d, title=%s, price=%f, description=%s, isOpen=%b, owner=%s, topBidder=%s]", id, title, price, description, isOpen, owner, topBidder);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((closeDate == null) ? 0 : closeDate.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((isOpen == null) ? 0 : isOpen.hashCode());
		result = prime * result + ((openDate == null) ? 0 : openDate.hashCode());
		result = prime * result + ((owner == null) ? 0 : owner.hashCode());
		result = prime * result + ((price == null) ? 0 : price.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		result = prime * result + ((topBidder == null) ? 0 : topBidder.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Auction)) {
			return false;
		}
		Auction other = (Auction) obj;
		if (closeDate == null) {
			if (other.closeDate != null) {
				return false;
			}
		} else if (!closeDate.equals(other.closeDate)) {
			return false;
		}
		if (description == null) {
			if (other.description != null) {
				return false;
			}
		} else if (!description.equals(other.description)) {
			return false;
		}
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		if (isOpen == null) {
			if (other.isOpen != null) {
				return false;
			}
		} else if (!isOpen.equals(other.isOpen)) {
			return false;
		}
		if (openDate == null) {
			if (other.openDate != null) {
				return false;
			}
		} else if (!openDate.equals(other.openDate)) {
			return false;
		}
		if (owner == null) {
			if (other.owner != null) {
				return false;
			}
		} else if (!owner.equals(other.owner)) {
			return false;
		}
		if (price == null) {
			if (other.price != null) {
				return false;
			}
		} else if (!price.equals(other.price)) {
			return false;
		}
		if (title == null) {
			if (other.title != null) {
				return false;
			}
		} else if (!title.equals(other.title)) {
			return false;
		}
		if (topBidder == null) {
			if (other.topBidder != null) {
				return false;
			}
		} else if (!topBidder.equals(other.topBidder)) {
			return false;
		}
		return true;
	}

}

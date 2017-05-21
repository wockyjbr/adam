package simpleAuction.utils;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

public class AuctionForm {

	@NotNull
    @Size(min=2, max=30)
	private String title;
	
	@NotNull
	@Min(1)
	private Double price;
	
	@NotNull
	@NotEmpty
	private String description;
	
	@NotNull
	private Long ownerId;
	
	@NotNull
	@Min(1)
	@Max(30)
	private Integer validity;

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

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

	@Override
	public String toString() {
		return "AuctionForm [title=" + title + ", price=" + price + ", description=" + description + ", ownerId="
				+ ownerId + "]";
	}

	public Integer getValidity() {
		return validity;
	}

	public void setValidity(Integer validity) {
		this.validity = validity;
	}

}

package simpleAuction.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import simpleAuction.entity.Auction;

public interface AuctionRepository extends CrudRepository<Auction, Long> {

    Page<Auction> findAll(Pageable pageable);
    Page<Auction> findByIsOpen(Boolean isOpen, Pageable pageable);
}

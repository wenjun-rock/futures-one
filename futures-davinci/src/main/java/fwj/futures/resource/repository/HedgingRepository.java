package fwj.futures.resource.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import fwj.futures.resource.entity.Hedging;

public interface HedgingRepository extends JpaRepository<Hedging, Integer> {

	Hedging findByName(String name);
	
}

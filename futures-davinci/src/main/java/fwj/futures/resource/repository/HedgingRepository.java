package fwj.futures.resource.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import fwj.futures.resource.entity.Hedging;

@RepositoryRestResource(exported = false)
public interface HedgingRepository extends JpaRepository<Hedging, Integer> {

	Hedging findByName(String name);
	
}

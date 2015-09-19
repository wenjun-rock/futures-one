package fwj.futures.resource.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import fwj.futures.resource.entity.Futures;

public interface FuturesRepository extends JpaRepository<Futures, Integer> {

	Futures findByCode(String code);
	
	@Query("select o from Futures o where o.active='1'")
	List<Futures> findAllActive();

}

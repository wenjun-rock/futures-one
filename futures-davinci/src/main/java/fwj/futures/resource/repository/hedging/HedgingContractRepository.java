package fwj.futures.resource.repository.hedging;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import fwj.futures.resource.entity.hedging.HedgingContract;

@RepositoryRestResource(exported = false)
public interface HedgingContractRepository extends JpaRepository<HedgingContract, Integer> {

	List<HedgingContract> findByCode(String code);
	
}

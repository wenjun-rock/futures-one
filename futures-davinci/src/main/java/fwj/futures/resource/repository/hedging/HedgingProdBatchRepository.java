package fwj.futures.resource.repository.hedging;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import fwj.futures.resource.entity.hedging.HedgingProdBatch;

@RepositoryRestResource(exported = false)
public interface HedgingProdBatchRepository extends JpaRepository<HedgingProdBatch, Integer> {

}

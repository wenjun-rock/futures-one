package fwj.futures.resource.hedging.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import fwj.futures.resource.hedging.entity.HedgingProdBatch;

@RepositoryRestResource(exported = false)
public interface HedgingProdBatchRepos extends JpaRepository<HedgingProdBatch, Integer> {

}

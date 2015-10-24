package fwj.futures.resource.hedging.repos;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import fwj.futures.resource.hedging.entity.HedgingProdExperiment;

@RepositoryRestResource(exported = false)
public interface HedgingProdExpRepos extends JpaRepository<HedgingProdExperiment, Integer> {

	@Query("select o from HedgingProdExperiment o where o.rsquared >= :minRsquared order by o.rsquared desc")
	List<HedgingProdExperiment> findByLimitRsquared(@Param("minRsquared") BigDecimal minRsquared);

}

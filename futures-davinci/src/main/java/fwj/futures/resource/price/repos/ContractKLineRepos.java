package fwj.futures.resource.price.repos;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import fwj.futures.resource.price.entity.ContractKLine;

@RepositoryRestResource(exported = false)
public interface ContractKLineRepos extends JpaRepository<ContractKLine, Integer> {
	
	List<ContractKLine> findByCodeAndDtBetween(String code, Date startDt, Date endDt);

	List<ContractKLine> findByCodeAndContractMonthOrderByDtAsc(String code, int ContractMonth);
	
	List<ContractKLine> findByCodeOrderByDtAsc(String code);
	
	List<ContractKLine> findByCodeOrderByDtDesc(String code);

	ContractKLine findTopByCodeAndContractMonthOrderByDtDesc(String code, int contractMonth);
}

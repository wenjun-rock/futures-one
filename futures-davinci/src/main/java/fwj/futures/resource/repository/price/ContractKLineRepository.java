package fwj.futures.resource.repository.price;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import fwj.futures.resource.entity.price.ContractKLine;

@RepositoryRestResource(exported = false)
public interface ContractKLineRepository extends JpaRepository<ContractKLine, Integer> {

	ContractKLine findTopByContractOrderByDtDesc(String contract);
	
	List<ContractKLine> findByCodeAndDtBetween(String code, Date startDt, Date endDt);

	List<ContractKLine> findByCodeOrderByDtAsc(String code);
	
	List<ContractKLine> findByCodeOrderByDtDesc(String code);
}

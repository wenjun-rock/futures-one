package fwj.futures.resource.price.repos;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import fwj.futures.resource.price.entity.ProdKLine;

@RepositoryRestResource(exported = false)
public interface ProdKLineRepos extends JpaRepository<ProdKLine, Integer> {

	ProdKLine findTopByCodeOrderByDtDesc(String code);
	
	List<ProdKLine> findByCodeAndDtBetweenOrderByDtAsc(String code, Date startDt, Date endDt);

	List<ProdKLine> findByCodeOrderByDtAsc(String code);
	
	List<ProdKLine> findByCodeOrderByDtDesc(String code);
}

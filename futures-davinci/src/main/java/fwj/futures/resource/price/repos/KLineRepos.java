package fwj.futures.resource.price.repos;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import fwj.futures.resource.price.entity.KLine;

@RepositoryRestResource(exported = false)
public interface KLineRepos extends JpaRepository<KLine, Integer> {

	KLine findTopByCodeOrderByDtDesc(String code);
	
	List<KLine> findByCodeAndDtBetweenOrderByDtAsc(String code, Date startDt, Date endDt);

	List<KLine> findByCodeOrderByDtAsc(String code);
	
	List<KLine> findByCodeOrderByDtDesc(String code);
}

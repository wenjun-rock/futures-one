package fwj.futures.resource.repository.price;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import fwj.futures.resource.entity.price.KLine;

@RepositoryRestResource(exported = false)
public interface KLineRepository extends JpaRepository<KLine, Integer> {

	KLine findTopByCodeOrderByDtDesc(String code);
	
	List<KLine> findByCodeAndDtBetweenOrderByDtAsc(String code, Date startDt, Date endDt);

	List<KLine> findByCodeOrderByDtAsc(String code);
	
	List<KLine> findByCodeOrderByDtDesc(String code);
}

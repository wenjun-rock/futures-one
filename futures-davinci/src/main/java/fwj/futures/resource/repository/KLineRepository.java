package fwj.futures.resource.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import fwj.futures.resource.entity.KLine;

public interface KLineRepository extends JpaRepository<KLine, Integer> {

	KLine findTopByCodeOrderByDtDesc(String code);
	
	List<KLine> findTop60ByCodeOrderByDtDesc(String code);

	List<KLine> findByCodeAndDtBetween(String code, String startDt, String endDt);

	List<KLine> findByCode(@Param("code") String code);

}

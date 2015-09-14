package fwj.futures.resource.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import fwj.futures.resource.entity.KLine;

public interface KLineRepository extends JpaRepository<KLine, Integer> {

	@Query("select max(o.dt) from KLine o where o.code = :code")
	String findMaxDtByCode(@Param("code") String code);

	@Query("select o from KLine o where code = :code and (dt between :startDt and :endDt)")
	List<KLine> findByCodeDateRange(@Param("code") String code, @Param("startDt") String startDt,
			@Param("endDt") String endDt);

}

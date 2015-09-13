package fwj.futures.resource.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import fwj.futures.resource.entity.KLine;

public interface KLineRepository extends JpaRepository<KLine, Integer> {

	@Query("select max(o.dt) from KLine o where o.code = :code")
	String findMaxDtByCode(@Param("code") String code);

}

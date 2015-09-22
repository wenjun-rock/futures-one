package fwj.futures.resource.repository.prod;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import fwj.futures.resource.entity.prod.FuturesTradeTime;

@RepositoryRestResource(exported = false)
public interface FuturesTradeTimeRepository extends JpaRepository<FuturesTradeTime, Integer> {

	List<FuturesTradeTime> findByCodeOrderByStartTimeAsc(String code);

}

package fwj.futures.resource.prod.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import fwj.futures.resource.prod.entity.FuturesTradeTime;

@RepositoryRestResource(exported = false)
public interface FuturesTradeTimeRepos extends JpaRepository<FuturesTradeTime, Integer> {

	List<FuturesTradeTime> findByCodeOrderByStartTimeAsc(String code);

}

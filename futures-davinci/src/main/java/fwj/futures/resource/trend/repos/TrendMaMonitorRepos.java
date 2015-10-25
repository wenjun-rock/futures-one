package fwj.futures.resource.trend.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import fwj.futures.resource.trend.entity.TrendMaMonitor;

@RepositoryRestResource(exported = false)
public interface TrendMaMonitorRepos extends JpaRepository<TrendMaMonitor, Integer> {

	public TrendMaMonitor findByCodeAndTrend(String code, String trend);

}

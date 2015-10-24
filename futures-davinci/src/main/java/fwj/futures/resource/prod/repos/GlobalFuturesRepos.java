package fwj.futures.resource.prod.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import fwj.futures.resource.prod.entity.GlobalFutures;

@RepositoryRestResource(exported = false)
public interface GlobalFuturesRepos extends JpaRepository<GlobalFutures, Integer> {

	GlobalFutures findByCode(String code);
	
}

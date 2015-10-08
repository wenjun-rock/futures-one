package fwj.futures.resource.repository.prod;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import fwj.futures.resource.entity.prod.GlobalFutures;

@RepositoryRestResource(exported = false)
public interface GlobalFuturesRepository extends JpaRepository<GlobalFutures, Integer> {

	GlobalFutures findByCode(String code);
	
}

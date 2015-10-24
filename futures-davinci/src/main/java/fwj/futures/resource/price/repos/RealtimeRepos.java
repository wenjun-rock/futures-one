package fwj.futures.resource.price.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import fwj.futures.resource.price.entity.RealtimeStore;

@RepositoryRestResource(exported = false)
public interface RealtimeRepos extends JpaRepository<RealtimeStore, Integer> {

//	List<RealtimeStore> findTop20000OrderByPriceTimeDesc();

}

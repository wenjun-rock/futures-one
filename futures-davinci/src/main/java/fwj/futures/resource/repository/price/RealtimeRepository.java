package fwj.futures.resource.repository.price;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import fwj.futures.resource.entity.price.RealtimeStore;

@RepositoryRestResource(exported = false)
public interface RealtimeRepository extends JpaRepository<RealtimeStore, Integer> {

	List<RealtimeStore> findTop20000OrderByPriceTimeDesc();

}

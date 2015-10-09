package fwj.futures.resource.repository.price;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import fwj.futures.resource.entity.price.GlobalKLine;

@RepositoryRestResource(exported = false)
public interface GlobalKLineRepository extends JpaRepository<GlobalKLine, Integer> {

	GlobalKLine findTopByCodeOrderByDtDesc(String code);
	
}

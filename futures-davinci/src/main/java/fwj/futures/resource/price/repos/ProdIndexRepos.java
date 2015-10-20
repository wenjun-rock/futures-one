package fwj.futures.resource.price.repos;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import fwj.futures.resource.price.entity.ProdIndex;

@RepositoryRestResource(exported = false)
public interface ProdIndexRepos extends JpaRepository<ProdIndex, Integer> {

	List<ProdIndex> findByCodeOrderByDtAsc(String code);

	List<ProdIndex> findByCodeAndDtBetweenOrderByDtAsc(String code, Date startDt, Date endDt);

	int deleteByCodeAndDtBetween(String code, Date startDt, Date endDt);

}

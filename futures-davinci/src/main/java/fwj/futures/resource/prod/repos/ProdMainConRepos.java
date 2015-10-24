package fwj.futures.resource.prod.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import fwj.futures.resource.prod.entity.ProdMainCon;

@RepositoryRestResource(exported = false)
public interface ProdMainConRepos extends JpaRepository<ProdMainCon, Integer> {

	List<ProdMainCon> findByCode(String code);

}

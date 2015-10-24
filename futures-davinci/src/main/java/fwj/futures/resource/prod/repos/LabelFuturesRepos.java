package fwj.futures.resource.prod.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import fwj.futures.resource.prod.entity.LabelFutures;

@RepositoryRestResource(exported = false)
public interface LabelFuturesRepos extends JpaRepository<LabelFutures, Integer> {

	List<LabelFutures> findByFuturesCodeOrderByLabelRankAsc(String code);

	List<LabelFutures> findByLabelId(Integer labelId);

	List<LabelFutures> findByLabelName(String labelName);

	List<LabelFutures> findByLabelIdOrderByFuturesCodeAsc(Integer labelId);
}

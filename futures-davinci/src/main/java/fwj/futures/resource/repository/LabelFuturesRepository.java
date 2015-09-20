package fwj.futures.resource.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import fwj.futures.resource.entity.LabelFutures;

@RepositoryRestResource(exported = false)
public interface LabelFuturesRepository extends JpaRepository<LabelFutures, Integer> {

	List<LabelFutures> findByFuturesCodeOrderByLabelRankAsc(String code);

	List<LabelFutures> findByLabelId(Integer labelId);

	List<LabelFutures> findByLabelName(String labelName);

	List<LabelFutures> findByLabelIdOrderByFuturesCodeAsc(Integer labelId);
}

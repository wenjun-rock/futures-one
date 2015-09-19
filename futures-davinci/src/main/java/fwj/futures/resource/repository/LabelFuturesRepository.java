package fwj.futures.resource.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import fwj.futures.resource.entity.LabelFutures;

public interface LabelFuturesRepository extends JpaRepository<LabelFutures, Integer> {

	List<LabelFutures> findByFuturesCodeOrderByLabelRankAsc(String code);

	List<LabelFutures> findByLabelId(Integer labelId);

	List<LabelFutures> findByLabelName(String labelName);

}

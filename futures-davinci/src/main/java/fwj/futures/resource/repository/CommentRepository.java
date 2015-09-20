package fwj.futures.resource.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import fwj.futures.resource.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Integer> {

	// List<Comment>
	// findByRelativeTypeAndRelativeKeyOrderByCommitTimeDesc(String
	// relativeType, String relativeId);

	@Query("select o from Comment o where o.relativeType=:relativeKey and o.relativeKey=:key order by commitTime desc")
	List<Comment> findByTypeAndKey(Integer type, String key);

	@Query("select o from Comment o where o.relativeType=:relativeKey order by commitTime desc")
	List<Comment> findByType(Integer type);

}

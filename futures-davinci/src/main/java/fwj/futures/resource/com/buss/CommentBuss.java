package fwj.futures.resource.com.buss;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;

import fwj.futures.resource.com.entity.Comment;
import fwj.futures.resource.com.repos.CommentRepos;
import fwj.futures.resource.prod.buss.ProdBuss;
import fwj.futures.resource.prod.entity.Futures;

@Component
public class CommentBuss {

	private Logger log = Logger.getLogger(this.getClass());

	private long lastCommitTime = 0;

	@Autowired
	private ProdBuss productBuss;

	@Autowired
	private CommentRepos commentRepository;

	@Cacheable(value = "CommentBuss.queryAll")
	public List<Comment> queryAll() {
		return commentRepository.findAll(new Sort(Direction.DESC, "commitTime"));
	}

	@Cacheable(value = "CommentBuss.queryByType")
	public List<Comment> queryByType(Integer type) {
		return commentRepository.findByType(type);
	}

	@Cacheable(value = "CommentBuss.queryByTypeAndKey")
	public List<Comment> queryByTypeAndKey(Integer type, String key) {
		return commentRepository.findByTypeAndKey(type, key);
	}

	@CacheEvict(value = { "CommentBuss.queryAll", "CommentBuss.queryByType",
			"CommentBuss.queryByTypeAndKey" }, allEntries = true)
	synchronized public Comment commitComment(Integer type, String key, String content) {

		Date now = new Date();
		if (now.getTime() < lastCommitTime + 1000) {
			log.warn("It's too hot! last commit was on " + lastCommitTime);
			return null;
		} else {
			lastCommitTime = now.getTime();
		}

		String name = null;
		if (type == 1) {
			Futures futures = productBuss.queryFuturesByCode(key);
			if (futures == null) {
				return null;
			} else {
				name = futures.getName();
			}
		} else {
			name = "";
		}

		Comment comment = new Comment();
		comment.setType(type);
		comment.setRltKey(key);
		comment.setRltName(name);
		comment.setContent(content);
		comment.setCommitTime(now);
		return commentRepository.saveAndFlush(comment);
	}

}

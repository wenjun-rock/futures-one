package fwj.futures.resource.buss;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;

import fwj.futures.resource.entity.com.Comment;
import fwj.futures.resource.repository.com.CommentRepository;

@Component
public class CommentBuss {
	
	private Logger log = Logger.getLogger(this.getClass());
	
	private long lastCommitTime = 0;

	@Autowired
	private CommentRepository commentRepository;

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
		if(now.getTime() < lastCommitTime + 1000){
			log.warn("It's too hot! last commit was on " + lastCommitTime);
			return null;
		} else {
			lastCommitTime = now.getTime();
		}
		Comment comment = new Comment();
		comment.setRelativeType(type);
		comment.setRelativeKey(key);
		comment.setContent(content);
		comment.setCommitTime(now);
		return commentRepository.saveAndFlush(comment);
	}

}

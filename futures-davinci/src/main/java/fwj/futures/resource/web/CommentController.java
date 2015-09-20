package fwj.futures.resource.web;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fwj.futures.resource.buss.CommentBuss;
import fwj.futures.resource.entity.Comment;

@RestController()
@RequestMapping("/web/comments")
public class CommentController {

	@Autowired
	private CommentBuss commentBuss;

	@RequestMapping(method = RequestMethod.GET)
	public List<Comment> getComments(@RequestParam(value = "from", defaultValue = "1") int from,
			@RequestParam(value = "len", defaultValue = "10") int len,
			@RequestParam(value = "type", required = false) Integer type,
			@RequestParam(value = "key", required = false) String key) {
		List<Comment> list = null;
		if (type == null) {
			list = commentBuss.queryAll();
		} else if (key == null) {
			list = commentBuss.queryByType(type);
		} else {
			list = commentBuss.queryByTypeAndKey(type, key);
		}
		if (from > list.size()) {
			return Collections.emptyList();
		} else {
			int start = Math.max(from - 1, 0);
			int to = Math.min((from - 1 + len), list.size());
			return list.subList(start, to);
		}
	}

	@RequestMapping(method = RequestMethod.POST)
	public Comment commitComment(@RequestParam("type") Integer type, @RequestParam("key") String key,
			@RequestParam("content") String content) {
		return commentBuss.commitComment(type, key, content);
	}

}

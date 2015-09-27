package fwj.futures.resource.web.ctrl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/admin/letmein")
public class AdminCtrl {

	private Logger log = Logger.getLogger(this.getClass());

	@Autowired
	private CacheManager cacheManager;

	@RequestMapping(value = "/clear-cache", method = RequestMethod.GET)
	public String clearCache() {
		String messages = "";
		for (String cacheName : cacheManager.getCacheNames()) {
			String message = "clear " + cacheName;
			log.info(message);
			messages += message + "\n";
			cacheManager.getCache(cacheName).clear();
		}
		return messages;
	}

}

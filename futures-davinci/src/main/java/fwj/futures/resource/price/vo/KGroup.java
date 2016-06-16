package fwj.futures.resource.price.vo;

import java.util.Date;
import java.util.Map;

/**
 * @author Administrator
 *
 */
public class KGroup implements Comparable<KGroup> {

	/**
	 * pattern: yyyy-MM-dd
	 */
	private Date dt;

	/**
	 * key:code
	 */
	private Map<String, ? extends K> kLineMap;

	public KGroup(Date dt, Map<String, K> kLineMap) {
		this.dt = dt;
		this.kLineMap = kLineMap;
	}

	public Date getDt() {
		return dt;
	}

	public Map<String, ? extends K> getkLineMap() {
		return kLineMap;
	}

	@Override
	public int compareTo(KGroup that) {
		return this.dt.compareTo(that.dt);
	}
}
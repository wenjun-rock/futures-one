package fwj.futures.resource.vo;

import java.util.Date;
import java.util.Map;

import fwj.futures.resource.entity.price.KLine;

/**
 * @author Administrator
 *
 */
public class KLineGroup implements Comparable<KLineGroup> {

	/**
	 * pattern: yyyy-MM-dd
	 */
	private Date dt;

	/**
	 * key:code
	 */
	private Map<String, KLine> kLineMap;

	public KLineGroup(Date dt, Map<String, KLine> kLineMap) {
		this.dt = dt;
		this.kLineMap = kLineMap;
	}

	public Date getDt() {
		return dt;
	}

	public Map<String, KLine> getkLineMap() {
		return kLineMap;
	}

	@Override
	public int compareTo(KLineGroup that) {
		return this.dt.compareTo(that.dt);
	}
}
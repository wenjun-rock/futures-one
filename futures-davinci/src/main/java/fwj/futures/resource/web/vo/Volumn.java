package fwj.futures.resource.web.vo;

import java.util.Date;

public  class Volumn implements Comparable<Volumn> {
	private Date d;
	private Integer v;

	public Volumn(Date dt, Integer vol) {
		d = dt;
		v = vol;
	}

	public Date getD() {
		return d;
	}

	public Integer getV() {
		return v;
	}

	@Override
	public int compareTo(Volumn that) {
		return this.d.compareTo(that.d);
	}
}

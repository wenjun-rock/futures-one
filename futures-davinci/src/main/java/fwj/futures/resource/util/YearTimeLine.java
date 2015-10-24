package fwj.futures.resource.util;

import java.util.Date;
import java.util.List;

public class YearTimeLine<T extends TimeLineable> implements Comparable<YearTimeLine<T>> {

	private String year;

	private List<PointWrap<T>> line;

	public YearTimeLine(String year, List<PointWrap<T>> line) {
		this.year = year;
		this.line = line;
	}

	public String getYear() {
		return year;
	}

	public List<PointWrap<T>> getLine() {
		return line;
	}

	@Override
	public int compareTo(YearTimeLine<T> that) {
		return that.year.compareTo(this.year);
	}

	public static class PointWrap<T extends TimeLineable> {
		private Date dt;
		private String y;
		private T p;

		public PointWrap(Date dt, String y, T p) {
			this.dt = dt;
			this.y = y;
			this.p = p;
		}

		public Date getDt() {
			return dt;
		}

		public String getY() {
			return y;
		}

		public T getP() {
			return p;
		}
	}

}

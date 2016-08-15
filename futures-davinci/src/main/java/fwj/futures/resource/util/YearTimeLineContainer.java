package fwj.futures.resource.util;

import java.util.List;

public class YearTimeLineContainer<T extends TimeLineable> {

	private String name;
	private List<YearTimeLine<T>> yearLines;

	public YearTimeLineContainer(String name, List<YearTimeLine<T>> yearLines) {
		this.name = name;
		this.yearLines = yearLines;
	}

	public String getName() {
		return name;
	}

	public List<YearTimeLine<T>> getYearLines() {
		return yearLines;
	}

}

package fwj.futures.resource.hedging.vo;

import java.util.List;

import fwj.futures.resource.util.YearTimeLine;

public class HedgingContractContainer {

	private String name;

	private List<HedgingContractDiff> line;
	private List<YearTimeLine<HedgingContractDiff>> yearLines;

	public HedgingContractContainer(String name, List<HedgingContractDiff> line,
			List<YearTimeLine<HedgingContractDiff>> yearLines) {
		this.name = name;
		this.line = line;
		this.yearLines = yearLines;
	}

	public String getName() {
		return name;
	}

	public List<YearTimeLine<HedgingContractDiff>> getYearLines() {
		return yearLines;
	}

	public List<HedgingContractDiff> getLine() {
		return line;
	}

	public static class HedgingContractLine implements Comparable<HedgingContractLine> {
		private String year;
		private List<HedgingContractDiff> line;

		public HedgingContractLine(String year, List<HedgingContractDiff> line) {
			this.year = year;
			this.line = line;
		}

		public String getYear() {
			return year;
		}

		public List<HedgingContractDiff> getLine() {
			return line;
		}

		@Override
		public int compareTo(HedgingContractLine that) {
			return that.year.compareTo(this.year);
		}

	}

}

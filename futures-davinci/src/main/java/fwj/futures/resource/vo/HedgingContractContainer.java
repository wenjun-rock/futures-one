package fwj.futures.resource.vo;

import java.util.List;

public class HedgingContractContainer {

	private String name;

	private List<HedgingContractLine> lines;

	public HedgingContractContainer(String name, List<HedgingContractLine> lines) {
		this.name = name;
		this.lines = lines;
	}

	public String getName() {
		return name;
	}

	public List<HedgingContractLine> getLines() {
		return lines;
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

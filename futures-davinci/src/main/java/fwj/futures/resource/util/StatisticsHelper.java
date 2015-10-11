package fwj.futures.resource.util;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class StatisticsHelper {

	public static Result statsBigDecimal(List<BigDecimal> list) {
		List<Double> doubleList = list.stream().map(bd -> bd.doubleValue()).collect(Collectors.toList());
		return statsDouble(doubleList);
	}

	public static Result statsDouble(List<Double> dataList) {
		dataList.sort(Comparator.naturalOrder());
		double q1 = dataList.get(dataList.size() / 4);
		double q3 = dataList.get(dataList.size() * 3 / 4);
		int remove = dataList.size() * 3 / 100;
		List<Double> cleanList = dataList.subList(remove, dataList.size() - remove);
		double avg = cleanList.stream().collect(Collectors.averagingDouble(d -> d));
		double e = cleanList.stream().collect(Collectors.averagingDouble(d -> Math.pow(d - avg, 2.0)));
		double sd = Math.sqrt(e);
		return new Result(sd, avg, q1, q3);
	}

	public static class Result {
		double sd;
		double avg;
		double q1;
		double q3;

		public Result(double sd, double avg, double q1, double q3) {
			this.sd = sd;
			this.avg = avg;
			this.q1 = q1;
			this.q3 = q3;
		}

		public double getSd() {
			return sd;
		}

		public double getAvg() {
			return avg;
		}

		public double getQ1() {
			return q1;
		}

		public double getQ3() {
			return q3;
		}
	}

}

package fwj.futures.resource.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import fwj.futures.resource.util.YearTimeLine.PointWrap;

public class LineHelper {

	public static <T extends TimeLineable> List<YearTimeLine<T>> foldLineInYear(List<T> timeLine) {
		DateFormat df = new SimpleDateFormat("yyyy");
		Map<String, List<PointWrap<T>>> map = timeLine.stream().map(point -> {
			String year = df.format(point.getD());
			Calendar cal = Calendar.getInstance();
			cal.setTime(point.getD());
			cal.set(Calendar.YEAR, 2012);
			return new PointWrap<T>(cal.getTime(), year, point);
		}).collect(Collectors.groupingBy(PointWrap::getY, Collectors.toList()));
		return map.entrySet().stream().map(entry -> new YearTimeLine<T>(entry.getKey(), entry.getValue())).sorted()
				.collect(Collectors.toList());
	}

}

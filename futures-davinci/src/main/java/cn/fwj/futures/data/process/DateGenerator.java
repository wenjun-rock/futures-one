package cn.fwj.futures.data.process;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class DateGenerator {

	public static List<String> range(String startDt, String endDt) {
		try {
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			Date start = df.parse(startDt);
			Date end = df.parse(endDt);
			Calendar cal = Calendar.getInstance();
			cal.setTime(start);
			List<String> list = new ArrayList<>();
			while (!cal.getTime().after(end)) {
				list.add(df.format(cal.getTime()));
				cal.add(Calendar.DAY_OF_MONTH, 1);
			}
			return list;
		} catch (ParseException e) {
			return Collections.emptyList();
		}
	}

}

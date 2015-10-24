package fwj.futures.data.init.com;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fwj.futures.data.launch.AbstractBaseLaunch;
import fwj.futures.resource.com.entity.Holiday;
import fwj.futures.resource.com.repos.HolidayRepos;

@Component
public class InitHoliday extends AbstractBaseLaunch {

	@Autowired
	private HolidayRepos holidayRepository;

	@Override
	protected void execute() throws Exception {

		holidayRepository.deleteAllInBatch();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Stream.of( //
				new Input("国庆", "2015-10-01", "2015-10-07"), //
				new Input("中秋", "2015-09-26", "2015-09-27") //
		).forEach(input -> {
			try {
				Holiday holiday = new Holiday();
				holiday.setDescription(input.desc);
				holiday.setStartDate(input.from);
				holiday.setEndDate(input.to);
				Calendar cal = Calendar.getInstance();
				cal.setTime(df.parse(input.from));
				cal.add(Calendar.HOUR, -3);
				holiday.setActualStartTime(cal.getTime());
				cal.setTime(df.parse(input.to));
				cal.add(Calendar.DATE, 1);
				cal.add(Calendar.HOUR, 9);
				holiday.setActualEndTime(cal.getTime());
				holidayRepository.save(holiday);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		holidayRepository.flush();
		System.out.println("done!");
	}

	public static void main(String[] args) {
		launch(InitHoliday.class);
	}

	private class Input {
		private String desc;
		private String from;
		private String to;

		private Input(String desc, String from, String to) {
			this.desc = desc;
			this.from = from;
			this.to = to;
		}
	}
}

package fwj.futures.data.init.com;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fwj.futures.data.launch.AbstractBaseLaunch;
import fwj.futures.resource.entity.com.Holiday;
import fwj.futures.resource.repository.com.HolidayRepository;

@Component
public class InitHoliday extends AbstractBaseLaunch {

	@Autowired
	private HolidayRepository holidayRepository;

	@Override
	protected void execute() throws Exception {

		holidayRepository.deleteAllInBatch();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Stream.of( //
				new Input("国庆", "2015-10-01", "2015-10-07") //
		).forEach(input -> {
			try {
				Date from = df.parse(input.from);
				Calendar cal = Calendar.getInstance();
				cal.setTime(df.parse(input.to));
				cal.add(Calendar.DATE, 1);
				cal.add(Calendar.SECOND, -1);
				Date to = cal.getTime();
				Holiday holiday = new Holiday();
				holiday.setDesc2(input.desc);
				holiday.setStartDateTime(from);
				holiday.setEndDateTime(to);
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

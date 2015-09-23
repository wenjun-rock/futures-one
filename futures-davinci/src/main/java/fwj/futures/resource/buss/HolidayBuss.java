package fwj.futures.resource.buss;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import fwj.futures.resource.entity.com.Holiday;
import fwj.futures.resource.repository.com.HolidayRepository;

@Component
public class HolidayBuss {
	
	@Autowired
	private HolidayRepository holidayRepository;

	@Cacheable(value = "HolidayBuss.queryAll")
	public List<Holiday> queryAll() {
		return holidayRepository.findAll();
	}

}

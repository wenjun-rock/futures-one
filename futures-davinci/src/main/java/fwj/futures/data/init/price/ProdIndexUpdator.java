package fwj.futures.data.init.price;

import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fwj.futures.data.launch.AbstractBaseLaunch;
import fwj.futures.resource.price.buss.ProdIndexBuss;

@Component
public class ProdIndexUpdator extends AbstractBaseLaunch {

	@Autowired
	ProdIndexBuss prodIndexBuss;

	@Override
	protected void execute() throws Exception {
		Calendar cal = Calendar.getInstance();
		Date endDt = cal.getTime();
		cal.add(Calendar.YEAR, -20);
		Date startDt = cal.getTime();
		prodIndexBuss.updateProdIndex("RB", startDt, endDt);
	}

	public static void main(String[] args) {
		launch(ProdIndexUpdator.class);
	}
}

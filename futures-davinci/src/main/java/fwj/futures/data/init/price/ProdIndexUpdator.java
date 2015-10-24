package fwj.futures.data.init.price;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fwj.futures.data.enu.ProdEnum;
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
//		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//		Date startDt = df.parse("2015-10-21");
//		Date endDt = df.parse("2015-10-22");
		
		int i = 0;
		for (ProdEnum prod : ProdEnum.values()) {
			log.info(
					String.format("%3d updateProdIndex for %s between %s and %s", ++i, prod.getCode(), startDt, endDt));
			prodIndexBuss.updateProdIndex(prod.getCode(), startDt, endDt);
		}
//		prodIndexBuss.updateProdIndex("A", startDt, endDt);
	}

	public static void main(String[] args) {
		launch(ProdIndexUpdator.class);
	}
}

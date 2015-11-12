package fwj.futures.data.init.price;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fwj.futures.data.launch.AbstractBaseLaunch;
import fwj.futures.resource.price.buss.ProdSpotPriceBuss;

@Component
public class SpotPriceDownload extends AbstractBaseLaunch {
	
	@Autowired
	private ProdSpotPriceBuss prodSpotPriceBuss;

	@Override
	protected void execute() throws Exception {	
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date startDt = df.parse("2015-10-24");
		Date endDt = df.parse("2015-11-11");
		prodSpotPriceBuss.updateProdSpotPrice(startDt, endDt);
	}

	public static void main(String[] args) {
		launch(SpotPriceDownload.class);
	}
}

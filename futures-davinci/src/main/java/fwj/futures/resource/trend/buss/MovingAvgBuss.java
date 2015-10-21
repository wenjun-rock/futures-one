package fwj.futures.resource.trend.buss;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fwj.futures.resource.price.buss.ProdIndexBuss;
import fwj.futures.resource.trend.vo.ProdMA;

@Component
public class MovingAvgBuss {
	
	@Autowired
	private ProdIndexBuss prodIndexBuss;
	
	public ProdMA calProdMovingAverage(String code) {
		
		prodIndexBuss.queryAscByCode(code).stream().forEach(prodIndex -> {
			
		});
		
		return null;
	}

}

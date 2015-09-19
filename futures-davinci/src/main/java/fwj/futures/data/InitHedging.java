package fwj.futures.data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;

import fwj.futures.data.enu.ProdEnum;
import fwj.futures.data.launch.AbstractBaseLaunch;
import fwj.futures.data.struct.Formula;
import fwj.futures.resource.entity.Hedging;
import fwj.futures.resource.repository.HedgingRepository;

@Component
public class InitHedging extends AbstractBaseLaunch {

	@Autowired
	private HedgingRepository hedgingRepository;

	@Override
	protected void execute() throws Exception {

		List<Hedging> hedgingList = new ArrayList<>();

		Hedging hedging1 = new Hedging();
		hedging1.setName("豆油棕榈油套利1");
		hedging1.setDesc("");
		hedging1.setStartDt("2014-03-03");
		hedging1.setUpLimit(new BigDecimal(100));
		hedging1.setDownLimit(new BigDecimal(-100));
		hedging1.setExpression(JSON.toJSONString(Formula.create().putConstant("-587.08732")
				.putMultinomial(ProdEnum.DouYou, "1").putMultinomial(ProdEnum.ZongLvYou, "-1.03179")));
		hedgingList.add(hedging1);
		System.out.println(JSON.toJSONString(hedging1));
		
		

		Hedging hedging3 = new Hedging();
		hedging3.setName("焦炭焦煤套利1");
		hedging3.setDesc("");
		hedging3.setStartDt("2014-01-01");
		hedging3.setUpLimit(new BigDecimal(40));
		hedging3.setDownLimit(new BigDecimal(-40));
		hedging3.setExpression(JSON.toJSONString(Formula.create().putConstant("162.31")
				.putMultinomial(ProdEnum.JiaoTan, "1").putMultinomial(ProdEnum.JiaoMei, "-1.60584")));
		hedgingList.add(hedging3);
		System.out.println(JSON.toJSONString(hedging3));
		

		for (Hedging ne : hedgingList) {
			Hedging old = hedgingRepository.findByName(ne.getName());
			if (old == null) {
				hedgingRepository.save(ne);
			} else {
				old.setName(ne.getName());
				old.setDesc(ne.getDescription());
				old.setStartDt(ne.getStartDt());
				old.setUpLimit(ne.getUpLimit());
				old.setDownLimit(ne.getDownLimit());
				old.setExpression(ne.getExpression());
				hedgingRepository.save(old);
			}
		}

	}

	public static void main(String[] args) {
		launch(InitHedging.class);
	}
}

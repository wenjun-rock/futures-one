package fwj.futures.data.init.hedging;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;

import fwj.futures.data.enu.ProdEnum;
import fwj.futures.data.launch.AbstractBaseLaunch;
import fwj.futures.data.struct.Formula;
import fwj.futures.resource.entity.hedging.Hedging;
import fwj.futures.resource.repository.hedging.HedgingRepository;

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
		hedging1.setUpLimit(new BigDecimal(300));
		hedging1.setDownLimit(new BigDecimal(-300));
		hedging1.setExpression(JSON.toJSONString(Formula.create().putConstant("-587.08732")
				.putMultinomial(ProdEnum.DouYou, "1").putMultinomial(ProdEnum.ZongLvYou, "-1.03179")));
		hedgingList.add(hedging1);
		System.out.println(JSON.toJSONString(hedging1));
		
	
		Hedging hedging3 = new Hedging();
		hedging3.setName("焦炭焦煤套利1");
		hedging3.setDesc("");
		hedging3.setUpLimit(new BigDecimal(30));
		hedging3.setDownLimit(new BigDecimal(-30));
		hedging3.setExpression(JSON.toJSONString(Formula.create().putConstant("162.31")
				.putMultinomial(ProdEnum.JiaoTan, "1").putMultinomial(ProdEnum.JiaoMei, "-1.60584")));
		hedgingList.add(hedging3);
		System.out.println(JSON.toJSONString(hedging3));
		
		Hedging hedging4 = new Hedging();
		hedging4.setName("豆油棕榈油套利2");
		hedging4.setDesc("");
		hedging4.setUpLimit(new BigDecimal(300));
		hedging4.setDownLimit(new BigDecimal(-300));
		hedging4.setExpression(JSON.toJSONString(Formula.create().putConstant("2881.57")
				.putMultinomial(ProdEnum.DouYou, "-1").putMultinomial(ProdEnum.ZongLvYou, "0.5619586")));
		hedgingList.add(hedging4);
		System.out.println(JSON.toJSONString(hedging4));
		
		Hedging hedging5 = new Hedging();
		hedging5.setName("豆油棕榈油套利3");
		hedging5.setDesc("");
		hedging5.setUpLimit(new BigDecimal(300));
		hedging5.setDownLimit(new BigDecimal(-300));
		hedging5.setExpression(JSON.toJSONString(Formula.create().putConstant("3162.88")
				.putMultinomial(ProdEnum.DouYou, "-1.430981").putMultinomial(ProdEnum.ZongLvYou, "1")));
		hedgingList.add(hedging5);
		System.out.println(JSON.toJSONString(hedging5));
		

		for (Hedging ne : hedgingList) {
			Hedging old = hedgingRepository.findByName(ne.getName());
			if (old == null) {
				hedgingRepository.save(ne);
			} else {
				old.setName(ne.getName());
				old.setDesc(ne.getDescription());
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

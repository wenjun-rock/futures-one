package fwj.futures.data.init.prod;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fwj.futures.data.enu.ProdEnum;
import fwj.futures.data.launch.AbstractBaseLaunch;
import fwj.futures.resource.entity.prod.Futures;
import fwj.futures.resource.repository.prod.FuturesRepository;

@Component
public class InitFutures extends AbstractBaseLaunch {

	@Autowired
	private FuturesRepository productRepository;

	@Override
	protected void execute() throws Exception {

		for (ProdEnum ele : ProdEnum.values()) {
			Futures prod = productRepository.findByCode(ele.getCode());
			if (prod == null) {
				prod = new Futures();
			}
			prod.setCode(ele.getCode());
			prod.setName(ele.getName());
			prod.setUnit(ele.getUnit());
			prod.setUnitDesc(ele.getUnitDesc());
			prod.setExchange(ele.getExchange().toString());
			prod.setActive(1);
			productRepository.save(prod);
		}

		System.out.println("done!");

	}

	public static void main(String[] args) {
		launch(InitFutures.class);
	}
}

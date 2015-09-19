package fwj.futures.data;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;

import fwj.futures.data.launch.AbstractBaseLaunch;
import fwj.futures.resource.entity.Product;
import fwj.futures.resource.repository.ProductRepository;

@Component
public class InitProduct extends AbstractBaseLaunch {

	@Autowired
	private ProductRepository productRepository;

	@Override
	protected void execute() throws Exception {
		
		for(fwj.futures.data.enu.ProdEnum ele : fwj.futures.data.enu.ProdEnum.values()){
			Product prod = productRepository.findByCode(ele.getCode());
			if(prod == null) {
				prod = new Product();
			}
			System.out.println(ele.getExchange().toString());
			prod.setCode(ele.getCode());
			prod.setName(ele.getName());
			prod.setExchange(ele.getExchange().toString());
			prod.setActive("1");
			productRepository.save(prod);
		}
		
		List<Product> products = productRepository.findAll();
		System.out.println(JSON.toJSONString(products));
		
	}

	public static void main(String[] args) {
		launch(InitProduct.class);
	}
}

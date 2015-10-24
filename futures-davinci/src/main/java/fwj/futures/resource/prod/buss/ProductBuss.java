package fwj.futures.resource.prod.buss;

import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import fwj.futures.resource.prod.entity.Futures;
import fwj.futures.resource.prod.entity.FuturesTradeTime;
import fwj.futures.resource.prod.entity.Label;
import fwj.futures.resource.prod.entity.LabelFutures;
import fwj.futures.resource.prod.entity.ProdMainCon;
import fwj.futures.resource.prod.repos.FuturesRepos;
import fwj.futures.resource.prod.repos.FuturesTradeTimeRepos;
import fwj.futures.resource.prod.repos.LabelFuturesRepos;
import fwj.futures.resource.prod.repos.LabelRepos;
import fwj.futures.resource.prod.repos.ProdMainConRepos;
import fwj.futures.resource.prod.vo.ProductInfo;
import fwj.futures.resource.prod.vo.ProductLabel;
import fwj.futures.resource.prod.vo.ProductInfo.InnerLabel;
import fwj.futures.resource.prod.vo.ProductLabel.InnerProduct;

@Component
public class ProductBuss {

	@Autowired
	private FuturesRepos futuresRepo;

	@Autowired
	private LabelFuturesRepos labelFuturesRepo;

	@Autowired
	private LabelRepos labelRepository;

	@Autowired
	private FuturesTradeTimeRepos tradeTimeRepo;

	@Autowired
	private ProdMainConRepos prodMainConRepos;

	@Cacheable(value = "ProductBuss.queryAllLabels")
	public List<ProductLabel> queryAllLabels() {
		return labelRepository.findAll(new Sort("rank")).stream().map(label -> this.queryLabel(label))
				.collect(Collectors.toList());
	}

	@Cacheable(value = "ProductBuss.queryLabel")
	public ProductLabel queryLabel(Integer labelId) {
		return this.queryLabel(labelRepository.findOne(labelId));
	}

	@Cacheable(value = "ProductBuss.queryLabel", key = "#label.id")
	private ProductLabel queryLabel(Label label) {
		ProductLabel productLabel = new ProductLabel();
		productLabel.setId(label.getId());
		productLabel.setName(label.getName());
		List<LabelFutures> labelFuturesList = labelFuturesRepo.findByLabelIdOrderByFuturesCodeAsc(label.getId());
		if (labelFuturesList == null) {
			productLabel.setProducts(Collections.emptyList());
		} else {
			productLabel.setProducts(labelFuturesList.stream().map(labelFutures -> {
				InnerProduct innerProduct = new InnerProduct();
				innerProduct.setCode(labelFutures.getFuturesCode());
				innerProduct.setName(labelFutures.getFuturesName());
				return innerProduct;
			}).collect(Collectors.toList()));
		}
		return productLabel;
	}

	@Cacheable(value = "ProductBuss.queryInfoByCode")
	public ProductInfo queryInfoByCode(String code) {
		ProductInfo info = new ProductInfo();
		Futures futures = futuresRepo.findByCode(code);
		info.setCode(futures.getCode());
		info.setName(futures.getName());
		info.setUnit(futures.getUnit());
		info.setUnitDesc(futures.getUnitDesc());
		info.setLabels(labelFuturesRepo.findByFuturesCodeOrderByLabelRankAsc(code).stream().map(label -> {
			InnerLabel innerLabel = new InnerLabel();
			innerLabel.setLabelId(label.getLabelId());
			innerLabel.setLabelName(label.getLabelName());
			return innerLabel;
		}).collect(Collectors.toList()));
		info.setTradeTimes(tradeTimeRepo.findByCodeOrderByStartTimeAsc(code));
		return info;
	}

	@Cacheable(value = "ProductBuss.queryAllCode")
	public List<String> queryAllCode() {
		return futuresRepo.findAllActive().stream().map(Futures::getCode).collect(Collectors.toList());
	}

	@Cacheable(value = "ProductBuss.queryAllFutures")
	public List<Futures> queryAllFutures() {
		return futuresRepo.findAllActive();
	}

	@Cacheable(value = "ProductBuss.queryCodeByLabelId")
	public List<String> queryCodeByLabelId(Integer id) {
		return labelFuturesRepo.findByLabelId(id).stream().map(LabelFutures::getFuturesCode)
				.collect(Collectors.toList());
	}

	@Cacheable(value = "ProductBuss.queryFuturesByCode")
	public Futures queryFuturesByCode(String code) {
		return futuresRepo.findByCode(code);
	}

	@Cacheable(value = "ProductBuss.queryTradeTimesByCode")
	public List<FuturesTradeTime> queryTradeTimesByCode(String code) {
		return tradeTimeRepo.findByCodeOrderByStartTimeAsc(code);
	}

	@Cacheable(value = "ProductBuss.queryAllTradeTimes")
	public List<FuturesTradeTime> queryAllTradeTimes() {
		return tradeTimeRepo.findAll();
	}

	@Cacheable(value = "ProductBuss.queryMainConByCode")
	public List<ProdMainCon> queryMainConByCode(String code) {
		List<ProdMainCon> list = prodMainConRepos.findByCode(code);

		int currentMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;
		list.sort((l, r) -> {
			int i1 = l.getMonth() <= currentMonth ? l.getMonth() + 12 : l.getMonth();
			int i2 = r.getMonth() <= currentMonth ? r.getMonth() + 12 : r.getMonth();
			return i1 - i2;
		});
		return list;
	}

}

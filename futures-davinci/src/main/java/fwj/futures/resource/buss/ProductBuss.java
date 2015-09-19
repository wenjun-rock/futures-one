package fwj.futures.resource.buss;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import fwj.futures.resource.entity.Futures;
import fwj.futures.resource.entity.KLine;
import fwj.futures.resource.entity.Label;
import fwj.futures.resource.entity.LabelFutures;
import fwj.futures.resource.repository.FuturesRepository;
import fwj.futures.resource.repository.KLineRepository;
import fwj.futures.resource.repository.LabelFuturesRepository;
import fwj.futures.resource.repository.LabelRepository;
import fwj.futures.resource.vo.ProductInfo;
import fwj.futures.resource.vo.ProductLabel;
import fwj.futures.resource.vo.ProductLabel.InnerProduct;

@Component
public class ProductBuss {

	@Autowired
	private FuturesRepository futuresRepo;

	@Autowired
	private LabelFuturesRepository labelFuturesRepo;

	@Autowired
	private LabelRepository labelRepository;

	@Autowired
	private KLineRepository kLineRepository;

	@Cacheable(value = "ProductLabelBuss.queryAllLabels")
	public List<ProductLabel> queryAllLabels() {
		return labelRepository.findAll(new Sort("rank")).stream().map(label -> this.queryLabel(label))
				.collect(Collectors.toList());
	}

	@Cacheable(value = "ProductLabelBuss.queryLabel")
	public ProductLabel queryLabel(Integer labelId) {
		return this.queryLabel(labelRepository.findOne(labelId));
	}

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

	@Cacheable(value = "ProductLabelBuss.queryAllCode")
	public List<String> queryAllCode() {
		return futuresRepo.findAllActive().stream().map(Futures::getCode).collect(Collectors.toList());
	}

	@Cacheable(value = "ProductLabelBuss.queryCodeByLabelId")
	public List<String> queryCodeByLabelId(Integer id) {
		return labelFuturesRepo.findByLabelId(id).stream().map(LabelFutures::getFuturesCode)
				.collect(Collectors.toList());
	}

	@Cacheable(value = "ProductLabelBuss.queryFuturesByCode")
	public Futures queryFuturesByCode(String code) {
		return futuresRepo.findByCode(code);
	}

	public List<KLineDtCodeGroup> queryEndPrice(List<String> codeList) {
		Map<String, Map<String, KLine>> kLineMap = codeList.stream().map(code -> kLineRepository.findByCode(code))
				.flatMap(kLineList -> kLineList.stream())
				.collect(Collectors.groupingBy(KLine::getDt, Collectors.toMap(KLine::getCode, kLine -> kLine)));
		return kLineMap.entrySet().stream().map(entry -> new KLineDtCodeGroup(entry.getKey(), entry.getValue()))
				.sorted().collect(Collectors.toList());
	}

	public static class KLineDtCodeGroup implements Comparable<KLineDtCodeGroup> {

		private String dt;
		private Map<String, KLine> kLineMap;

		public KLineDtCodeGroup(String dt, Map<String, KLine> kLineMap) {
			this.dt = dt;
			this.kLineMap = kLineMap;
		}

		public String getDt() {
			return dt;
		}

		public Map<String, KLine> getkLineMap() {
			return kLineMap;
		}

		@Override
		public int compareTo(KLineDtCodeGroup that) {
			return this.dt.compareTo(that.dt);
		}
	}

	public ProductInfo queryInfoByCode(String code) {
		// TODO Auto-generated method stub
		return null;
	}

}

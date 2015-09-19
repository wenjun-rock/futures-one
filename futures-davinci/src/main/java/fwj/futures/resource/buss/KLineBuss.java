package fwj.futures.resource.buss;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import fwj.futures.resource.entity.KLine;
import fwj.futures.resource.repository.KLineRepository;

@Component
public class KLineBuss {

	@Autowired
	private KLineRepository kLineRepository;

	@Cacheable(value = "KLineBuss.queryLatest60ByCode")
	public List<KLine> queryLatest60ByCode(String code) {
		return kLineRepository.findTop60ByCodeOrderByDtDesc(code);
	}
	
	@Cacheable(value = "KLineBuss.findByCode")
	public List<KLine> findByCode(String code) {
		return kLineRepository.findByCode(code);
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

}

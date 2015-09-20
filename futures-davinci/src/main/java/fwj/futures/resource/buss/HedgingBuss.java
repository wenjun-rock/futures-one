package fwj.futures.resource.buss;

import java.math.BigDecimal;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fwj.futures.data.struct.Formula;
import fwj.futures.data.struct.Formula.Multinomial;
import fwj.futures.resource.entity.Hedging;
import fwj.futures.resource.entity.KLine;
import fwj.futures.resource.repository.HedgingRepository;
import fwj.futures.resource.vo.KLineGroup;
import fwj.futures.resource.vo.UnitData;
import fwj.futures.resource.vo.UnitDataGroup;

@Component
public class HedgingBuss {

	@Autowired
	private HedgingRepository hedgingRepo;

	public Hedging getById(Integer id) {
		return hedgingRepo.findOne(id);
	}

	public BigDecimal calculate(Formula fomular, KLineGroup group) {
		BigDecimal result = fomular.getConstant();
		for (Multinomial multinomial : fomular.getMultinomials()) {
			KLine kLine = group.getkLineMap().get(multinomial.getCode());
			if (kLine == null) {
				return null;
			} else {
				result = result.add(multinomial.getCoefficient().multiply(kLine.getEndPrice()));
			}
		}
		return result;
	}

	public BigDecimal calculate(Formula fomular, UnitDataGroup unitDataGroup) {
		BigDecimal result = fomular.getConstant();
		Map<String, BigDecimal> map = unitDataGroup.getUnitDataList().stream()
				.collect(Collectors.toMap(UnitData::getCode, UnitData::getPrice));
		for (Multinomial multinomial : fomular.getMultinomials()) {
			if (map.get(multinomial.getCode()) == null) {
				return null;
			} else {
				result = result.add(multinomial.getCoefficient().multiply(map.get(multinomial.getCode())));
			}
		}
		return result;
	}

}

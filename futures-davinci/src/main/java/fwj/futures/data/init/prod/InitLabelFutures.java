package fwj.futures.data.init.prod;

import java.util.List;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fwj.futures.data.enu.Exchange;
import fwj.futures.data.enu.ProdEnum;
import fwj.futures.data.launch.AbstractBaseLaunch;
import fwj.futures.resource.prod.entity.Label;
import fwj.futures.resource.prod.entity.LabelFutures;
import fwj.futures.resource.prod.repos.LabelFuturesRepos;
import fwj.futures.resource.prod.repos.LabelRepos;

@Component
public class InitLabelFutures extends AbstractBaseLaunch {

	@Autowired
	private LabelRepos labelRepository;

	@Autowired
	private LabelFuturesRepos labelFuturesRepository;

	@Override
	protected void execute() throws Exception {

		Input[] define = new Input[] { //
				new Input("上海", ProdEnum.findByExchange(Exchange.SH)), //
				new Input("大连", ProdEnum.findByExchange(Exchange.DL)), //
				new Input("郑州", ProdEnum.findByExchange(Exchange.ZZ)), //
				new Input("金属", ProdEnum.HuangJin, ProdEnum.Baiyin, ProdEnum.Tong, ProdEnum.Lv, ProdEnum.Xin,
						ProdEnum.Qian, ProdEnum.Xi, ProdEnum.Nie), //
				new Input("黑色", ProdEnum.LuoWenGang, ProdEnum.ReJuan, ProdEnum.JiaoTan, ProdEnum.JiaoMei,
						ProdEnum.TieKuangShi, ProdEnum.DongLiMeiZC), //
				new Input("农产", ProdEnum.DaDou1, ProdEnum.YuMi, ProdEnum.DouPo, ProdEnum.DouYou, ProdEnum.JiDan,
						ProdEnum.YuDian, ProdEnum.QiangMai, ProdEnum.BaiTang, ProdEnum.MianHua, ProdEnum.CaiYou,
						ProdEnum.CaiPo, ProdEnum.ZongLvYou), //
				new Input("化工", ProdEnum.SuLiao, ProdEnum.JuBinXi, ProdEnum.PTA, ProdEnum.JuLvYiXi, ProdEnum.BoLi,
						ProdEnum.JiaChun, ProdEnum.LiQing, ProdEnum.XiangJiao), //
				new Input("贵金属", ProdEnum.HuangJin, ProdEnum.Baiyin), //
				new Input("油脂套利", ProdEnum.DouYou, ProdEnum.ZongLvYou, ProdEnum.CaiYou), //
				new Input("饲料套利", ProdEnum.DouPo, ProdEnum.CaiPo, ProdEnum.YuMi, ProdEnum.JiDan), //
				new Input("油粕套利", ProdEnum.DouYou, ProdEnum.DouPo, ProdEnum.CaiYou, ProdEnum.CaiPo), //
				new Input("煤炭套利", ProdEnum.JiaoTan, ProdEnum.JiaoMei, ProdEnum.DongLiMeiZC) //
		};

		Stream.of(define).forEach(input -> {
			List<LabelFutures> oldObjs = labelFuturesRepository.findByLabelName(input.labelName);
			labelFuturesRepository.delete(oldObjs);
			Label label = labelRepository.findByName(input.labelName);
			Stream.of(input.futuresArr).forEach(prodEnum -> {
				LabelFutures labelFutures = new LabelFutures();
				labelFutures.setLabelId(label.getId());
				labelFutures.setLabelName(label.getName());
				labelFutures.setLabelRank(label.getRank());
				labelFutures.setFuturesCode(prodEnum.getCode());
				labelFutures.setFuturesName(prodEnum.getName());
				labelFuturesRepository.save(labelFutures);
			});
		});

		System.out.println("done!");

	}

	public static void main(String[] args) {
		launch(InitLabelFutures.class);
	}

	private class Input {
		private String labelName;
		private ProdEnum[] futuresArr;

		private Input(String labelName, ProdEnum... futuresArr) {
			this.labelName = labelName;
			this.futuresArr = futuresArr;
		}
	}
}

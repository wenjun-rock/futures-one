package fwj.futures.data;

import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fwj.futures.data.launch.AbstractBaseLaunch;
import fwj.futures.resource.entity.Label;
import fwj.futures.resource.repository.LabelRepository;

@Component
public class InitLabel extends AbstractBaseLaunch {

	@Autowired
	private LabelRepository labelRepository;

	@Override
	protected void execute() throws Exception {

		Input[] define = new Input[] { //
				new Input("上海", 1, true), //
				new Input("大连", 2, true), //
				new Input("郑州", 3, true), //
				new Input("金属", 10, true), //
				new Input("黑色", 20, true), //
				new Input("农产", 30, true), //
				new Input("化工", 40, true), //
				new Input("贵金属", 50, true), //
				new Input("油脂套利", 110, false), //
				new Input("饲料套利", 120, false), //
				new Input("油粕套利", 130, false), //
				new Input("煤炭套利", 140, false) //
		};

		Stream.of(define).forEach(input -> {
			Label label = labelRepository.findByName(input.name);
			label = label == null ? new Label() : label;
			label.setName(input.name);
			label.setRank(input.rank);
			label.setLocked(input.lock ? "1" : "0");
			labelRepository.save(label);
		});
		
		System.out.println("done!");

	}

	public static void main(String[] args) {
		launch(InitLabel.class);
	}

	private class Input {
		private String name;
		private int rank;
		private boolean lock;

		private Input(String name, int rank, boolean lock) {
			this.name = name;
			this.rank = rank;
			this.lock = lock;
		}
	}
}

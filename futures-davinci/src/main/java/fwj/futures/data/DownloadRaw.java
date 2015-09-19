package fwj.futures.data;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.io.Files;
import com.google.common.io.Resources;

import fwj.futures.data.enu.ProdEnum;
import fwj.futures.data.launch.AbstractBaseLaunch;
import fwj.futures.data.process.DataURI;

@Component
public class DownloadRaw extends AbstractBaseLaunch {

	@Autowired
	private DataURI dataURI;

	@Override
	protected void execute() throws Exception {
		for (ProdEnum prod : ProdEnum.values()) {
			System.out.println(String.format("Downloading the data of %s...", prod));
			byte[] data = Resources.toByteArray(dataURI.getDailyKLineUrl(prod));
			Files.asByteSink(dataURI.getDailyKLineFile(prod, new Date())).write(data);
		}
		System.out.println("Done!");
	}

	public static void main(String[] args) {
		launch(DownloadRaw.class);
	}
}

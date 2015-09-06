package cn.fwj.futures.data.buss.model;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.io.Files;
import com.google.common.io.Resources;

import cn.fwj.futures.data.enu.Product;
import cn.fwj.futures.data.launch.AbstractBaseLaunch;
import cn.fwj.futures.data.process.DataURI;

@Component
public class DownloadRaw extends AbstractBaseLaunch {

	@Autowired
	private DataURI dataURI;

	@Override
	protected void execute() throws Exception {
		for (Product prod : Product.values()) {
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

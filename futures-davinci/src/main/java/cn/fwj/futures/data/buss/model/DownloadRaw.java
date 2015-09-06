package cn.fwj.futures.data.buss.model;

import java.util.Date;

import com.google.common.io.Files;
import com.google.common.io.Resources;

import cn.fwj.futures.data.enu.Product;
import cn.fwj.futures.data.process.DataURI;

public class DownloadRaw {

	public static void main(String[] args) throws Exception {
		for (Product prod : Product.values()) {
			System.out.println(String.format("Downloading the data of %s...", prod));
			byte[] data = Resources.toByteArray(DataURI.get().getDailyKLineUrl(prod));
			Files.asByteSink(DataURI.get().getDailyKLineFile(prod, new Date())).write(data);
		}
		System.out.println("Done!");
	}

}

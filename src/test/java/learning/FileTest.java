package learning;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

public class FileTest {

	@Test
	public void test() {
		File base = new File("/home/kondo/code/myplugin/gallery/src/main/resources/images");
		travarse(base);
	}

	private void travarse(File base) {
		File[] files = base.listFiles();
		for (File file : files) {
			if(file.isDirectory()){
				System.out.println("[" + file.getAbsolutePath() + "]");
				travarse(file);
			}else{
				System.out.println(file.getAbsolutePath());
			}
		}
	}

}

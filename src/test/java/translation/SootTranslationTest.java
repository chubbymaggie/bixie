package translation;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import bixie.translation.Main;

public class SootTranslationTest extends AbstractIcTest {

	@Test
	public void test() {

		File classFileDir = null;
		try {

			final File source_dir = new File(testRoot + "soot_tests/");
			List<File> sourceFiles = new LinkedList<File>();
			File[] directoryListing = source_dir.listFiles();
			if (directoryListing != null) {
				for (File child : directoryListing) {
					if (child.isFile() && child.getName().endsWith(".java")) {
						sourceFiles.add(child);
					}
				}
			}

			classFileDir = compileJavaFiles(sourceFiles.toArray(new File[sourceFiles.size()]));
			Main
					.main(new String[] { "-j", classFileDir.getAbsolutePath() });
		} catch (IOException e) {
			e.printStackTrace();
			fail("Translation failed.");
		} finally {
			if (classFileDir != null) {
				try {
					delete(classFileDir);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

}

package bixie.ic_test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;

import org.junit.AfterClass;

/**
 * Abstract class for all test cases that detect inconsistent code.
 * 
 * @author schaef
 * 
 */
public class AbstractIcTest {

	protected static final String userDir = 
			System.getProperty("user.dir") + "/";
	protected static final String testRoot = 
			userDir + "src/test/resources/";

	@edu.umd.cs.findbugs.annotations.SuppressWarnings(
		    value="DM_DEFAULT_ENCODING", 
		    justification="I know what I'm doing")
	protected String fileToString(File f) {
		StringBuffer sb = new StringBuffer();
		try (FileReader fileRead = new FileReader(f);				
				BufferedReader reader = new BufferedReader(fileRead);) {
			String line;
			while (true) {
				line = reader.readLine();
				if (line==null) break;
				sb.append(line);
				sb.append("\n");
			}
		} catch (Throwable e) {
			
		}
		return sb.toString();
	}
	
	@edu.umd.cs.findbugs.annotations.SuppressWarnings(
		    value="DM_DEFAULT_ENCODING", 
		    justification="I know what I'm doing")
	protected boolean compareFiles(File out, File gold) {
		try (FileReader fR1 = new FileReader(out);
				FileReader fR2 = new FileReader(gold);
				BufferedReader reader1 = new BufferedReader(fR1);
				BufferedReader reader2 = new BufferedReader(fR2);) {
			String line1, line2;
			while (true) // Continue while there are equal lines
			{
				line1 = reader1.readLine();
				line2 = reader2.readLine();

				// End of file 1
				if (line1 == null) {
					// Equal only if file 2 also ended
					return (line2 == null ? true : false);
				}

				// Different lines, or end of file 2
				if (!line1.equalsIgnoreCase(line2)) {
					return false;
				}
			}
		} catch (Exception e) {
			return false;
		}
	}

	protected File compileJavaFile(File sourceFile) throws IOException {
		final File tempDir = getTempDir();
		final String javac_command = String.format(
				"javac -g %s -d %s", sourceFile.getAbsolutePath(),
				tempDir.getAbsolutePath());

		ProcessBuilder pb = new ProcessBuilder(javac_command.split(" "));
		pb.redirectOutput(Redirect.INHERIT);
		pb.redirectError(Redirect.INHERIT);
		Process p = pb.start();

		try {
			p.waitFor();
		} catch (InterruptedException e) {
			e.printStackTrace();
			return null;
		}

		return tempDir;
	}

	protected File getTempDir() throws IOException {
		final File tempDir = File.createTempFile("bixie_test_temp",
				Long.toString(System.nanoTime()));
		if (!(tempDir.delete())) {
			throw new IOException("Could not delete temp file: "
					+ tempDir.getAbsolutePath());
		}
		if (!(tempDir.mkdir())) {
			throw new IOException("Could not create temp directory: "
					+ tempDir.getAbsolutePath());
		}
		return tempDir;
	}

	protected void delete(File f) throws IOException {
		if (f.isDirectory()) {
			for (File c : f.listFiles())
				delete(c);
		}
		if (!f.delete()) {
			throw new IOException("Failed to delete file: " + f);
		}
	}

	@AfterClass
	public static void tearDown() {
		org.joogie.GlobalsCache.resetInstance();
	}

}

package tspsolver.util.copy;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public abstract class FileCopy {

	public static void copy(File source, File destination) throws IOException {
		try (InputStream inputStream = new FileInputStream(source); OutputStream outputStream = new FileOutputStream(destination)) {
			byte[] buffer = new byte[1024];

			int length = 0;
			while ((length = inputStream.read(buffer)) > 0) {
				outputStream.write(buffer, 0, length);
			}
		}
		catch (IOException exception) {
			throw exception;
		}
	}
}

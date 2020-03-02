package com.teamium.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.teamium.exception.UnprocessableEntityException;

/**
 * <p>
 * A class for reading file
 * </p>
 * 
 * @author Avinash Gupta
 *
 */
public class FileReader {
	
	protected final static Logger logger = LoggerFactory.getLogger(FileReader.class);
	
	/**
	 * To convert a particular file into Base64 encoded byte stream.
	 * 
	 * @param absolutefilePath
	 *            the absolutefilePath
	 * 
	 * @return encoded Bse64 byte array.
	 * 
	 * @throws IOException
	 */
	public static byte[] getEncodedFileByteArray(String absolutefilePath) throws IOException {
		Path path = Paths.get(absolutefilePath);
		if (!Files.exists(path)) {
			logger.error("The requested resourse : " + absolutefilePath + " is not available.");
			throw new UnprocessableEntityException("Requested resourse is not available.");
		}
		byte[] data = Files.readAllBytes(path);
		return Base64.getEncoder().encode(data);
	}
	public static void deleteFile(String absolutePath) {
		try {
			File file = new File(absolutePath);
			if (file.delete()) {
				logger.error("The File:" + absolutePath + "deleted");
			}
		} catch (Exception e) {
			logger.error("The File:" + absolutePath + "  could not deleted");
		}
	}
}

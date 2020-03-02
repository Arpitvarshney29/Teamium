package com.teamium.utils;

import java.io.File;
import java.nio.file.Paths;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.io.Files;

/**
 * <p>
 * A class for writing file
 * </p>
 * 
 * @author Avinash Gupta
 *
 */
public class FileWriter {

	protected final static Logger logger = LoggerFactory.getLogger(FileWriter.class);

	/**
	 * To save input/output schedule file in their specified directory.
	 * 
	 * @param fileContent
	 * @param basePath
	 * @param fileName
	 * @param channelCode
	 * @param folderType
	 * @param version
	 * @param extension
	 * 
	 * @return absolute file path of saved schedule file.
	 * 
	 * @throws Exception
	 */
	public static String saveFile(byte[] fileContent, String path, String fileName) throws Exception {
		String dirPath = Paths.get(path+"/" + fileName).toString();

		String absolutePath = dirPath;
		File dirFile = new java.io.File(path.toString());
		if (!dirFile.exists()) {
			if (!dirFile.mkdirs()) {
				logger.error("The File: " + fileName + ", could not be uploaded, because no upload dir exists.");
				throw new Exception("The File: " + fileName + ", could not be uploaded, because no upload dir exists.");
			}
		}
		Files.write(fileContent, new java.io.File(absolutePath));
		return absolutePath;
	}
	
	/**
	 * To delete a particular file in on an absolutePath
	 * 
	 * @param absolutePath
	 *            the absolutePath
	 */
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

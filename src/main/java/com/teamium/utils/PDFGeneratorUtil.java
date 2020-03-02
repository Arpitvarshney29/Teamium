package com.teamium.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.teamium.exception.UnprocessableEntityException;
import com.lowagie.text.DocumentException;

/**
 * <p>
 * A class for generating PDF
 * </p>
 * 
 * @author Avinash Gupta
 *
 */
public class PDFGeneratorUtil {

	private final static Logger logger = LoggerFactory.getLogger(PDFGeneratorUtil.class);

	/**
	 * To return byteArray of a pdf/excel.
	 * 
	 * @param htmlContent
	 *            the htmlContent.
	 * 
	 * @return the byteArray.
	 * 
	 * @throws IOException
	 */
	public static byte[] createPdf(String htmlContent) {
		ByteArrayOutputStream os = null;
		os = new ByteArrayOutputStream();
		ITextRenderer renderer = new ITextRenderer();
		renderer.setDocumentFromString(htmlContent);
		renderer.layout();
		try {
			renderer.createPDF(os, false);
		} catch (DocumentException e) {
			logger.error("Unable to create PDF.");
			throw new UnprocessableEntityException("Unable to create PDF.");
		}
		renderer.finishPDF();
		return Base64.getEncoder().encode(os.toByteArray());
	}

	/**
	 * Method to create New PDF
	 * 
	 * @param htmlContent
	 *            the htmlContent
	 * 
	 * @return PDF content in byte Array
	 * 
	 * @throws Exception
	 */
	public static byte[] createNewPdf(String htmlContent) throws Exception {

		FileOutputStream os = null;
		String fileName = UUID.randomUUID().toString();
		try {
			final File outputFile = new File("/" + fileName + ".pdf");
			os = new FileOutputStream(outputFile);
			FileUtils.writeStringToFile(new java.io.File("/report.html"), htmlContent);
			ITextRenderer renderer = new ITextRenderer();
			renderer.setDocumentFromString(htmlContent);
			renderer.layout();
			renderer.createPDF(os, false);
			renderer.finishPDF();
			System.out.println("PDF created successfully");
		} finally {
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
					logger.error("Exception occured : " + e.getMessage());
					e.printStackTrace();
				}
			}
		}
		return Base64.getEncoder().encode(new ByteArrayOutputStream(10).toByteArray());
	}

}

package com.teamium.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import javax.validation.constraints.NotNull;

import org.apache.commons.io.FileUtils;
import org.hibernate.validator.constraints.NotBlank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

@Service
public class EditionPdfUtil {

	@Autowired
	@Qualifier("editionTemplate")
	private TemplateEngine templateEngine;
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	public String createPdf(@NotNull String templateName, Map map, @NotNull String fileLocation) throws Exception {
		Context ctx = new Context();
		String createdFileName = null;
		if (map != null) {
			ctx.setVariables(map);
		}

		String processedHtml = templateEngine.process(templateName, ctx);
		FileOutputStream os = null;
		String fileName = templateName;
		// String fileName = UUID.randomUUID().toString();
		try {
			File path = new File(fileLocation);
			if (path.exists()) {
				try {
					FileUtils.deleteDirectory(path);
				} catch (Exception e) {
					logger.info("Error while deleting previous file " + e.getMessage());
				}
			}
			path.mkdirs();

			final File outputFile = File.createTempFile(fileName, ".pdf", path);

			os = new FileOutputStream(outputFile);
			// to save the html to use in future
			FileUtils.writeStringToFile(new File(path, fileName + ".html"), processedHtml);
			ITextRenderer renderer = new ITextRenderer();
			renderer.setDocumentFromString(processedHtml);
			renderer.layout();
			renderer.createPDF(os, false);
			renderer.finishPDF();
			createdFileName = outputFile.getName();
			System.out.println("PDF created successfully with name : " + outputFile.getName());
		} finally {
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
					/* ignore */ }
			}
		}
		return createdFileName;
	}

	/**
	 * Create pdf without prevoius delete
	 * 
	 * @param templateName
	 * @param map
	 * @param fileLocation
	 * 
	 * @return createdFileName
	 * 
	 * @throws Exception
	 */
	public String createPdfWithoutPreviousDelete(@NotNull String templateName, Map map, @NotNull String fileLocation)
			throws Exception {
		Context ctx = new Context();
		String createdFileName = null;
		if (map != null) {
			ctx.setVariables(map);
		}

		String processedHtml = templateEngine.process(templateName, ctx);
		FileOutputStream os = null;
		String fileName = templateName;
		// String fileName = UUID.randomUUID().toString();
		try {
			File path = new File(fileLocation);
			if (path.exists()) {
				try {
					// FileUtils.deleteDirectory(path);
				} catch (Exception e) {
					logger.info("Error while deleting previous file " + e.getMessage());
				}
			}
			path.mkdirs();

			final File outputFile = File.createTempFile(fileName, ".pdf", path);

			os = new FileOutputStream(outputFile);
			// to save the html to use in future
			FileUtils.writeStringToFile(new File(path, fileName + ".html"), processedHtml);
			ITextRenderer renderer = new ITextRenderer();
			renderer.setDocumentFromString(processedHtml);
			renderer.layout();
			renderer.createPDF(os, false);
			renderer.finishPDF();
			createdFileName = outputFile.getName();
			System.out.println("PDF created successfully with name : " + outputFile.getName());
		} finally {
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
					/* ignore */ }
			}
		}
		return createdFileName;
	}
}

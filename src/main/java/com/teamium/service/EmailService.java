package com.teamium.service;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.InternetHeaders;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.teamium.config.PropConfig;
import com.teamium.domain.prod.resources.staff.StaffMember;
import com.teamium.exception.UnprocessableEntityException;

/**
 * A service class implementation for sending Email
 *
 */
@Service
public class EmailService {

	private JavaMailSender mailSender;
	private TemplateEngine templateEngine;
	private PropConfig propConfig;

	@Autowired
	public EmailService(JavaMailSender mailSender, TemplateEngine templateEngine, PropConfig propConfig) {
		this.mailSender = mailSender;
		this.templateEngine = templateEngine;
		this.propConfig = propConfig;
	}

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * Method to send mail
	 * 
	 * @param sendFrom
	 *            the sendFrom
	 * @param sendTo
	 *            the sendTo
	 * @param subject
	 *            the subject
	 * @param message
	 *            the message
	 * @param ccEmailList
	 *            the ccEmailList
	 */
	public void sendEMail(final String sendFrom, final String sendTo, final String subject, final String message,
			List<String> ccEmailList) {
		logger.info("Inside EmailService::sendEMail(), To send email. sendTo : " + sendTo);
		validateSmtp();
		MimeMessagePreparator preparator = new MimeMessagePreparator() {
			public void prepare(MimeMessage mimeMessage) throws Exception {

				// R16-812 add cc list to all emails
				if (ccEmailList != null && !ccEmailList.isEmpty()) {
					InternetAddress[] recipientAddress = new InternetAddress[ccEmailList.size()];
					for (int i = 0; i <= ccEmailList.size() - 1; i++) {
						String ccId = ccEmailList.get(i);
						recipientAddress[i] = new InternetAddress(ccId);
					}
					mimeMessage.setRecipients(javax.mail.Message.RecipientType.CC, recipientAddress);
				}
				mimeMessage.setRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(sendTo, sendTo));

				// R16-812 end

				mimeMessage.setFrom(new InternetAddress(sendFrom, sendFrom));
				mimeMessage.setSubject(subject);
				InternetHeaders headers = new InternetHeaders();
				headers.addHeader("Content-type", "text/html; charset=UTF-8");

				MimeBodyPart body = new MimeBodyPart(headers, message.getBytes("UTF-8"));
				MimeMultipart multipart = new MimeMultipart();

				multipart.addBodyPart(body);
				mimeMessage.setContent(multipart);

			}
		};

		EmailService.this.mailSender.send(preparator);
		logger.warn("Email sent to: " + sendTo);
	}

	/**
	 * Method to send password recovery request
	 * 
	 * @param token
	 *            the token
	 * @param request
	 *            the request
	 * @param user
	 *            the user
	 */
	public void sendPasswordRecoveryRequest(String token, HttpServletRequest request, StaffMember user, String email) {
		logger.info("Inside EmailService::sendPasswordRecoveryRequest(), To send Password Recovery Request. "
				+ "user id = " + user.getId());
		final String url = propConfig.getWebAppBaseUrl() + "/reset-password?email=" + email + "&token=" + token;
		String sendFrom = propConfig.getSupportEmail();
		String sendTo = email;
		String subject = "Reset Password";

		Context context = new Context();
		context.setVariable("url", url);
		context.setVariable("from", sendFrom);
		context.setVariable("expiryTime", propConfig.getPasswordRecoveryExpirationHours());
		// String htmlContent =
		// templateEngine.process("password-recovery-email.html",
		// context);
		// sendMail(sendFrom, sendTo, subject, htmlContent);

		// String str = "Password-Reset notification mail" + "\n URL : " + url +
		// "\n From : " + sendFrom
		// + "\n expiryTime(in Hrs) : " +
		// propConfig.getPasswordRecoveryExpirationHours();
		//
		// sendMail(sendFrom, sendTo, subject, str);
		String htmlContent = templateEngine.process("password-recovery-email.html", context);

		sendMail(sendFrom, sendTo, subject, htmlContent);
		logger.info("Returning after sending Password Recovery Request.");
	}

	/**
	 * To send Mail
	 * 
	 * @param sendFrom
	 *            the sendFrom
	 * @param sendTo
	 *            the sendTo
	 * @param subject
	 *            the subject
	 * @param message
	 *            the message
	 * @throws IOException
	 */
	public void sendMail(final String sendFrom, final String sendTo, final String subject, final String message) {
		logger.info("Inside EmailService::sendMail(), To send mail. sendTo = " + sendTo);
		validateSmtp();
		MimeMessagePreparator messagePreparator = mimeMessage -> {
			MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
			messageHelper.setFrom(sendFrom);
			messageHelper.setTo(sendTo);
			messageHelper.setSubject(subject);
			messageHelper.setText(message, true);

			URL teamiumLogoURL = getClass().getResource("/static/img/Teamium-white.png");
			DataSource teamiumLogo = new FileDataSource(teamiumLogoURL.getFile());

			messageHelper.addInline("teamiumLogo", teamiumLogo);
			URL signInURL = getClass().getResource("/static/img/signin-icon-white.png");
			DataSource signInLogo = new FileDataSource(signInURL.getFile());

			messageHelper.addInline("signInLogo", signInLogo);

		};
		try {
			mailSender.send(messagePreparator);
		} catch (MailException e) {
			logger.error("Email does not sent to email address = " + sendTo, e);
			throw new UnprocessableEntityException("Email does not sent to email address = " + sendTo);
		} catch (Exception e) {
			logger.error("Email does not sent to email address = " + sendTo, e);
			throw new UnprocessableEntityException("Email does not sent to email address = " + sendTo);
		}
		logger.info("Returning after sending mail.");
	}

	// public String getDataSaurce(String message) {
	// String newMessage = "";
	// String fileExtension =
	// FilenameUtils.getExtension(propConfig.getFlashStaticResourseURL() +
	// "flashlogo.jpg");
	// String dataSource = "data:image/" + fileExtension + ";base64,";
	// try {
	// byte[] ar =
	// FileReader.getEncodedFileByteArray(propConfig.getFlashStaticResourseURL()
	// +
	// "flashlogo.jpg");
	// dataSource += new String(ar);
	// newMessage = message.replaceAll("#flashlogo", dataSource);
	// System.out.println(newMessage);
	// } catch (IOException e1) {
	// // TODO Auto-generated catch block
	// e1.printStackTrace();
	// }
	// return newMessage;
	// }

	/**
	 * To send Password Reset Notification Mail.
	 * 
	 * @param user
	 *            the user
	 * @param newPassword
	 *            the newPassword
	 */
	public void sendPasswordResetNotificationMail(StaffMember staffMember, String email, String newPassword) {
		logger.info(
				"Inside EmailService::sendPasswordResetNotificationMail(), To send Password Reset Notification Mail. "
						+ " user id = " + staffMember.getId());
		String sendFrom = propConfig.getSupportEmail();
		String sendTo = email;
		String subject = "Your Password for Teamium has been reset.";
		Context context = new Context();
		context.setVariable("from", sendFrom);
		context.setVariable("user", staffMember);
		context.setVariable("password", newPassword);
		// String htmlContent =
		// templateEngine.process("reset-password-by-admin.html",
		// context);
		// sendMail(sendFrom, sendTo, subject, htmlContent);
		String str = "Password-Reset notification mail" + "\n From : " + sendFrom + "\n User : "
				+ staffMember.getFirstName() + "\n password : " + newPassword;
		sendMail(sendFrom, sendTo, subject, str);
		logger.info("Returning after sending Password Reset Notification Mail.");
	}

	/**
	 * To Validate SMTP Credentials.
	 * 
	 */
	private void validateSmtp() {
		logger.info("Inside EmailService::validateSmtp(), To validate smtp.");
		if (StringUtils.isBlank(propConfig.getSmtpPort()) || StringUtils.isBlank(propConfig.getSmtpHost())
				|| StringUtils.isBlank(propConfig.getSmtpUserName())
				|| StringUtils.isBlank(propConfig.getSmtpPassword())) {
			logger.error("SMTP credential is missing.");
			throw new UnprocessableEntityException("Error while sending mail.");
		}
	}

}

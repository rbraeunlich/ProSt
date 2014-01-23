package de.blogspot.wrongtracks.prost.example.behaviour;

import java.net.URL;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.impl.bpmn.behavior.MailActivityBehavior;
import org.activiti.engine.impl.pvm.delegate.ActivityExecution;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

//Due to its impl-package I guess I shouldn't extend MailActivitiBehaviour
//well, who cares...
public class SendMailWithAttachmentBehaviour extends MailActivityBehavior{

	private static final long serialVersionUID = 3051437963239250128L;

	@Override
	public void execute(ActivityExecution execution) {
		URL uploadUrl = (URL) execution.getVariable("uploadUrl");

		String toStr = getStringFromField(to, execution);
		String fromStr = getStringFromField(from, execution);
		String ccStr = getStringFromField(cc, execution);
		String bccStr = getStringFromField(bcc, execution);
		String subjectStr = getStringFromField(subject, execution);
		String textStr = getStringFromField(text, execution);
		String htmlStr = getStringFromField(html, execution);
		String charSetStr = getStringFromField(charset, execution);

		// only sending HTML e-mails
		HtmlEmail email = createHtmlEmail(textStr, htmlStr);

		addTo(email, toStr);
		setFrom(email, fromStr);
		addCc(email, ccStr);
		addBcc(email, bccStr);
		setSubject(email, subjectStr);
		setMailServerProperties(email);
		setCharset(email, charSetStr);

		// attachment
		EmailAttachment attachment = new EmailAttachment();
		attachment.setURL(uploadUrl);
		attachment.setDisposition(EmailAttachment.ATTACHMENT);
		attachment.setDescription("Attachment");
		String dateiname = uploadUrl.toString().substring(
				uploadUrl.toString().lastIndexOf("/") + 1);
		attachment.setName(dateiname);
		//XXX I gotta change the CCL to get the test running
		ClassLoader ccl = Thread.currentThread().getContextClassLoader();
		try {
			email.attach(attachment);
			Thread.currentThread().setContextClassLoader(email.getClass().getClassLoader());
			email.send();
		} catch (EmailException e) {
			throw new ActivitiException("Could not send e-mail", e);
		}
		finally{
			Thread.currentThread().setContextClassLoader(ccl);
		}
		leave(execution);
	}

}

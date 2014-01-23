package de.blogspot.wrongtracks.prost.example.behaviour;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.ops4j.pax.exam.CoreOptions.bundle;
import static org.ops4j.pax.exam.CoreOptions.junitBundles;
import static org.ops4j.pax.exam.CoreOptions.mavenBundle;
import static org.ops4j.pax.exam.CoreOptions.options;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import javax.inject.Inject;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.OptionUtils;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerMethod;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.ServiceReference;
import org.subethamail.wiser.Wiser;

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerMethod.class)
public class SendMailWithAttachmentBehaviourTest {

	private static Wiser wiser;
	
	@Inject
	private BundleContext ctx;
	
	@BeforeClass
	public static void setUpBeforeClass(){
		wiser = new Wiser(8025);
		wiser.start();
	}
	
	@AfterClass
	public static void tearDownAfterClass(){
		if(wiser != null){
			wiser.stop();
		}
	}

	@Configuration
	public static Option[] configuration() {
		//@formatter:off
		Option[] core = options(mavenBundle().groupId("org.activiti").artifactId("activiti-engine").version("5.12.1"),
				mavenBundle().groupId("org.activiti").artifactId("activiti-bpmn-converter").version("5.12.1"),
				mavenBundle().groupId("org.activiti").artifactId("activiti-bpmn-model").version("5.12.1"),
				mavenBundle().groupId("org.activiti").artifactId("activiti-osgi").version("5.12.1"),
				mavenBundle().groupId("org.mybatis").artifactId("mybatis").version("3.1.1"),
				mavenBundle().groupId("joda-time").artifactId("joda-time").version("2.1"),
				mavenBundle().groupId("com.h2database").artifactId("h2").version("1.2.143"),
				mavenBundle().groupId("javax.transaction").artifactId("com.springsource.javax.transaction").version("1.1.0"),
				//only servicemix bundles evade that cce because of DataContentHandler
				mavenBundle().groupId("org.apache.servicemix.specs").artifactId("org.apache.servicemix.specs.activation-api-1.1").version("2.3.0"),
				mavenBundle().groupId("org.apache.servicemix.bundles").artifactId("org.apache.servicemix.bundles.javax.mail").version("1.4.1_5"),
				mavenBundle().groupId("commons-lang").artifactId("commons-lang").version("2.4"),
				mavenBundle().groupId("org.apache.commons").artifactId("commons-lang3").version("3.0"),
				mavenBundle().groupId("org.apache.commons").artifactId("commons-email").version("1.2"),
				mavenBundle().groupId("org.apache.felix").artifactId("org.apache.felix.fileinstall").version("3.2.6"),
				mavenBundle().groupId("org.apache.aries.blueprint").artifactId("org.apache.aries.blueprint.core").version("1.3.1-SNAPSHOT"),
				mavenBundle().groupId("org.apache.aries.proxy").artifactId("org.apache.aries.proxy").version("1.0.1"),
				mavenBundle().groupId("org.apache.aries").artifactId("org.apache.aries.util").version("1.1.0"),
				mavenBundle().groupId("org.slf4j").artifactId("slf4j-api").version("1.7.2"),
				mavenBundle().groupId("ch.qos.logback").artifactId("logback-classic").version("1.0.13"),
				mavenBundle().groupId("ch.qos.logback").artifactId("logback-core").version("1.0.13"),
				mavenBundle().groupId("de.blogspot.wrongtracks.prost").artifactId("activiti-engine-blueprint-wrapper").version("0.0.1-SNAPSHOT"),
				mavenBundle().groupId("de.blogspot.wrongtracks.prost").artifactId("prost-ejb-api").version("0.0.1-SNAPSHOT"),
				bundle("wrap:mvn:org.subethamail/subethasmtp/3.1.7"),
				bundle("reference:file:target/classes"));
		//@formatter:on
		return OptionUtils.combine(core, junitBundles());
	}

	@Test
	public void bundleStart() {
		Bundle bundle = null;
		for (Bundle b : ctx.getBundles()) {
			if (b.getSymbolicName().equals(
					"de.blogspot.wrongtracks.prost.prost-example-process")) {
				bundle = b;
			}
		}
		try {
			bundle.start();
			assertThat(bundle.getState(), is(equalTo(Bundle.ACTIVE)));
		} catch (BundleException e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void sendMail() throws MalformedURLException{
		ServiceReference ref = ctx.getServiceReference(ProcessEngine.class.getName());
		ProcessEngine processEngine = (ProcessEngine) ctx.getService(ref);
		ProcessInstance processInstance = processEngine.getRuntimeService().startProcessInstanceByKey("testProcessBP");
		processEngine.getRuntimeService().setVariable(processInstance.getProcessInstanceId(), "mail", "ronny.braeunlich@data-experts.de");
		processEngine.getRuntimeService().setVariable(processInstance.getProcessInstanceId(), "uploadUrl", new File(".").toURI().toURL());
		Task task = processEngine.getTaskService().createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
		processEngine.getTaskService().complete(task.getId());
		assertThat(wiser.getMessages().size(), is(1));
	}
}

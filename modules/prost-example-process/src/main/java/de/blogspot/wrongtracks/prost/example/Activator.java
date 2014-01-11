package de.blogspot.wrongtracks.prost.example;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.impl.ProcessEngineImpl;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import de.blogspot.wrongtracks.prost.example.form.UrlFormType;

public class Activator implements BundleActivator {

	@Override
	public void start(BundleContext context) throws Exception {
		ServiceReference reference = context.getServiceReference(ProcessEngine.class.getName());
		while(reference == null){
			Thread.sleep(200L);
			reference = context.getServiceReference(ProcessEngine.class.getName());
		}
		ProcessEngine processEngine = (ProcessEngine) context.getService(reference);
		((ProcessEngineImpl) processEngine).getProcessEngineConfiguration().getFormTypes().addFormType(new UrlFormType());
	}

	@Override
	public void stop(BundleContext context) throws Exception {
	}

}

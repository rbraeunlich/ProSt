package de.blogspot.wrongtracks.prost.blueprint;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.activiti.engine.impl.javax.el.ELContext;
import org.activiti.engine.impl.pvm.delegate.ActivityBehavior;
import org.activiti.osgi.blueprint.BlueprintELResolver;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

public class ProStBlueprintELResolver extends BlueprintELResolver {
	
	private static final String OSGI_SERVICE_BLUEPRINT_COMPNAME = "osgi.service.blueprint.compname";
	private static final Logger LOGGER = Logger
			.getLogger(ProStBlueprintELResolver.class.getName());
	private Map<String, ServiceReference> delegateMap = new HashMap<String, ServiceReference>();
	private BundleContext context;

	public Object getValue(ELContext context, Object base, Object property) {
		if (base == null) {
			// according to javadoc, can only be a String
			String key = (String) property;
			for (String name : delegateMap.keySet()) {
				if (name.equalsIgnoreCase(key)) {
					context.setPropertyResolved(true);
					return getContext().getService(delegateMap.get(name));
				}
			}
		}
		return super.getValue(context, base, property);
	}

	public void bindBehaviour(ServiceReference behavior) {
		String name = behavior.getProperty(OSGI_SERVICE_BLUEPRINT_COMPNAME).toString();
		delegateMap.put(name, behavior);
		LOGGER.info("Added Activiti behavior to behavior cache " + name);
	}

	public void unbindBehaviour(ServiceReference behavior) {
		if(behavior == null || behavior.getProperty(OSGI_SERVICE_BLUEPRINT_COMPNAME) == null){
			LOGGER.warning("Couldn't remove behavior because of null value: " + String.valueOf(behavior));
			return;
		}
		String name = behavior.getProperty(OSGI_SERVICE_BLUEPRINT_COMPNAME).toString();
		if (delegateMap.containsKey(name)) {
			delegateMap.remove(name);
		}
		LOGGER.info("removed Activiti behavior from behavior cache " + name);
	}

	public BundleContext getContext() {
		return context;
	}

	public void setContext(BundleContext context) {
		this.context = context;
	}

}

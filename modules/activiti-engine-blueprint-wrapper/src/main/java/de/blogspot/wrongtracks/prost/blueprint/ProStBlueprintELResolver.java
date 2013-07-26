package de.blogspot.wrongtracks.prost.blueprint;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.activiti.engine.impl.javax.el.ELContext;
import org.activiti.engine.impl.pvm.delegate.ActivityBehavior;
import org.activiti.osgi.blueprint.BlueprintELResolver;

public class ProStBlueprintELResolver extends BlueprintELResolver {
	private static final Logger LOGGER = Logger
			.getLogger(ProStBlueprintELResolver.class.getName());
	private Map<String, ActivityBehavior> delegateMap = new HashMap<String, ActivityBehavior>();

	public Object getValue(ELContext context, Object base, Object property) {
		if (base == null) {
			// according to javadoc, can only be a String
			String key = (String) property;
			for (String name : delegateMap.keySet()) {
				if (name.equalsIgnoreCase(key)) {
					context.setPropertyResolved(true);
					return delegateMap.get(name);
				}
			}
		}
		return super.getValue(context, base, property);
	}

	@SuppressWarnings("rawtypes")
	public void bindBehaviour(ActivityBehavior delegate, Map props) {
		String name = (String) props.get("osgi.service.blueprint.compname");
		delegateMap.put(name, delegate);
	}

	@SuppressWarnings("rawtypes")
	public void unbindBehaviour(ActivityBehavior delegate, Map props) {
		String name = (String) props.get("osgi.service.blueprint.compname");
		if (delegateMap.containsKey(name)) {
			delegateMap.remove(name);
		}
		LOGGER.info("removed Activiti service from delegate cache " + name);
	}

}

package es.imim.bg.ztools.ui.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

public class AbstractModel {

	protected ArrayList<PropertyChangeListener> listeners =
		new ArrayList<PropertyChangeListener>(0);
	
	public AbstractModel() {
	}
	
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		listeners.add(listener);
	}
	
	protected void firePropertyChange(
			String propName, Object oldValue, Object newValue) {
		
		for (PropertyChangeListener l : listeners) {
			PropertyChangeEvent evt = 
				new PropertyChangeEvent(this, propName, oldValue, newValue);
			l.propertyChange(evt);
		}
	}
}

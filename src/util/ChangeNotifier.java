package util;

import java.util.ArrayList;

import core.Logger;

public class ChangeNotifier {

	private Logger logger;
	private Object parent;
	private ArrayList<IChangeListener> listeners;
	
	public ChangeNotifier(Logger logger, Object parent){
		this.logger = logger;
		this.parent = parent;
		listeners = new ArrayList<IChangeListener>();
	}
	
	public void addListener(IChangeListener listener) {
		if(!listeners.contains(listener)){
			listeners.add(listener);
		}
	}
	
	public void removeListener(IChangeListener listener) {
		if(listeners.contains(listener)){
			listeners.remove(listener);
		}
	}
	
	public void notifyListeners(){
		for(IChangeListener listener : listeners){
			try{
				listener.changed(parent);
			}catch(Exception e){
				logger.error(e);
			}
		}
	}
}

package util.notify;

import java.util.ArrayList;

import util.logger.ILogger;

public class ChangeNotifier implements IChangeNotifier {

	private ILogger logger;
	private Object parent;
	private ArrayList<IChangeListener> listeners;
	
	public ChangeNotifier(ILogger logger, Object parent){
		this.logger = logger;
		this.parent = parent;
		listeners = new ArrayList<IChangeListener>();
	}
	
	@Override
	public void addListener(IChangeListener listener) {
		if(!listeners.contains(listener)){
			listeners.add(listener);
		}
	}
	
	@Override
	public void removeListener(IChangeListener listener) {
		if(listeners.contains(listener)){
			listeners.remove(listener);
		}
	}
	
	@Override
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

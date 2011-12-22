package tray;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import tray.IMessageDisplay.MessageType;
import util.logger.ILogger;

public abstract class AbstractTrayAction implements ActionListener {

	private ILogger logger;
	private IMessageDisplay tray;
	
	public AbstractTrayAction(ILogger logger, IMessageDisplay tray){
		this.logger = logger;
		this.tray = tray;
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		if(event.getActionCommand() != null){
			logger.info("=> tray-action \""+event.getActionCommand()+"\"");
		}else{
			logger.info("=> tray-action");
		}
    	try{
    		action(event);
		}catch (Exception e){
			logger.error(e);
			tray.displayMessage(e.getMessage(), MessageType.ERROR);
		}
	}
	
	abstract protected void action(ActionEvent event) throws Exception;
}

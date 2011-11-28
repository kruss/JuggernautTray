package tray;

import java.awt.Image;
import java.awt.Menu;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import monitor.IBuildMonitor;
import monitor.IBuild.BuildStatus;
import monitor.IBuildMonitor.BuildInfo;

import core.JuggernautTray;

import util.IComponent;
import util.SystemTools;
import util.UiTools;
import util.logger.ILogger;
import util.notify.IChangeListener;

public class TrayManager implements ITrayManager, IChangeListener {

	private enum IconType {
		SAD, ANGRY, HAPPY
	}
	
	private ILogger logger;
	private IComponent parent;
	private IBuildMonitor monitor;
	private TrayIcon icon;
	
	public TrayManager(ILogger logger, IComponent parent, IBuildMonitor monitor) throws Exception {
		this.logger = logger;
		this.parent = parent;
		this.monitor = monitor;
	}

	@Override
	public void init() throws Exception {
		if(SystemTray.isSupported()){
			updateUI();
			monitor.getChangeNotifier().addListener(this);
		}else{
			throw new Exception("System-Tray not supported");
		}
	}
	
	@Override
	public void shutdown() throws Exception {
		monitor.getChangeNotifier().removeListener(this);
	}
	
	@Override
	public void displayMessage(String message, MessageType type) {
		if(icon != null){
			icon.displayMessage(JuggernautTray.APP_NAME, message, type);
		}
	}

	@Override
	public void changed(Object object) {
		if(object instanceof IBuildMonitor){
			try{
				updateUI();
			}catch(Exception e){
				logger.error(e);
				displayMessage(e.getMessage(), MessageType.ERROR);
			}
		}
	}

	private void updateUI() throws Exception {
		setIcon(getIconType4BuildStatus(monitor.getStatus()));
		setMenu(monitor.getBuildInfo());
	}
	
	private IconType getIconType4BuildStatus(BuildStatus status) {
		if(status == BuildStatus.OK){
			return IconType.HAPPY;
		}else if(status == BuildStatus.ERROR){
			return IconType.ANGRY;
		}else{
			return IconType.SAD;
		}
	}
	
	private void setIcon(IconType type) throws Exception {
	     if(icon != null){
	    	 icon.setImage(getIconImage(type));
	     }else{
	    	 createIcon(getIconImage(type));
	     }
	}

	private Image getIconImage(IconType type) throws Exception {
		File file = new File("icon"+File.separator+type.toString().toLowerCase()+".gif");
		if(file.exists()){
			return Toolkit.getDefaultToolkit().getImage(file.getAbsolutePath());
		}else{
			throw new Exception("Not a file: "+file.getAbsolutePath());
		}
	}
	
	private void createIcon(Image image) throws Exception {
        icon = new TrayIcon(image, JuggernautTray.APP_NAME);
		icon.setImageAutoSize(true);
		icon.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { // win & linux fired on double-click only
            	if(SystemTools.isWindowsOS() && SystemTools.isLinuxOS()){
	            	logger.log("update...");
	        		try{
						monitor.updateStatus();
					}catch (Exception ex){
						logger.error(ex);
						displayMessage(ex.getMessage(), MessageType.ERROR);
					}
            	}
            }
		});
		SystemTray tray = SystemTray.getSystemTray();
		tray.add(icon);
	}

	private void setMenu(ArrayList<BuildInfo> builds) {
		PopupMenu popupMenu = new PopupMenu();
        
        for(BuildInfo build : builds){
        	final String indentifier = build.identifier;
        	final String url = build.url;
            MenuItem buildMenu = new MenuItem(indentifier+" ("+build.status.toString()+")");
            buildMenu.addActionListener(new TrayAction(logger, this){
    			@Override
    			protected void action(ActionEvent event) throws Exception {
    				logger.log("open browser: "+url);
    				SystemTools.openBrowser(url);
    			}
            });
            popupMenu.add(buildMenu);
        }
        if(builds.size() > 0){
        	popupMenu.addSeparator();
        }
        
		Menu setupMenu = new Menu("Setup");
		popupMenu.add(setupMenu);
		
        MenuItem addMenuItem = new MenuItem("Add build");
        addMenuItem.addActionListener(new TrayAction(logger, this){
			@Override
			protected void action(ActionEvent event) throws Exception {
				String identifier = UiTools.inputDialog("Add build (name@server)");
				if(identifier != null && !identifier.isEmpty()){
					monitor.addBuild(identifier);
					monitor.updateStatus();
				}
			}
        });
        setupMenu.add(addMenuItem);
        
		if(builds.size() > 0){
        	MenuItem removeMenuItem = new MenuItem("Remove build");	  
        	removeMenuItem.addActionListener(new TrayAction(logger, this){
    			@Override
    			protected void action(ActionEvent event) throws Exception {
    				String identifier = UiTools.inputDialog("Remove build (name@server)");
    				if(identifier != null && !identifier.isEmpty()){
    					monitor.removeBuild(identifier);
    				}
    			}
            });
        	setupMenu.add(removeMenuItem);
        	setupMenu.addSeparator();
        	
        	MenuItem updateMenuItem = new MenuItem("Update status");	
        	updateMenuItem.addActionListener(new TrayAction(logger, this){
    			@Override
    			protected void action(ActionEvent event) throws Exception {
    				monitor.updateStatus();
    			}
            });
        	setupMenu.add(updateMenuItem);
		}
        
        MenuItem quitMenuItem = new MenuItem("Quit");
        quitMenuItem.addActionListener(new TrayAction(logger, this){
			@Override
			protected void action(ActionEvent event) throws Exception {
				parent.shutdown();
			}
        });
        popupMenu.add(quitMenuItem);
        
        icon.setPopupMenu(popupMenu);
	}
}

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
import java.io.File;
import java.util.ArrayList;

import monitor.IBuildMonitor;
import monitor.MonitorData;
import monitor.IBuild.BuildStatus;
import monitor.MonitorData.BuildInfo;

import core.Juggertray;

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
			icon.displayMessage(Juggertray.APP_NAME, message, type);
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
		MonitorData data = monitor.getMonitorData();
		setIcon(getIconType4BuildStatus(data.getStatus()));
		setToolTip(data.getBuilds().size(), data.getBuilds(BuildStatus.OK).size());
		setMenu(data.getBuilds());
	}
	
	private void setIcon(IconType type) throws Exception {
		if(icon != null){
			icon.setImage(getIconImage(type));
		}else{
			createIcon(getIconImage(type));
		}
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

	private Image getIconImage(IconType type) throws Exception {
		File file = new File("icon"+File.separator+type.toString().toLowerCase()+".gif");
		if(file.exists()){
			return Toolkit.getDefaultToolkit().getImage(file.getAbsolutePath());
		}else{
			throw new Exception("Not a file: "+file.getAbsolutePath());
		}
	}
	
	private void createIcon(Image image) throws Exception {
        icon = new TrayIcon(image);
		icon.setImageAutoSize(true);
		icon.addActionListener(new TrayAction(logger, this){
			@Override
			protected void action(ActionEvent event) throws Exception {
				if(SystemTools.isWindowsOS() || SystemTools.isLinuxOS()){
					monitor.updateMonitor();
				}
			}
		});
		SystemTray tray = SystemTray.getSystemTray();
		tray.add(icon);
	}

	private void setToolTip(int totalBuilds, int okBuilds) {
		if(totalBuilds > 0){
			icon.setToolTip(Juggertray.APP_NAME+" ("+okBuilds+" / "+totalBuilds+" OK)");
		}else{
			icon.setToolTip(Juggertray.APP_NAME+" ("+Juggertray.APP_VERSION+")");
		}
	}
	
	private void setMenu(ArrayList<BuildInfo> builds) {
		PopupMenu popupMenu = new PopupMenu();
        
        for(BuildInfo build : builds){
            MenuItem buildMenu = new MenuItem(getMenuLabel4BuildInfo(build));
            final String url = build.url;
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
		
        MenuItem addMenuItem = new MenuItem("Add");
        addMenuItem.addActionListener(new TrayAction(logger, this){
			@Override
			protected void action(ActionEvent event) throws Exception {
				String identifier = UiTools.inputDialog("Add build (name@server)");
				if(identifier != null && !identifier.isEmpty()){
					monitor.addBuild(identifier);
					monitor.updateMonitor();
				}
			}
        });
        setupMenu.add(addMenuItem);
        
		if(builds.size() > 0){
        	MenuItem removeMenuItem = new MenuItem("Remove");	 
        	removeMenuItem.addActionListener(new TrayAction(logger, this){
    			@Override
    			protected void action(ActionEvent event) throws Exception {
    				String identifier = UiTools.optionDialog("Remove build", monitor.getMonitorData().getIdentifiers());
    				if(identifier != null && !identifier.isEmpty() && UiTools.confirmDialog("Remove build ["+identifier+"] ?")){
    					monitor.removeBuild(identifier);
    				}
    			}
            });
        	setupMenu.add(removeMenuItem);
        	
        	MenuItem editMenuItem = new MenuItem("Edit");	 
        	editMenuItem.addActionListener(new TrayAction(logger, this){
    			@Override
    			protected void action(ActionEvent event) throws Exception {
    				String identifier1 = UiTools.optionDialog("Edit build", monitor.getMonitorData().getIdentifiers());
    				if(identifier1 != null && !identifier1.isEmpty()){
    					String identifier2 = UiTools.inputDialog("Edit build (name@server)", identifier1);
    					if(identifier2 != null && !identifier2.isEmpty()){
        					monitor.removeBuild(identifier1);
        					monitor.addBuild(identifier2);
    						monitor.updateMonitor();
    					}
    				}
    			}
            });
        	setupMenu.add(editMenuItem);
        	
        	if(SystemTools.isMacOS()){
	        	setupMenu.addSeparator();
	        	MenuItem updateMenuItem = new MenuItem("Update");	
	        	updateMenuItem.addActionListener(new TrayAction(logger, this){
	    			@Override
	    			protected void action(ActionEvent event) throws Exception {
	    				monitor.updateMonitor();
	    			}
	            });
	        	setupMenu.add(updateMenuItem);
        	}
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
	
	private String getMenuLabel4BuildInfo(BuildInfo build) {
		if(build.status == BuildStatus.OK){
			return build.identifier+" ("+build.status.toString()+")";
		}else if(build.status == BuildStatus.ERROR){
			return "~ "+build.identifier+" ("+build.status.toString()+")";
		}else{
			return "~ "+build.identifier+" ("+build.status.toString()+")";
		}
	}
}

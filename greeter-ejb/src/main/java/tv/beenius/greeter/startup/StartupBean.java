package tv.beenius.greeter.startup;


import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;

import org.jboss.logging.Logger;

import tv.beesmart.msdp.core.ejb.configuration.ConfigRepository;
import app.deployment.DeploymentService;
import app.deployment.DeploymentUtils;
import app.deployment.admin.SdkAdminMenuItem;

@Singleton
@Startup
public class StartupBean {

	private static final Logger logger = Logger.getLogger(StartupBean.class
			.getName());

	@EJB(mappedName = "java:global/msdp/msdp-core-ejb/DeploymentBean")
	private DeploymentService deploymentService;

	@EJB(mappedName = "java:global/msdp/msdp-core-ejb/Configuration")
	private ConfigRepository configRepo;

	private static String applicationUid;

	@PostConstruct
	private void init() {
		String audit = "[greeter.PostConstruct]";
		logger.info(audit);

		try {
			String jspPath = "/greeter";
			// admin values in main menu
			SdkAdminMenuItem rootItem = new SdkAdminMenuItem("SDK Demo", null);

			// subitems
			ArrayList<SdkAdminMenuItem> rootSubitems = new ArrayList<SdkAdminMenuItem>(); // subitems
			SdkAdminMenuItem s_addLocation = new SdkAdminMenuItem("Demo",
					jspPath + "/admin/HelloWorldServlet");
			rootSubitems.add(s_addLocation);
			rootItem.setSubitems(rootSubitems);

			DeploymentUtils utils = new DeploymentUtils(this.getClass());
			String manifestData = utils
					.getExternalXmlData("application-manifest.xml");
			String settingsData = utils
					.getExternalXmlData("application-settings.xml");
			byte[] iconData = utils.getExternalBinaryData("sdkdemo-icon.png");
			byte[] imageData = utils.getExternalBinaryData("sdkdemo.jpg");
			applicationUid = deploymentService.registerApplication(
					manifestData, settingsData, iconData, imageData, rootItem);
			logger.info(audit + " app registered under " + applicationUid);

		} catch (Exception e) {
			logger.error(audit + e.getMessage());
		}
	}

	@PreDestroy
	private void destroy() {
		String audit = "[greeter.preDestroy]";
		logger.info(audit + " called");

		deploymentService.unregisterApplication(applicationUid);

		if (configRepo == null) {
			logger.error(audit + " unable to locate Configuration");
		} else {
			// configRepo.unregister(sdkPlugin.getModuleName());
		}
	}

	// public void addConfigurationToSystemConfiguration()
	// {
	// String audit = "[world-time.addConfigurationToSystemConfiguration]";
	//
	// if (configRepo == null)
	// {
	// logger.error(audit + " unable to locate Configuration");
	// }
	//
	// // configRepo.register(app.getModuleName(), "<settings>" + "<sdk>" +
	// "<facebook>" + "<oauth>"
	// // +
	// "<MY_API_KEY value='' description='Api key of your application' regional='true' forClient='false'/>"
	// // +
	// "<MY_REDIRECT_URL value='' description='Redirect url from your application' regional='true' forClient='false'/>"
	// // +
	// "<YOUR_APP_SECRET value='' description='Application secret of your application' regional='true' forClient='false'/>"
	// // +
	// "<FACEBOOK_AUTH_MODE value='SERVER_AUTH' description='Authorization mode (SERVER_AUTH  or USER_AUTH)' regional='true' forClient='false'/>"
	// // + "</oauth>" + "</facebook>" + "</sdk>" + "</settings>");
	// }

}

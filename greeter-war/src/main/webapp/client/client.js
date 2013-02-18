/*
 * Wrap may module into it's own local context. Prevents leaking of local variables
 * global namespace aka window object. BS is denoted as modules inport.
 */
(function(BS){

/* Some common frameworks object dereferenced for cleaner code. 
 * WARNING! omitting var syntax will pollute global namespace. In worst case we
 * can overwrite some crucial variables like "BeeSmart". Be sure your variables are 
 * contained inside this local scope.
 */
var keys = BS.misc.keyGroups,
	ModuleDom = BS.misc.ModuleDom,
	mm = BS.modMgr,
	am = BS.appMgr,
	ui = BS.ui,
	misc = mm.getApp('Misc')
;

// Construction of an BSApplication, that is application in context of BS client framework.
var demoScreen = new BS.App('sdkdemo', {
	/* Mark demo screen as default BSApplication inside BSModule. That means
	 * each time some part of BS client framework wants to present your app eg. 
	 * on a screen to end user this bsApp will be pushed in front of all others.
	 * */
	isDefault: true
});
// Handler that catches input events - mainly key presses, when BSApp is in focus
demoScreen.onInputEvent = function(keyPressedName){
	switch(keyPressedName){
	case 'ok':
		demoScreen.utils.getService(this);
		break;
	case 'left':
	case 'back':
		am.pop();
		break;
	case 'up':
	case 'down':				
		break;	
	case 'green':
		break;			
	}
};
demoScreen.init = function(callerAppId, promoId){
	var that = this;
	
	// First time app is launched
	if (this.dom == null){
		// We do SVG template lazy loading
		that.utils.loadSvgTemplate(function(){
			that.utils.templateInit(that);
			that.utils.prepareGui(that);
		});
	} else // template is there, so just prepare gui to rest states
		that.utils.prepareGui(that);
};
demoScreen.dom = null;
demoScreen.utils = {
	loadSvgTemplate: function(onDone){
		// To report when we're done here. 
		onDone = onDone || function(){};
		
		/* Request svg template file. You should be able to access file from web 
		 * browser also. 
		 */
		BS.request.Get('/%s/client/ui.svg'.sprintf("greeter")/*web context root must mutc the one defined in application.xml*/)
			.complete(function(response){
				/* Response can fail due to server failure or connection problem 
				 * on bad internet pipes. Be sure to handle that properly. In following
				 * case nothing will happen
				 */
				if (response.success == false) {
					BS.logger.info("ERROR: %s reports SVG template loading error."
						+" App won't work properly".sprintf(bsModule.id));
					// remove my self from users focus
					// TODO: nice error message informing user something went worong
					am.pop();
					return;
				}
				// Parses/appends xml and appends it to the document tree,
				BS.misc.parseAndAppendModuleSVG(response.data);
				// success callback
				onDone(true); 
			});
	},
	templateInit: function(that){
		that.dom = {
			"scrHolder": $("#sdkdemo"), 
			"description": $("#sdkdemo-description"),
			"feedback": $("#sdkdemo-feedback")
		};
		that.utils.inFeedback = new ui.InplaceFeedback(
			that.dom.feedback, {autoHide: 2012});
	},
	prepareGui: function(that){
		that.dom.scrHolder.show();
		
		// Change common action bar
		misc.dom.actionBar.set([
			['back', 'Return'], 
			['empty', null], 
			['empty', null], 
			['confirm', 'Ask server']
		]);
		
	},
	getService: function(that, onDone){
		var onDone = onDone || function(){};
		
		BS.request.Get(
			"/stb/legacy-gateway/%s.services.HelloWorldService/getHelloWorld".sprintf("tv.beenius.greeter")
		).complete(function(response){
			// Development logging, must not be used in production environments, 
			// use BS.logger.debug insted
			l(response)
			
			if (response.success == false) {
				// Nice error message informing user something went wrong
				that.utils.inFeedback.show("errSmall", "Something got wrong here!");
				return;
			}
			
			/* InplaceFeedback ... ui.js. 1. argument: see <defs> content in 
			 * /skin/{skin-name}/svg/ui.svg for icon references like "errSmall"
			 */
			that.utils.inFeedback.show("okSmall", "Great success of the call!");
			
			that.dom.description.text(response.data.result.msg);
			onDone(true);
		});
	},
	cleanup: function(){
		// Remove SVG template from document tree.
		that.dom.scrHolder.remove();
		// Break svg references. GC will empty memory for you.
		that.dom = null;
	}
};
demoScreen.destroy = function(){
	this.dom.scrHolder.hide();
};
demoScreen.focus = function(callingModule){};	
demoScreen.blur = function(callingModule){};


/* Construction of an BSModule in context of BS client framework. Can be understood
 * as an analogy to BeeStore application
 * */ 
var bsModule = new BS.Module(
	{
		id: 'greeter',/*application guid must match one defined in application-manifest.xml*/
		/* NOTE: when writing feature that will be distributed as BeeStore Application,
		 * launcher method must be provided!
		 * Launcher method is invoked by BeeStore "Launch" command. Application
		 * needs handle its every appearance triggered via various integration points. 
		 * */
		launcher: function(){
			/* Don't care in which client state were we called in, just add my 
			 * default bsApp in the client's application stack.
			 * */
			am.push(demoScreen);
		},
		/* When BeeStore app is no longer desired to be active on client, certain
		 * cleanup tasks must be done.
		 * */
		uninstall: function(){
			demoScreen.utils.cleanup();
		}
	}
);

/* BSApplication are registered into BSModule. Unregistered BSApplication are unable
 * to function inside BS client framework
 */
bsModule.register(demoScreen);


})(BeeSmart); // See comment in the beginning of file.
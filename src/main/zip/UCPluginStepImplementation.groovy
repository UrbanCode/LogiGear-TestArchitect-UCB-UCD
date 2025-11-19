/**
 * (c) Copyright IBM Corporation 2017.
 * This is licensed under the following license.
 * The Eclipse Public 1.0 License (http://www.eclipse.org/legal/epl-v10.html)
 * U.S. Government Users Restricted Rights:  Use, duplication or disclosure restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.codehaus.groovy.runtime.StackTraceUtils

import com.ibm.issr.core.log.LogTracingClass
import com.ibm.issr.core.log.Logger
import com.ibm.issr.core.log.LoggerLevel
import com.ibm.issr.core.plugin.AbortPluginException;

/**
 * <p>This is a re-usable base class for UrbanCode Plugins implemented in Groovy.  This
 * class has standard setup and teardown code for the plugin step along with helper
 * functions.</p>
 * <p>The concrete class implementation MUST implement the program's main() function, pretty
 * much as follows.</p>
 * <pre>
 * {@code
 *  public class ConcreteExample extends UCDPluginStepImplementation {
 *
 *  ...
 *
 * 	public static void main( java.lang.String[] args ) {
 * 		def stepImpl = new ConcreteExample();
 * 		stepImpl.performStep(args) { implementation_of_the_step  }
 * 	}
 * </pre>
 * <p>Some of the features implemented by this base Step class are...</p>
 * <ul>
 * <li>On startup, it automatically loads the inbound properties into 'inProps'</li>
 * <li>On startup, it automatically loads the Agent's properties into the variable 'agentProps'</li>
 * <li>On ending, it automatically puts any properties in 'outProps' to the output property file</li>
 * <li>On startup, if there is a property named 'loggingLevel', this automatically sets the logging
 * level to the designated property value (trace, debug, info, etc)</li>
 * </ul>
 */
class UCPluginStepImplementation extends LogTracingClass {
	/**
	 * The current working directory.
	 */
	File workDir;
	/**
	 * A 'Properties' map of the properties passed from UrbanCode server to the plugin.
	 * For example, call 'def propName = inProps.propName' to access the value of the "propName" property.
	 */
	Properties inProps;
	/**
	 * A 'Properties' map of the properties sent from the plugin to the UrbanCode server. You can set
	 * property values by calling 'outProps.put( "propName", propValue )'.
	 * When the step ends, this base class automatically sends the properties back to UrbanCode Deploy.
	 */
	Properties outProps;
	/**
	 * Read only access the current Agent's installed properties, which are in the file 'conf/agent/installed.properties' as
	 * a Properties map.
	 */
	Properties agentProps;

	/**
	 * The "home" (installation) directory of the UrbanCode agent.
	 */
	File AGENT_HOME = new File(System.getenv().get("AGENT_HOME"))

	/**
	 * <p>Call this function to perform the step!!  Specifically, this function
	 * does initialization, then it calls 'theStep' (which is implementation of the step), and
	 * then it does post-step processing.  For example, in pre-step initialization, it reads
	 * the inbound properties and in post-step processing, it outputs outbound properties.</p>
	 * <p>This function may be extended in inheritence classes.</p>
	 * @param args <p>The command line arguments to the step.</p>
	 * <p>The parameters are</p>
	 * <ul>
	 * <li>args[0] - Name of the inbound property file</li>
	 * <li>args[1] - Name of the outbound property file</li>
	 * </ul>
	 * @param theStep This closure actually performs the step.
	 */
	void performStep( java.lang.String[] args, Closure theStep ) {
		try {
			workDir = new File('.').canonicalFile

			// load the inbound properties
			inProps = new Properties();
			def inputPropsFile = new File(args[0]);
			def inputPropsStream = new FileInputStream(inputPropsFile);
			inProps.load(inputPropsStream);

			// initialize the outbound properties (to an empty set)
			outProps = new Properties();

			// If there is a property named "loggingLevel", set the logging level.
			// It should be blank, 'trace', 'debug', 'info', 'warn' or 'error'
			if (inProps.containsKey("loggingLevel")) {
				try {
					Logger.setLoggingLevel(inProps.loggingLevel)
				} catch (Exception e) {
					Logger.error("Invalid trace level of '${loggingLevel}' sent to the plugin via the 'loggingLevel' property")
					System.exit(1)
				}
			}

			// call the implementation of the step!!
			theStep();

			// If there are any outbound properties save them to the property file
			if (outProps.size() > 0) {
				def outputPropsFile = new File(args[1]);
				def outputPropsStream = new FileOutputStream(outputPropsFile);
				outProps.store(outputPropsStream, "Output props");
			}
		} catch (AbortPluginException e) {
			// if it is an 'AbortPluginException', display the error message, but no stack trace unless 'debug' level
			Logger.println(LoggerLevel.ERROR, e.message )
			// sanitize the groovy information
			StackTraceUtils.deepSanitize(e)
			Logger.printStackTrace(LoggerLevel.DEBUG, e)
			System.exit(1)
		} catch (Exception e) {
			Logger.println(LoggerLevel.ERROR, e.message )
			// sanitize the groovy information
			StackTraceUtils.deepSanitize(e)
			Logger.printStackTrace(LoggerLevel.INFO, e)
			System.exit(1)
		}
	}

	/**
	 * Display list of plugin parameters - this can and should be extended by classes that extend this one.
	 */
	protected void displayParameters() {
		Logger.info "   loggingLevel='${Logger.getLoggingLevel()}'"
	}

	/**
	 * Helper function that returns the server URL for the UrbanCode server that launched this plugin step, such as "https://server:1234".
	 */
	String getMyRestServerUrl() {
		return System.getenv("AH_WEB_URL");
	}

	/**
	 * Helper function that returns the PROXY_HOST system environment.  IF the call is through a relay
	 * server, this field will be defined and should be used as an HTTP Relay Server.
	 * @return Empty string if no Proxy Host or non-empty if there is a proxy host.
	 */
	String getMyRestServerProxyHost() {
		return System.getenv("PROXY_HOST")
	}

	/**
	 * Helper function that returns the PROXY_PORT system environment.  IF the call is through a relay
	 * server, this field will be defined and should be used as an HTTP Relay Server.
	 * @return Empty string if no Proxy Port or non-empty if there is a proxy Port.
	 */
	String getMyRestServerProxyPort() {
		return System.getenv("PROXY_PORT")
	}

	/**
	 * Helper function that returns the username to use for automatic Token based authentication with the
	 * UrbanCode server that launched this plugin.  When an UrbanCode server launches a plugin, it make the server url,
	 * and a token based authentication username and password available.  Note that the tokens are session specific and only
	 * good for the lifetime instance of this plugin step.
	 */
	String getMyRestServerTokenUsername() {
		return "PasswordIsAuthToken";
	}

	/**
	 * Helper function that returns the username to use for automatic Token based authentication with the
	 * UrbanCode server that launched this plugin.  When an UrbanCode server launches a plugin, it make the server url,
	 * and a token based authentication username and password available.  Note that the tokens are session specific and only
	 * good for the lifetime instance of this plugin step.
	 */
	String getMyRestServerTokenPassword() {
		String authToken = System.getenv("AUTH_TOKEN");
		return "{\"token\" : \"" + authToken + "\"}";
	}

	/**
	 * Returns the current Agent's installed properties, which are in the file 'conf/agent/installed.properties' as
	 * a Properties map.  Throws exception if there is an error.
	 */
	Properties getAgentProps() {
		if (this.agentProps == null) {
			this.agentProps = new Properties();
			def agentInstalledProps = new File(AGENT_HOME, "conf/agent/installed.properties")
			def agentInputStream = null;
			agentInputStream = new FileInputStream(agentInstalledProps);
			agentProps.load(agentInputStream);
		}
		return this.agentProps;
	}

	/**
	 * Declaring this private function makes inProps a read-only field.
	 * @param properties
	 */
	private void setInProps( Properties properties ) {
		this.inProps = properties;
	}

	/**
	 * Declaring this private function makes outProps a read-only field.
	 * @param properties
	 */
	private void setOutProps( Properties properties ) {
		this.outProps = properties;
	}

	/**
	 * Declaring this private function makes agentProps a read-only field.
	 * @param properties
	 */
	private void setAgentProps( Properties properties ) {
		this.agentProps = properties;
	}

	/**
	 * Exits the plugin with an error message.
	 */
	protected void exitWithErrorMessage( String msg ) {
		throw new RuntimeException(msg)
	}

	/**
	 * Sets the named Step output property to the given value.
	 */
	public void setOutputProperty( String propertyName, String propertyValue ) {
		outProps.put(propertyName, propertyValue)
	}
}

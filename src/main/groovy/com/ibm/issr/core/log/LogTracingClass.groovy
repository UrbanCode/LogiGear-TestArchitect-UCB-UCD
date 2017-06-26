/**
 * (c) Copyright IBM Corporation 2017.
 * This is licensed under the following license.
 * The Eclipse Public 1.0 License (http://www.eclipse.org/legal/epl-v10.html)
 * U.S. Government Users Restricted Rights:  Use, duplication or disclosure restricted by GSA ADP Schedule Contract with IBM Corp.
 */

package com.ibm.issr.core.log;


/**
 * Extend any other class from this one to automatically enable function call trace logging
 * for the extended class.  In that event it logs a trace call for every function entry and
 * exit point.
 * @author ltclark
 *
 */
public class LogTracingClass extends GroovyObjectSupport implements GroovyInterceptable {
	// This class does a log trace on functions in derived classes.
	// BUT building the trace output can involve making calls to other
	// functions that are also traced - ending up in an infinite recursive loop of
	// traced function calls trace display which calls another traced function which
	// calls trace display - and so on.
	// To break this look, 'skipTrace' is set to true when processing a processing
	// the trace messaging.  When 'skipTrace' is true, simply call the function without
	// trace information.
	private static boolean skipTrace = false;


	/**
	 * This function is overridden to generate trace logging information for all setter
	 * calls to the class.  This works because in Groovy, EVERY setter call goes through
	 * this setProperty method.  Note that if you call object.property=value or
	 * object.setProperty(value), setProperty is called!!
	 */
	public void setProperty(String name, value) {

		if (Logger.displayTrace) {
			// Note that the setProperty sets skipTrace but does NOT actually skipTrace itself
			//   ... because we want to trace all setter calls!! (even if during a skipTrace)
			def oldSkipTrace = skipTrace
			skipTrace = true;
			try {
				String setterDisplay = "Set: [${this}].${name} to "
				if (value instanceof String) {
					setterDisplay = setterDisplay + "'${value}'"
				} else {
					setterDisplay = setterDisplay + "${value}"
				}
				Logger.trace(setterDisplay)
			}
			finally {
				skipTrace = oldSkipTrace
			}
		}

		super.setProperty(name,value)
	}


	/**
	 * This function is overridden to generate trace logging information for all getter
	 * calls to the class.  This works because in Groovy, EVERY getter call goes through
	 * this getProperty method.  Note that if you call object.property or object.getProperty(),
	 * this getProperty function is called.
	 */
	public def getProperty(String name) {

		def result = super.getProperty(name)

		if (Logger.displayTrace && (! skipTrace)) {
			def oldSkipTrace = skipTrace
			skipTrace = true;
			try {
				String resultDisplay = "Get: [${this}].${name} returns "
				if (result instanceof String) {
					resultDisplay = resultDisplay + "'${result}'"
				} else if (result) {
					if (result == this) {
						resultDisplay = resultDisplay + "**this**"
					} else {
						resultDisplay = resultDisplay + "${result}"
					}
				}
				Logger.trace(resultDisplay)
			}
			finally {
				skipTrace = oldSkipTrace
			}
		}

		return result
	}

	/**
	 * This function is overridden
	 * to generate trace logging information about the functions that are called.
	 * This works because, in groovy, every class method call (except for get/set) goes through invokeMethod.
	 */
	public def invokeMethod(String name, args) {
		String functionName = ""

		if (Logger.displayTrace && (! skipTrace)) {
			def oldSkipTrace = skipTrace
			skipTrace = true;
			try {
				// determine if trace logging is disabled for this function
				def metaMethod = LogTracingClass.metaClass.getMetaMethod(name,args);

				// log the call
				functionName = "[${this}].$name("
				String delim = ""
				if (args) {
					args.each() { arg ->
						def argDisplay
						if (arg instanceof String) {
							argDisplay = "'${arg}'"
						} else {
							argDisplay = "${arg}";
						}
						functionName = functionName + "${delim} ${argDisplay}"
						delim = ","
					}
				}
				functionName = functionName + " )"
				Logger.trace("Entering: ${functionName}")
				Logger.setTraceTabDepth(Logger.getTabDepth()+1)
			}
			finally {
				skipTrace = oldSkipTrace;
			}
		}

		def result = super.invokeMethod(name, args)

		if (Logger.displayTrace && (! skipTrace)) {
			def oldSkipTrace = skipTrace
			skipTrace = true;
			try {
				def resultDisplay = ""
				if (result instanceof String) {
					resultDisplay = " returning '${result}'"
				} else if (result) {
					if (result == this) {
						resultDisplay = " returning **this**"
					} else {
						resultDisplay = " returning ${result}"
					}
				}

if (result==null) {
	resultDisplay = resultDisplay + " - data type is 'null'"

} else {
	resultDisplay = resultDisplay + " - data type is " + result.getClass().name
}


				Logger.setTraceTabDepth(Logger.getTabDepth()-1)
				Logger.trace("Leaving: ${functionName}${resultDisplay}")
			}
			finally {
				skipTrace = oldSkipTrace;
			}
		}

		return result
	}
}

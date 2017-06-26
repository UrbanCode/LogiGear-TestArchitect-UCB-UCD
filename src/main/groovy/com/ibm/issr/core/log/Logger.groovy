/**
 * (c) Copyright IBM Corporation 2017.
 * This is licensed under the following license.
 * The Eclipse Public 1.0 License (http://www.eclipse.org/legal/epl-v10.html)
 * U.S. Government Users Restricted Rights:  Use, duplication or disclosure restricted by GSA ADP Schedule Contract with IBM Corp.
 */

package com.ibm.issr.core.log

import com.ibm.issr.core.plugin.PluginHelper;

/**
 * This is intended to be a very lightweight message logger.  It is
 * modeled after and could easily be integrated with log4j, but this
 * implementation is designed to be more lightweight.  This class contains
 * only static information.
 * @author ltclark
 *
 */
class Logger {
	public static boolean displayTrace = false;
	public static boolean displayDebug = false;
	public static boolean displayInfo = true;
	public static boolean displayWarn = true;
	public static boolean displayError = true;
	public static boolean displayFatal = true;
	public static int tabDepth = 0
	private static String tabOutput = ""
	private static String theLoggingLevel = "info"

	/**
	 * Sets the tab level but only if trace is on.
	 * @param newTabDepth
	 */
	public static void setTraceTabDepth( int newTabDepth ) {
		if (displayTrace) {
			if (newTabDepth > 0) {
				tabDepth = newTabDepth
			} else {
				tabDepth = 0
			}
			tabOutput = ""
			for (int i=0; i<tabDepth; ++i) {
				tabOutput = tabOutput + "\t"
			}
		}
	}

	/**
	 * Returns the current tab depth.
	 */
	public static int getTabDepth() {
		return tabDepth
	}

	/**
	 * Sets the logging level to the named level.  The order of the levels is
	 * trace, debug, info, warn, error, fatal.  When you select a level, it
	 * displays that level and the higher level.  For example, if you set the
	 * level to 'debug', it will display all but the 'trace' messages.  Throws
	 * exception if the level doesn't match.
	 * @param level Either blank or one of the logging levels.
	 */
	public static void setLoggingLevel( String level ) {
		boolean found = false;
		level = level.trim().toLowerCase()
		if (level.equals("trace")) {
			found = true;
			displayTrace = true;
		}
		if (level.equals("debug") || found) {
			found = true;
			displayDebug = true;
		}
		if (level.equals("info") || found || level.length()==0) {
			found = true;
			displayInfo = true;
		}
		if (level.equals("warn") || found) {
			found = true;
			displayWarn = true;
		}
		if (level.equals("error") || found) {
			found = true;
			displayError = true;
		}
		if (level.equals("fatal") || found) {
			found = true;
			displayFatal = true;
		}
		if (! found) {
			PluginHelper.abortPlugin( "Logger.setLoggingLevel() called with invalid level of '${level}'")
		}
		theLoggingLevel = level
		Logger.debug("Logging level set to '${level}'")
	}

	public static String getLoggingLevel() {
		return theLoggingLevel
	}

	/**
	 * Logs a 'trace' level message.
	 */
	public static void trace( def message ) {
		if (displayTrace) {
			println "${tabOutput}TRACE: ${message}"
		}
	}

	/**
	 * Logs a 'debug' level message.
	 */
	public static void debug( def message ) {
		if (displayDebug) {
			println "${tabOutput}DEBUG: ${message}"
		}
	}

	/**
	 * Logs an 'info' level message.
	 */
	public static void info( def message ) {
		if (displayInfo) {
			println "${tabOutput}INFO: ${message}"
		}
	}

	/**
	 * Logs a 'warn' level message.
	 */
	public static void warn( def message ) {
		if (displayWarn) {
			println "${tabOutput}WARNING: ${message}"
		}
	}

	/**
	 * Logs an 'error' level message.
	 */
	public static void error( def message ) {
		if (displayError) {
			println "${tabOutput}ERROR: ${message}"
		}
	}

	/**
	 * Logs a 'fatal' level message.
	 */
	public static void fatal( def message ) {
		if (displayFatal) {
			println "${tabOutput}FATAL: ${message}"
		}
	}


	/**
	 * Prints a message to the designated log level.
	 * @param loggerLevel Log level enumeration, such as LoggerLevel.INFO
	 * @param message The message to print.
	 */
	public static void println( LoggerLevel loggerLevel, def message ) {
		loggerLevel.println message
	}


	/**
	 * This prints an {@link Exception}/Throwable stack trace by printing the
	 * stack to the log level associated with loggerLevel.
	 * @param loggerLevel An enumerated value for the appropriate log level, such as LoggerLevel.DEBUG
	 * @param e The exception.
	 */
	public static void printStackTrace( LoggerLevel loggerLevel, Throwable e ) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		pw.flush()
		sw.flush()
		String stringTrace = sw.toString()
		stringTrace.eachLine { line ->
			loggerLevel.println line
		}
	}
}

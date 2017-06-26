/**
 * (c) Copyright IBM Corporation 2017.
 * This is licensed under the following license.
 * The Eclipse Public 1.0 License (http://www.eclipse.org/legal/epl-v10.html)
 * U.S. Government Users Restricted Rights:  Use, duplication or disclosure restricted by GSA ADP Schedule Contract with IBM Corp.
 */

package com.ibm.issr.core.log

/**
 * This is an enumeration which represents the different levels of logging (trace, debug, info, warn, error, fatal)
 * @author ltclark
 *
 */
enum LoggerLevel {
	TRACE( "trace", { line -> Logger.trace line } ),
	DEBUG( "debug", { line -> Logger.debug line } ),
	INFO( "info", { line -> Logger.info line } ),
	WARN( "warn", { line -> Logger.warn line } ),
	ERROR( "error", { line -> Logger.error line } ),
	FATAL( "fatal", { line -> Logger.fatal line } );

	private String displayName;
	private Closure printHandler;

	/**
	 * Constructor
	 * @param displayName The display name, such as 'trace'
	 * @param printHandler This is a closure that can be called to print a line to the correct logger.
	 * It takes one parameter - which is the line to print to the logger.
	 */
	private LoggerLevel( String displayName, Closure printHandler ) {
		this.displayName = displayName;
		this.printHandler = printHandler;
	}

	/**
	 * Returns the (display) name.
	 */
	public String getName() {
		return displayName;
	}

	/**
	 * Prints the given line to the appropriate log level.
	 * @param line The line to print.
	 */
	public void println( String line ) {
		printHandler line
	}
}

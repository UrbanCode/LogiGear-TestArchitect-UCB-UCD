/**
 * (c) Copyright IBM Corporation 2017.
 * This is licensed under the following license.
 * The Eclipse Public 1.0 License (http://www.eclipse.org/legal/epl-v10.html)
 * U.S. Government Users Restricted Rights:  Use, duplication or disclosure restricted by GSA ADP Schedule Contract with IBM Corp.
 */

/**
 * <p>This package contains logging functions that are designed to be similar to Log4J logging.
 * Currently, these are not integrated with log4j, but they could easily be tied together.</p>
 * <p>The following log levels are defined (in order): trace, debug, info, warn, error, fatal.
 * Each level is higher than the previous.  When you enable logging for a level, such as 'debug',
 * it turns on all logging at that level and higher.  So, 'debug' will turn on debug, info, warn, error and fatal,
 * but does not turn on tracing.</p>
 * <p>Here is a quick overview of the classes in this package...</p>
 * <ul>
 * <li>{@link Logger} implements the logging engine.
 * </li>
 * <li>{@link LoggerLevel} defines the logging levels (as an enumeration).
 * </li>
 * <li>{@link LogTracingClass} is a helper class for tracing method calls.  Simply declare this
 * class as a base class (class x extends this) to automatically turn on log tracing for all of the
 * methods in the class (and any inherited classes).  When you do so, a trace message is generated when
 * a function is called and when the function returns.
 * </li>
 * </ul>
 */
package com.ibm.issr.core.log

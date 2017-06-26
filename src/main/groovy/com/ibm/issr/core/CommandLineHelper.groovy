/**
 * (c) Copyright IBM Corporation 2017.
 * This is licensed under the following license.
 * The Eclipse Public 1.0 License (http://www.eclipse.org/legal/epl-v10.html)
 * U.S. Government Users Restricted Rights:  Use, duplication or disclosure restricted by GSA ADP Schedule Contract with IBM Corp.
 */

package com.ibm.issr.core

/**
 * Utility/helper function for working with generating command lines.
 * @author ltclark
 *
 */
class CommandLineHelper {
	/**
	 * Generates a script embeddable command line version of the command line.  This
	 * deals with quotes and spaces.  For example, if the input is  ["test.sh", "a b", "c", "d"],
	 * then this generates 'test.sh "a b" c d'.  For elements that have embedded spaces, this
	 * surrounds the element with double quotes, which works on Windows or Linux.
	 * @param commandLine An array of elements in a command line call.  The first element
	 * is the command and subsequent elements are the parameters.  For example,
	 * ["test.sh", "a b", "c", "d"].
	 * @return The string version of the command.
	 */
	public static String getCommandString( String[] commandLine ) {
		String retval = "";
		String delim = ""
		for (String cmd in commandLine) {
			if (cmd.contains(" ")) {
				if (cmd.startsWith('"') && cmd.endsWith('"')) {
					retval = retval + delim + cmd;
				} else {
					retval = retval + delim + '"' + cmd + '"';
				}
			} else if (! cmd) {
				retval = retval + delim + '""'
			} else {
				retval = retval + delim + cmd;
			}
			delim = " ";
		}
		return retval;
	}
}

/**
 * (c) Copyright IBM Corporation 2017.
 * This is licensed under the following license.
 * The Eclipse Public 1.0 License (http://www.eclipse.org/legal/epl-v10.html)
 * U.S. Government Users Restricted Rights:  Use, duplication or disclosure restricted by GSA ADP Schedule Contract with IBM Corp.
 */

package com.ibm.issr.core.plugin

/**
 * Class containing Plugin Helper functions.
 * @author ltclark
 *
 */
class PluginHelper {
	/**
	 * Immediately abort the plugin because of a failure condition described by the message.
	 * This actually throws a message.  The message is displayed to the output as an 'error'
	 * message, but no stack trace is thrown.
	 * @param msg
	 */
	static public void abortPlugin( String msg ) {
		throw new AbortPluginException(msg)
	}
}

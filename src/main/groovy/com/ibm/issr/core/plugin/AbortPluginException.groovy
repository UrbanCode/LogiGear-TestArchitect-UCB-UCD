/**
 * (c) Copyright IBM Corporation 2017.
 * This is licensed under the following license.
 * The Eclipse Public 1.0 License (http://www.eclipse.org/legal/epl-v10.html)
 * U.S. Government Users Restricted Rights:  Use, duplication or disclosure restricted by GSA ADP Schedule Contract with IBM Corp.
 */

package com.ibm.issr.core.plugin

/**
 * This exception is thrown by see {@link PluginHelper}.abortPlugin().  The root plugin
 * class should catch this exception type and display the corresponding message.
 * @author ltclark
 *
 */
class AbortPluginException extends Exception {
	public AbortPluginException(String msg) {
		super(msg);
	}
}

/**
 * (c) Copyright IBM Corporation 2017.
 * This is licensed under the following license.
 * The Eclipse Public 1.0 License (http://www.eclipse.org/legal/epl-v10.html)
 * U.S. Government Users Restricted Rights:  Use, duplication or disclosure restricted by GSA ADP Schedule Contract with IBM Corp.
 */

 package com.ibm.issr.core

class XmlUtility {

	/**
	 * Escapes " ' & < and > as &quot;, &apos;, &quot;, &lt; and &gt.
	 * @param orig
	 * @return Encoded XML string.
	 */
	public static String escapeXml( String orig ) {
		return orig.replaceAll("&", "&quot;").replaceAll("\"", "&quot;").replaceAll("'", "&apos;").replaceAll("<", "&lt;").replaceAll(">", "&gt;")
	}
}

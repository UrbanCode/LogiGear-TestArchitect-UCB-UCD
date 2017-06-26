/**
 * (c) Copyright IBM Corporation 2017.
 * This is licensed under the following license.
 * The Eclipse Public 1.0 License (http://www.eclipse.org/legal/epl-v10.html)
 * U.S. Government Users Restricted Rights:  Use, duplication or disclosure restricted by GSA ADP Schedule Contract with IBM Corp.
 */

package com.ibm.issr.core.file

import com.ibm.issr.core.log.LogTracingClass
import com.ibm.issr.core.log.Logger

/**
 * Checksum utility class - singleton
 * @author ltclark
 *
 */
class ChecksumHelper extends LogTracingClass {
	private static instance = new ChecksumHelper()

	// constructor is private
	private ChecksumHelper() {

	}

	/**
	 * Returns the singleton instance.
	 */
	public static ChecksumHelper getInstance() {
		return instance
	}

	/**
	 * Calculates the MD5 checksum for a file.
	 * @param file The handle to the file.
	 * @return Returns the checksum string.
	 */
	public String calculateChecksum(File file) {

		if (! (file.exists() && file.isFile())) {
			throw new Exception( "Unable to find a file named ${file.getAbsolutePath()}" )
		}
		java.security.MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
		def checksum = ""
		file.withInputStream() { is ->
			byte[] buffer = new byte[8192]
			int read = 0
			while ( (read=is.read(buffer)) > 0) {
				digest.update(buffer, 0, read)
			}
			checksum = new BigInteger(1, digest.digest()).toString(16).padLeft(32,'0')
		}
	}
}

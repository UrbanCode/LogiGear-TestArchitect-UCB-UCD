/**
 * (c) Copyright IBM Corporation 2017.
 * This is licensed under the following license.
 * The Eclipse Public 1.0 License (http://www.eclipse.org/legal/epl-v10.html)
 * U.S. Government Users Restricted Rights:  Use, duplication or disclosure restricted by GSA ADP Schedule Contract with IBM Corp.
 */

package com.ibm.issr.core.file

import java.io.File;

import com.ibm.issr.core.log.LogTracingClass;

/**
 * File helper functions.  This is a singleton
 * @author ltclark
 *
 */
class FileHelper extends LogTracingClass {
	private static instance = new FileHelper()

	// constructor is private
	private FileHelper() {

	}

	/**
	 * Returns the singleton instance.
	 */
	public static FileHelper getInstance() {
		return instance
	}

	/**
	 * Returns the relative path for the file relative to the 'directoryFile'.  For
	 * example if the directory is 'c:/test' and the file is 'c:/test/foo/bar.txt', this
	 * returns 'foo/bar.txt'.
	 * @param directoryFile Handle to a directory.
	 * @param file Handle to a specific file
	 * @return The relative path.
	 */
	public String calculateRelativePath( File directoryFile, File file ) {
		return directoryFile.toURI().relativize( file.toURI() ).getPath()
	}

}

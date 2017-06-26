/**
 * (c) Copyright IBM Corporation 2017.
 * This is licensed under the following license.
 * The Eclipse Public 1.0 License (http://www.eclipse.org/legal/epl-v10.html)
 * U.S. Government Users Restricted Rights:  Use, duplication or disclosure restricted by GSA ADP Schedule Contract with IBM Corp.
 */

package com.ibm.issr.core

import com.ibm.issr.core.log.LogTracingClass
import com.ibm.issr.core.log.Logger
import com.ibm.issr.core.plugin.PluginHelper;

/**
 * This is a class that is capable of running operating system commands with various options.
 * Note that the various setter functions return 'this' allowing syntax like
 * new CommandRunner().setCommand(["a"]).setTitle("foobar").setDirectory(directory)
 * @author ltclark
 *
 */
class CommandRunner extends LogTracingClass {
	private String[] command=null		// The command
	private String title=null			// The command title
	private String activeDirectory=null
	private boolean displayOutput=true
	private Closure printHandler=null
	private boolean runViaCommandShell = false
	// If timeoutInSeconds > 0, then there is a timeout
	private int timeoutInSeconds = 0

	/**
	 * Sets the actual command, which is a String array of the command and its parameters.
	 * @param command A string array where the first element is the actual command and every other
	 * element is a parameter (in order), such as ["command","param1","param 2"]
	 * @return Returns 'this'.
	 */
	public CommandRunner setCommand( String[] command ) {
		this.command = command;
		return this;
	}

	/**
	 * Sets the title of the command.
	 * @param title The title.
	 * @return Returns 'this'.
	 */
	public CommandRunner setTitle( String title ) {
		this.title = title
		return this
	}

	/**
	 * Sets an optional timeout (in seconds).  If the timeout is set to a non-zero value, then an error is thrown if it runs out of time
	 * @param timeoutInSeconds The timeout in seconds, or 0 to have no timeout.
	 * @return Returns 'this'.
	 */
	public CommandRunner setTimeout( int timeoutInSeconds ) {
		this.timeoutInSeconds = timeoutInSeconds
		return this
	}

	/**
	 * Set the 'runViaCommandShell' property. If this is true, then the command is prefixed with a command shell call, which
	 * is "sh -c" for Linux/Unix or "cmd /c" for Windows.
	 * @return Return 'this'
	 */
	public CommandRunner setRunViaCommandShell( boolean runViaCommandShell ) {
		this.runViaCommandShell = runViaCommandShell
		return this
	}

	/**
	 * Call this function to set an output handler which is called to emit the
	 * output of the nested application.
	 * @param printHandler The command handler.  It is called once per line from
	 * the nested application.  It has one parameter - that line.  It should print,
	 * save, log, ... as appropriate.
	 * @return Returns 'this'
	 */
	public CommandRunner setPrintln( Closure printHandler ) {
		this.printHandler = printHandler;
		return this
	}

	/**
	 * Sets the output to 'Logger.trace' output every line
	 * @return returns 'this'
	 */
	public CommandRunner setPrintToTrace() {
		this.printHandler = { line -> Logger.info line }
	}

	/**
	 * Sets the active directory to use when calling the command.
	 * @param activeDirectory The active directory.  Set to null for current working directory.
	 * @return 'this'
	 */
	public CommandRunner setActiveDirectory( String activeDirectory ) {
		this.activeDirectory = activeDirectory;
		return this
	}

	/**
	 * Should the output of the nested command be sent to the standard output??
	 * @param displayOut
	 * @return
	 */
	public CommandRunner setDisplayOut( boolean displayOut ) {
		this.displayOutput = displayOut
		return this
	}

	/**
	 * Returns a string version of the command.  This is quite useable for logging output.
	 */
	public String getCommandAsString() {
		String[] actualCommand = command
		if (runViaCommandShell) {
			String osName = System.getProperty("os.name")
			boolean isWindows = (osName.toLowerCase().contains("windows"))
			actualCommand = new String[command.size()+2]
			if (isWindows) {
				actualCommand[0] = "cmd"
				actualCommand[1] = "/c"
			} else {
				actualCommand[0] = "sh"
				actualCommand[1] "-c"
			}
			for (int i=0; i<command.size(); ++i) {
				actualCommand[i+2] = command[i];
			}

		}
		return CommandLineHelper.getCommandString(actualCommand)
	}

	/**
	 * Perform the command!!  Throws exception on error.
	 * @return Returns the exit code returned by the command.  By convention,
	 * an exit code of 0 means success.
	 */
	public int runCommand() {
		Closure printer = printHandler;
		if (! printer) {
			printer = { line -> println line }
		}
		if (this.command == null) {
			PluginHelper.abortPlugin( "CommandRunner failed - no command was provided")
		}
		String[] actualCommand = command
		if (runViaCommandShell) {
			String osName = System.getProperty("os.name")
			boolean isWindows = (osName.toLowerCase().contains("windows"))
			actualCommand = new String[command.size()+2]
			if (isWindows) {
				actualCommand[0] = "cmd"
				actualCommand[1] = "/c"
			} else {
				actualCommand[0] = "sh"
				actualCommand[1] "-c"
			}
			for (int i=0; i<command.size(); ++i) {
				actualCommand[i+2] = command[i];
			}

		}

		Logger.info "Calling command: ${this.getCommandAsString()}"

		StringBuffer cmdOut = new StringBuffer()
		Process proc
		if (this.activeDirectory) {
			// Verify the existence of the 'activeDirectory'.  If it doesn't exist, an odd exception is thrown.
			if (! (new File(activeDirectory)).isDirectory()) {
				PluginHelper.abortPlugin("Attempting to run a command script in the directory '${activeDirectory}', but the directory does not exist")
			}
			proc = actualCommand.execute(null, (new File( activeDirectory )))
		} else {
			proc = actualCommand.execute()
		}
		if (timeoutInSeconds > 0) {
			long startTime = System.currentTimeMillis()
			long endTime = startTime + ((long) timeoutInSeconds) * 1000L
			boolean procIsRunning = true
			while (procIsRunning) {
				if (System.currentTimeMillis() > endTime) {
					throw new Exception("The command line call timed out (timeout set to ${timeoutInSeconds} seconds)")
				}
				// sleep for a second
				try {
					Thread.sleep(1000)
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt()
				}
				// Display any new output lines
				if (this.displayOutput) {
					cmdOut.eachLine() { line -> printer line }
				}
				// test to see if done - by seeing if there is an exit value
				try {
					proc.exitValue()
					// If the exitValue() call worked, then the proc is done!!
					procIsRunning = false
				} catch (IllegalThreadStateException e) {
					// the process isn't done yet
				}
			}
		} else {
			proc.waitForProcessOutput(cmdOut, cmdOut)
			if (this.displayOutput) {
				cmdOut.eachLine() { line -> printer line }
			}
		}
		return proc.exitValue();

	}
}

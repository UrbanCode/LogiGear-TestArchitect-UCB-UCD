/**
 * (c) Copyright IBM Corporation 2017.
 * This is licensed under the following license.
 * The Eclipse Public 1.0 License (http://www.eclipse.org/legal/epl-v10.html)
 * U.S. Government Users Restricted Rights:  Use, duplication or disclosure restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import groovy.io.FileType

import com.ibm.issr.core.CommandRunner
import com.ibm.issr.core.log.Logger

class CallTestArchitectBatch extends UCPluginStepImplementation {
	String resultLocation
	String batchLocation
	boolean displayResultFiles

	/**
	 * Main application entry point.
	 */
	public static void main( java.lang.String[] args ) {
		def stepImpl = new CallTestArchitectBatch();
		stepImpl.performStep(args) { stepImpl.execute() }
	}

	/**
	 * This is the implementation of the Checksum step.
	 */
	void execute() {
		batchLocation = inProps.batchLocation
		resultLocation = inProps.resultLocation
		displayResultFiles = (inProps.displayResultFiles == "true")
		def resultDir = new File(resultLocation)

		// Display a summary of what this plugin is going to attempt to do
		Logger.info "Call a LogiGear Test Architect batch file"
		Logger.info "   batchLocation=${batchLocation}"
		Logger.info "   resultLocation=${resultLocation}"
		Logger.info "   displayResultFiles=${displayResultFiles}"
		super.displayParameters()

		//Run the batch file
		String[] commandLine = [batchLocation]
		CommandRunner commandRunner = new CommandRunner()
		commandRunner.setCommand(commandLine)
		commandRunner.runCommand()

		//Parse the results
		def isPassed = true
		resultDir.eachFileRecurse (FileType.FILES) { file ->
			try{
				def xUnitResult = new XmlParser().parse(file)
				Logger.info "Parsing file: " + file.getAbsolutePath()
				xUnitResult.testsuite.each{ testsuite ->
					isPassed &= (testsuite.'@errors' == "0" && testsuite.'@failures' == "0")
					Logger.info String.format("name: %s | err: %s | fail: %s", testsuite.'@name',testsuite.'@errors', testsuite.'@failures')
				}
			} catch (Exception ex){
				Logger.error(ex)
			}
		}

		//Display results
		Logger.info "...\n...\n"
		if (isPassed){
			Logger.info "TestArchitect reports that all tests are Passed"
			outProps.put("Status", "Success")
		} else {
			Logger.info "TestArchitect reports that all tests are not Passed.\nOne or more test case are failed\n"
			outProps.put("Status", "Failure")
		}

		if (displayResultFiles) {
			resultDir.eachFileRecurse(FileType.FILES) { file ->
				Logger.info ""
				Logger.info "RESULT FILE: " + file.name
				file.eachLine { String line ->
					println line
				}
			}
		}
	}
}

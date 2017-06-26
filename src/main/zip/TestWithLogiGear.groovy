/**
 * (c) Copyright IBM Corporation 2017.
 * This is licensed under the following license.
 * The Eclipse Public 1.0 License (http://www.eclipse.org/legal/epl-v10.html)
 * U.S. Government Users Restricted Rights:  Use, duplication or disclosure restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import groovy.io.FileType

import com.ibm.issr.core.CommandRunner
import com.ibm.issr.core.log.Logger

class TestWithLogiGear extends UCPluginStepImplementation {
	String testArchitectLocation
	boolean displayResultFiles
	String TAmod
	String TAresultname
	String TAxupath
	String TAproid
	String TAuid
	String TApwd
	String TAsessionid
	String TAvar
	String TAcomment
	String TAdbname
	String TAsrvid
	String TAdelay
	String TAversions
	String TAexporthtmlpath
	String TAexportxmlpath
	String TAtestsetid
	String TAuploadresulttorepos
	String TAuploadresultcond
	String TAuploadedfiletype
	String TAtimetraveling
	String TAudf
	String TAcapturecond
	String TAnumofinteraction
	String TAexportscreenshotcond
	String TAdevices
	String TAstartupsettings
	String TAtoolname
	String TAtoolpath
	String TAtoolscript
	String TAtoolcmd
	String TAexechost
	String TAexecport
	String TArshost
	String TArsport
	String TAdbtype
	String TAlsaddr
	String TAlsport
	String TAlsusr
	String TAredunlsaddr
	String TAredunlsport
	int timeout

	/**
	 * Main application entry point.
	 */
	public static void main( java.lang.String[] args ) {
		def stepImpl = new TestWithLogiGear();
		stepImpl.performStep(args) { stepImpl.execute() }
	}

	/**
	 * This is the implementation of the Checksum step.
	 */
	void execute() {
		testArchitectLocation = inProps.testArchitectLocation
		displayResultFiles = (inProps.displayResultFiles == "true")
		TAmod = inProps.TAmod
		TAresultname = inProps.TAresultname
		TAxupath = inProps.TAxupath
		TAproid = inProps.TAproid
		TAuid = inProps.TAuid
		TApwd = inProps.TApwd
		TAsessionid = inProps.TAsessionid
		TAvar = inProps.TAvar
		TAcomment = inProps.TAcomment
		TAdbname = inProps.TAdbname
		TAsrvid = inProps.TAsrvid
		TAdelay = inProps.TAdelay
		TAversions = inProps.TAversions
		TAexporthtmlpath = inProps.TAexporthtmlpath
		TAexportxmlpath = inProps.TAexportxmlpath
		TAtestsetid = inProps.TAtestsetid
		TAuploadresulttorepos = inProps.TAuploadresulttorepos
		TAuploadresultcond = inProps.TAuploadresultcond
		TAuploadedfiletype = inProps.TAuploadedfiletype
		TAtimetraveling = inProps.TAtimetraveling
		TAudf = inProps.TAudf
		TAcapturecond = inProps.TAcapturecond
		TAnumofinteraction = inProps.TAnumofinteraction
		TAexportscreenshotcond = inProps.TAexportscreenshotcond
		TAdevices = inProps.TAdevices
		TAstartupsettings = inProps.TAstartupsettings
		TAtoolname = inProps.TAtoolname
		TAtoolpath = inProps.TAtoolpath
		TAtoolscript = inProps.TAtoolscript
		TAtoolcmd = inProps.TAtoolcmd
		TAexechost = inProps.TAexechost
		TAexecport = inProps.TAexecport
		TArshost = inProps.TArshost
		TArsport = inProps.TArsport
		TAdbtype = inProps.TAdbtype
		TAlsaddr = inProps.TAlsaddr
		TAlsport = inProps.TAlsport
		TAlsusr = inProps.TAlsusr
		TAredunlsaddr = inProps.TAredunlsaddr
		TAredunlsport = inProps.TAredunlsport
		timeout = 0
		if (inProps.timeout.isInteger()) {
			timeout = inProps.timeout.toInteger()
		}

		// set some default values
		if (! TAexportscreenshotcond) {
			TAexportscreenshotcond = "0"
		}

		// Convert paths to absolute - to make sure that they work ok when calling TestArchitect
		def resultDir = new File(TAxupath)
		TAxupath = resultDir.getAbsolutePath()
		if (TAexporthtmlpath) {
			TAexporthtmlpath = (new File(TAexporthtmlpath)).getAbsolutePath()
		}
		if (TAexportxmlpath) {
			TAexportxmlpath = (new File(TAexportxmlpath)).getAbsolutePath()
		}
//		if (TAuploadresulttorepos) {
//			TAuploadresulttorepos = (new File(TAuploadresulttorepos)).getAbsolutePath()
//		}

		// Display a summary of what this plugin is going to attempt to do
		Logger.info "Call a LogiGear Test Architect Test"
		Logger.info "   testArchitectLocation=${testArchitectLocation}"
		Logger.info "   /mod=${TAmod}"
		Logger.info "   /resultname=${TAresultname}"
		Logger.info "   /xupath=${TAxupath}"
		Logger.info "   /proid=${TAproid}"
		Logger.info "   /uid=${TAuid}"
		Logger.info "   /sessionid=${TAsessionid}"
		Logger.info "   /var=${TAvar}"
		Logger.info "   /comment=${TAcomment}"
		Logger.info "   /dbname=${TAdbname}"
		Logger.info "   /srvid=${TAsrvid}"
		Logger.info "   /delay=${TAdelay}"
		Logger.info "   /versions=${TAversions}"
		Logger.info "   /exporthtmlpath=${TAexporthtmlpath}"
		Logger.info "   /exportxmlpath=${TAexportxmlpath}"
		Logger.info "   /testsetid=${TAtestsetid}"
		Logger.info "   /uploadresulttorepos=${TAuploadresulttorepos}"
		Logger.info "   /uploadresultcond=${TAuploadresultcond}"
		Logger.info "   /uploadedfiletype=${TAuploadedfiletype}"
		Logger.info "   /timetraveling=${TAtimetraveling}"
		Logger.info "   /udf=${TAudf}"
		Logger.info "   /capturecond=${TAcapturecond}"
		Logger.info "   /numofinteraction=${TAnumofinteraction}"
		Logger.info "   /exportscreenshotcond=${TAexportscreenshotcond}"
		Logger.info "   /devices=${TAdevices}"
		Logger.info "   /startupsettings=${TAstartupsettings}"
		Logger.info "   /toolname=${TAtoolname}"
		Logger.info "   /toolpath=${TAtoolpath}"
		Logger.info "   /toolscript=${TAtoolscript}"
		Logger.info "   /toolcmd=${TAtoolcmd}"
		Logger.info "   /exechost=${TAexechost}"
		Logger.info "   /execport=${TAexecport}"
		Logger.info "   /rshost=${TArshost}"
		Logger.info "   /rsport=${TArsport}"
		Logger.info "   /dbtype=${TAdbtype}"
		Logger.info "   /lsaddr=${TAlsaddr}"
		Logger.info "   /lsport=${TAlsport}"
		Logger.info "   /lsusr=${TAlsusr}"
		Logger.info "   /redunlsaddr=${TAredunlsaddr}"
		Logger.info "   /redunlsport=${TAredunlsport}"
		Logger.info "   displayResultFiles=${displayResultFiles}"
		Logger.info "   timeout=${timeout}"
		super.displayParameters()

		String[] commandLine = buildCommand()

		CommandRunner commandRunner = new CommandRunner()
		commandRunner.setCommand(commandLine)
//		Logger.info "Command Line: " + commandRunner.getCommandAsString()
		commandRunner.setTimeout( timeout )
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

		if (displayResultFiles) {
			resultDir.eachFileRecurse(FileType.FILES) { file ->
				Logger.info ""
				Logger.info "RESULT FILE: " + file.name
				file.eachLine { String line ->
					println line
				}
			}
		}

		//Display results
		Logger.info "...\n...\n"
		if (isPassed){
			Logger.info "TestArchitect reports that all tests are Passed"
			outProps.put("Status", "Success")
		} else {
			Logger.info "TestArchitect reports that all tests are not Passed.\nOne or more test case are failed\n"
//			outProps.put("Status", "Failure")
			System.exit(1)
		}

	}

	/**
	 * Builds the command line that needs to be executed.
	 * @return The command line as a string array.  The first element is the command and
	 * each subsequent element is one parameter.
	 */
	String[] buildCommand()
	{
		def cmd = []

		cmd << (new File( "${testArchitectLocation}/jre/bin/java.exe")).absolutePath
		cmd << "-jar"
		cmd << "-Xmx512m"
		cmd << (new File("${testArchitectLocation}/binclient/TACommandLine.jar")).absolutePath

		addParam(cmd, "/mod", TAmod)
		addParam(cmd, "/resultname", TAresultname)
		addParam(cmd, "/xupath", TAxupath)
		addParam(cmd, "/proid", TAproid)
		addParam(cmd, "/uid", TAuid)
		addParam(cmd, "/pwd", TApwd)
		addParam(cmd, "/sessionid", TAsessionid)
		addParam(cmd, "/var", TAvar)
		addParam(cmd, "/comment", TAcomment)
		addParam(cmd, "/dbname", TAdbname)
		addParam(cmd, "/srvid", TAsrvid)
		addParam(cmd, "/delay", TAdelay)
		addParam(cmd, "/versions", TAversions)
		addParam(cmd, "/exporthtmlpath", TAexporthtmlpath)
		addParam(cmd, "/exportxmlpath", TAexportxmlpath)
		addParam(cmd, "/testsetid", TAtestsetid)
		addParam(cmd, "/uploadresulttorepos", TAuploadresulttorepos)
		addConditionalParam(cmd, "/uploadresultcond", TAuploadresultcond)
		addParam(cmd, "/uploadedfiletype", TAuploadedfiletype)
		addParam(cmd, "/timetraveling", TAtimetraveling)
		addParam(cmd, "/udf", TAudf)
		addParam(cmd, "/capturecond", TAcapturecond)
		addParam(cmd, "/numofinteraction", TAnumofinteraction)
		addParam(cmd, "/exportscreenshotcond", TAexportscreenshotcond)
		addConditionalParam(cmd, "/devices", TAdevices)
		addConditionalParam(cmd, "/startupsettings", TAstartupsettings)
		addParam(cmd, "/toolname", TAtoolname)
		addParam(cmd, "/toolpath", TAtoolpath)
		addParam(cmd, "/toolscript", TAtoolscript)
		addParam(cmd, "/toolcmd", TAtoolcmd)
		addParam(cmd, "/exechost", TAexechost)
		addParam(cmd, "/execport", TAexecport)
		addParam(cmd, "/rshost", TArshost)
		addParam(cmd, "/rsport", TArsport)
		addParam(cmd, "/dbtype", TAdbtype)
		addParam(cmd, "/lsaddr", TAlsaddr)
		addParam(cmd, "/lsport", TAlsport)
		addParam(cmd, "/lsusr", TAlsusr)
		addParam(cmd, "/redunlsaddr", TAredunlsaddr)
		addParam(cmd, "/redunlsport", TAredunlsport)

		// Set /openresult to 'no' so no GUI windows are left opened
		addParam(cmd, "/openresult", "no")
	}

	/**
	 * IF a Parameter value to the command even if the value is empty
	 */
	private addParam( def cmd, def optionFlag, def value ) {
		cmd << optionFlag
		if (value) {
			cmd << value
		} else {
			cmd << ""
		}
	}

	/**
	 * IF a value is defined add the option and value.
	 */
	private addConditionalParam( def cmd, def optionFlag, def value ) {
		if (value) {
			cmd << optionFlag
			cmd << value
		}
	}
}

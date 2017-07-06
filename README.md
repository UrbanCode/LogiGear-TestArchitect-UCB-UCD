# IBM UrbanCode Deploy and Build LogiGear TestArchitect Plug-in [![Build Status](https://travis-ci.org/IBM-UrbanCode/LogiGear-TestArchitect-UCB-UCD.svg?branch=master)](https://travis-ci.org/IBM-UrbanCode/LogiGear-TestArchitect-UCB-UCD)
---

### License
This plug-in is protected under the [Eclipse Public 1.0 License](http://www.eclipse.org/legal/epl-v10.html)

### Compatibility
	The IBM UrbanCode Deploy automation plug-in uses the TACommandLine.jar library.
	This plug-in requires version 6.1.1 or later of IBM UrbanCode Deploy.

### Installation
	The packaged zip is located in the releases folder. No special steps are required for installation.
	See Installing plug-ins in UrbanCode Deploy. Download this zip file if you wish to skip the
	manual build step. Otherwise, download the entire LogiGear-TestArchitest-UCB-UCD and
	run the "gradle" command in the top level folder. This should compile the code and create
	a new distributable zip within the releases folder. Use this command if you wish to make
	your own changes to the plug-in.

### History
    Version 7
        - Separated build process for Build and Deploy.
    Version 6
        - Initial open source release.
        - Merged the two IBM UrbanCode Build and Deploy specific steps in two shared steps.

### How to build the plug-in from command line:

1. Navigate to the base folder of the project through command line.
2. Make sure that there is build.gradle file, and then execute `gradle` command.
3. The built plug-in is located at `build/distributions/logigear-testarchitect-xxxx-open-vdev.zip`

Note: The `gradle -Pbuild=y` command will build the IBM UrbanCode build specific plug-in. While `gradle` will build the plug-in for IBM UrbanCode Deploy.

# IBM UrbanCode Deploy and Build LogiGear TestArchitect Plug-in
---

### License
This plugin is protected under the [Eclipse Public 1.0 License](http://www.eclipse.org/legal/epl-v10.html)

### Compatibility
	The IBM UrbanCode Deploy automation plugin uses the TACommandLine.jar library.
	This plug-in requires version 6.1.1 or later of IBM UrbanCode Deploy.

### Installation
	The packaged zip is located in the releases folder. No special steps are required for installation.
	See Installing plug-ins in UrbanCode Deploy. Download this zip file if you wish to skip the
	manual build step. Otherwise, download the entire LogiGear-TestArchitest-UCB-UCD and
	run the "gradle" command in the top level folder. This should compile the code and create
	a new distributable zip within the releases folder. Use this command if you wish to make
	your own changes to the plugin.

### History
    Version 6
        - Initial open source release.
        - Merged the two IBM UrbanCode Build and Deploy specific steps in two shared steps.

### How to build the plugin from command line:

1. Navigate to the base folder of the project through command line.
2. Make sure that there is build.gradle file, and then execute 'gradle' command.
3. The built plugin is located at releases/logigear-testarchitect-open-vdev.zip

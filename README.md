# Description

Install4j Support

# Install Runtime into Local Repository

Maven needs to have the Install4j runtime jar in the local repository to build.

This is not yet in any available remote repository, so you'll need to install it manually before building:

    mvn -Dinstall4j.home=<path> -Pinstall-runtime

Where path is the location to where Install4j has been installed, for example on Mac OSX:

    mvn -Dinstall4j.home="/Applications/install4j 5/" -Pinstall-runtime

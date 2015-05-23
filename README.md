<!--

    Copyright (c) 2008-2015 Sonatype, Inc. All rights reserved.

    This program is licensed to you under the Apache License Version 2.0,
    and you may not use this file except in compliance with the Apache License Version 2.0.
    You may obtain a copy of the Apache License Version 2.0 at http://www.apache.org/licenses/LICENSE-2.0.

    Unless required by applicable law or agreed to in writing,
    software distributed under the Apache License Version 2.0 is distributed on an
    "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the Apache License Version 2.0 for the specific language governing permissions and limitations there under.

-->
# Description

install4j Support

# Install Runtime into Local Repository

Maven needs to have the install4j runtime jar in the local repository to build.

This is not yet in any available remote repository, so you'll need to install it manually before building:

    mvn -Dinstall4j.home=<path> -Pinstall-runtime

Where path is the location to where install4j has been installed, for example on Mac OSX:

    mvn -Dinstall4j.home="/Applications/install4j 5/" -Pinstall-runtime

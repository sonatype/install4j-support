<!--

    Copyright (c) 2007-2014 Sonatype, Inc. All rights reserved.

    This program is licensed to you under the Apache License Version 2.0,
    and you may not use this file except in compliance with the Apache License Version 2.0.
    You may obtain a copy of the Apache License Version 2.0 at http://www.apache.org/licenses/LICENSE-2.0.

    Unless required by applicable law or agreed to in writing,
    software distributed under the Apache License Version 2.0 is distributed on an
    "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the Apache License Version 2.0 for the specific language governing permissions and limitations there under.

-->
# install4j Slf4j Bridge

Bridges [Slf4j](http://slf4j.org) logging to install4j's log helpers (Util.log*).

<img src="../images/slf4j.png" style="float:right"/>

## Level Mapping

The install4j logging API only has _INFO_ and _ERROR_ level-like concepts.
The bridge will map slf4j levels as follows:

* TRACE uses Util.logInfo()
* DEBUG uses Util.logInfo()
* INFO uses Util.logInfo()
* WARN uses Util.logInfo()
* ERROR uses Util.logError()
* Throwables use Util.log()

See [com.install4j.api.Util](http://resources.ej-technologies.com/install4j/help/api/com/install4j/api/Util.html) for more details.

## Enable Debug Logging

Debug logging can be enabled by setting system properties:

    -Dorg.sonatype.install4j.slf4j.level=DEBUG

# TODO

Add more docs on configuration and usage.

<br style="clear:both"/>
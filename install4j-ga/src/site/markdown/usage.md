<!--

    Copyright (c) 2007-2012 Sonatype, Inc. All rights reserved.

    This program is licensed to you under the Apache License Version 2.0,
    and you may not use this file except in compliance with the Apache License Version 2.0.
    You may obtain a copy of the Apache License Version 2.0 at http://www.apache.org/licenses/LICENSE-2.0.

    Unless required by applicable law or agreed to in writing,
    software distributed under the Apache License Version 2.0 is distributed on an
    "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the Apache License Version 2.0 for the specific language governing permissions and limitations there under.

-->
# Usage

## References

[Event Tracking Guide](https://developers.google.com/analytics/devguides/collection/gajs/eventTrackerGuide)

## Install Extension Bundle

This extension uses [Slf4j](http://slf4j.org) for logging and will require the _slf4j-api_ and
a slf4j provider such as [install4j-slf4j](../install4j-slf4j/index.html).

Some additional libraries are also required.  To help configure install4j you can unzip the _install4j-ga-VERSION-bundle.zip_
into the extensions directory:

    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-dependency-plugin</artifactId>
                <executions>
                    <execution>
                        <id>install-extension</id>
                        <phase>pre-integration-test</phase>
                        <goals>
                            <goal>unpack</goal>
                        </goals>
                        <configuration>
                            <outputDirectory>${install4j.home}/extensions</outputDirectory>
                            <artifactItems>
                                <artifactItem>
                                    <groupId>org.sonatype.install4j</groupId>
                                    <artifactId>install4j-ga</artifactId>
                                    <classifier>bundle</classifier>
                                    <type>zip</type>
                                    <version>VERSION</version>
                                </artifactItem>
                            </artifactItems>
                        </configuration>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>

Besure to configure the proper __VERSION__ and configure the __install4j.home__ property.

## Actions

![image](images/actions.png)

### Configure Proxy

Configure HTTP proxy settings.  This will bridge the HTTP proxy configuration as detected by install4j.

This action should be placed before any other GA actions.

### Track Event

Track a single event.

At the moment only supports the following configuration:

* Tracking Code (string)
* Category (string)
* Action (string)
* Label (string)
* Value (integer)

### Track Event on Cancel

Track an event when user cancels installation.

Supports the same configuration as _Track Event_ but will automatically set the _Value_ to the progress percent completed when canceled.

This action should be placed early in the installer configuration, like in _Startup_.

## Disabling Functionality

You can disable all functionality of these events by setting the system property:

    -Dorg.sonatype.install4j.ga.GoogleAnalyticsActionSupport.disable=true

This is handy for testing so you don't send tracking events without having to disable the actions in install4j.

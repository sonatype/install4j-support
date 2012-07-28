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

## Dependencies

Install install4j 5.1.2+.  An local install4j installation is requied for this plugin to execute properly.

The recommended approache is to install the tool and then configure via _settings.xml_ the location where it was installed.

Update _settings.xml_ with local environmennt propeties:

    <profiles>
        <profile>
            <id>development</id>
            <properties>
                <install4j.home>CHANGEME</install4j.home>
            </properties>
        </profile>
    </profiles>

    <activeProfiles>
        <activeProfile>development</activeProfile>
    </activeProfiles>

Replacing __CHANGEME__ with the location where install4j has been installed on the current system.

## Project Configuration

Configure the _compile_ goal to execute:

    <build>
        <plugins>
            <plugin>
                <groupId>org.sonatype.install4j</groupId>
                <artifactId>install4j-maven-plugin</artifactId>
                <executions>
                    <execution>
                        <id>compile-installers</id>
                        <phase>package</phase>
                        <goals>
                            <goal>compile</goal>
                        </goals>
                        <configuration>
                            <projectFile>${project.basedir}/src/main/installer/myproject.install4j</projectFile>
                            <attach>true</attach>
                        </configuration>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>

For more details see the [Plugin Documentation](plugin-info.html).

## Signing

Update keystore passwords in _settings.xml_:

    <profiles>
        <profile>
            <id>development</id>
            <properties>
                <install4j.winKeystorePassword>CHANGEME</install4j.winKeystorePassword>
                <install4j.macKeystorePassword>CHANGEME</install4j.macKeystorePassword>
            </properties>
        </profile>
    </profiles>

    <activeProfiles>
        <activeProfile>development</activeProfile>
    </activeProfiles>

Replacing __CHANGEME__ with the appropriate keystore passwords.

## Installing License Key

Automated builds will need a valid license key configured for install4j to function properly.
You can configure the Maven build to automatically configure the license key with the _install-license_ goal.

It is recommended to configure this via _settings.xml_ and __NOT__ configure this value directly in you _pom.xml_.

Update license key in _settings.xml_:

    <profiles>
        <profile>
            <id>development</id>
            <properties>
                <install4j.licenseKey>CHANGEME</install4j.licenseKey>
            </properties>
        </profile>
    </profiles>

    <activeProfiles>
        <activeProfile>development</activeProfile>
    </activeProfiles>

Replacing __CHANGEME__ with the appropriate license key.

Configure the _install-license_ goal to exectute:

    <build>
        <plugins>
            <plugin>
                <groupId>org.sonatype.install4j</groupId>
                <artifactId>install4j-maven-plugin</artifactId>
                <executions>
                    <execution>
                        <id>compile-installers</id>
                        <phase>package</phase>
                        <goals>
                            <goal>install-license</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>

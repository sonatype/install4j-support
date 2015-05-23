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
# Usage

## References

* [Plugin Documentation](plugin-info.html)

## Requirements

A local install4j installation is requied for this plugin to execute properly.

This plugin requires at least install4j version 5.1.2.
This pluign will attempt to detect the version and may fail if the installed version is not compatible.

The recommended method is to install the tool and then configure via _settings.xml_ the location where it was installed.

Update _settings.xml_ with properties to configure the location where install4j has been pre-installed:

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

Replace __CHANGEME__ with the location where install4j has been installed on the current system.

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
                        </configuration>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>

Replace __${project.basedir}/src/main/installer/myproject.install4j__ with the install4j project file to compile.

If _install4j.home_ is not configured or is configured to an invalid or corrupted location, the plugin execution will skip.

## JVM Arguments

Additional JVM arguments can be set on the _install4jc_ process.  This can be useful to customize system properties
to control the compilers behavior.

This example increases the default connect and read timeouts:

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
                            <jvmArguments>
                                <arg>-Dinstall4j.connectTimeout=20000</arg>
                                <arg>-Dinstall4j.readTimeout=20000</arg>
                            </jvmArguments>
                        </configuration>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>

## Media Signing

Media signing generally requires passwords for keystore files.
This plugin can configure install4j with the appropriate keystore passwords for media file signing.

The keystore file location must be configured via the install4j IDE.

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
This goal needs to execute before any other install4j-maven-plugin goal.

It is recommended to configure this via _settings.xml_ and __NOT__ configure this value directly in your _pom.xml_.

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

Replace __CHANGEME__ with the appropriate license key.

Configure the _install-license_ goal to exectute:

    <build>
        <plugins>
            <plugin>
                <groupId>org.sonatype.install4j</groupId>
                <artifactId>install4j-maven-plugin</artifactId>
                <executions>
                    <execution>
                        <id>install-license</id>
                        <phase>package</phase>
                        <goals>
                            <goal>install-license</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>

## Attaching Media

The install4j compiled media files can be attached to the current Maven project when the _attach_ parameter is configured.

When media is attached, the compiled files will be installed into the local repository and will also be deployed.
The media files will be attached to the current project with the _id_ of media as the _classifier_.

Configure _custom ids_ for each media type to configure the _classifier_.

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

Replace __${project.basedir}/src/main/installer/myproject.install4j__ with the install4j project file to compile.

## Skipping Compilation

Compilation can be skipped by setting the _install4j.skip_ property:

    mvn -Dinstall4j.skip

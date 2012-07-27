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

Install install4j 5.1.2+

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

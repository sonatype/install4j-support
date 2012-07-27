# Description

Install4j Maven 2/3 Plugin

Compatible with install4j version 5.1.2 or higher.

# Usage

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

Configure the _compile_ goal to execute:

    <build>
        <plugins>
            <plugin>
                <groupId>org.sonatype.install4j</groupId>
                <artifactId>install4j-maven-plugin</artifactId>
                <version>1.0</version>
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

For more details see the [Plugin Documentation](http://sonatype.github.com/install4j-support/install4j-maven-plugin/plugin-info.html).

# TODO

Support pulling passwords from settings.xml w/master-password support
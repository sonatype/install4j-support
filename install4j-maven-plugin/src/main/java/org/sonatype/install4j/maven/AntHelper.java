/*
 * Copyright (c) 2007-2012 Sonatype, Inc. All rights reserved.
 *
 * This program is licensed to you under the Apache License Version 2.0,
 * and you may not use this file except in compliance with the Apache License Version 2.0.
 * You may obtain a copy of the Apache License Version 2.0 at http://www.apache.org/licenses/LICENSE-2.0.
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the Apache License Version 2.0 is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Apache License Version 2.0 for the specific language governing permissions and limitations there under.
 */
package org.sonatype.install4j.maven;

import org.apache.maven.plugin.Mojo;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectComponent;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.Chmod;
import org.apache.tools.ant.taskdefs.Mkdir;
import org.apache.tools.ant.taskdefs.Property;
import org.apache.tools.ant.types.Path;

import java.io.File;
import java.util.Iterator;
import java.util.Map;

/**
 * Ant helper.
 *
 * @since 1.0
 */
public class AntHelper
{
    private final Mojo owner;

    private final Log log;

    private final MavenProject project;

    private final Project ant;

    public AntHelper(final Mojo owner, final MavenProject project) {
        assert owner != null;
        assert project != null;

        this.owner = owner;
        this.log = owner.getLog();
        this.project = project;

        ant = new Project();
        ant.setBaseDir(project.getBasedir());
        initAntLogger(ant);
        ant.init();

        // Inherit properties from Maven
        inheritProperties();
    }

    private void initAntLogger(final Project ant) {
        MavenAntLoggerAdapter antLogger = new MavenAntLoggerAdapter(log);
        antLogger.setEmacsMode(true);
        antLogger.setOutputPrintStream(System.out);
        antLogger.setErrorPrintStream(System.err);

        if (log.isDebugEnabled()) {
            antLogger.setMessageOutputLevel(Project.MSG_VERBOSE);
        }
        else {
            antLogger.setMessageOutputLevel(Project.MSG_INFO);
        }

        ant.addBuildListener(antLogger);
    }

    private void inheritProperties() {
        // Propagate properties
        Map props = project.getProperties();
        Iterator iter = props.keySet().iterator();
        while (iter.hasNext()) {
            String name = (String) iter.next();
            String value = String.valueOf(props.get(name));
            setProperty(name, value);
        }

        // Hardcode a few
        setProperty("project.basedir", project.getBasedir());
    }

    public void setProperty(final String name, Object value) {
        assert name != null;
        assert value != null;

        String valueAsString = String.valueOf(value);

        if (log.isDebugEnabled()) {
            log.debug(String.format("Setting property: %s=%s", name, valueAsString));
        }

        Property prop = (Property) createTask("property");
        prop.setName(name);
        prop.setValue(valueAsString);
        prop.execute();
    }

    public String getProperty(final String name) {
        return ant.getProperty(name);
    }

    public Task createTask(final String name) throws BuildException {
        assert name != null;
        return ant.createTask(name);
    }

    public Path createPath(final File location) {
        Path path = new Path(ant);
        path.setLocation(location);
        return path;
    }

    public <T extends ProjectComponent> T createTask(final Class<T> type) {
        assert type != null;

        T task = null;
        try {
            task = type.newInstance();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
        task.setProject(ant);
        return task;
    }

    public void mkdir(final File dir) {
        assert dir != null;

        Mkdir mkdir = createTask(Mkdir.class);
        mkdir.setDir(dir);
        mkdir.execute();
    }

    public void chmod(final File dir, final String includes, final String perm) {
        Chmod chmod = createTask(Chmod.class);
        chmod.setDir(dir);
        chmod.setIncludes(includes);
        chmod.setPerm(perm);
        chmod.execute();
    }

    public void chmod(final File file, final String perm) {
        Chmod chmod = createTask(Chmod.class);
        chmod.setFile(file);
        chmod.setPerm(perm);
        chmod.execute();
    }
}
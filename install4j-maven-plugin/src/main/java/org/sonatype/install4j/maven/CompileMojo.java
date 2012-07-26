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

import org.apache.maven.project.MavenProjectHelper;
import org.apache.tools.ant.taskdefs.ExecTask;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Compile installers (via install4jc).
 *
 * @goal compile
 *
 * @since 1.0
 */
public class CompileMojo
    extends Install4jcMojoSupport
{
    /**
     * @parameter expression="${install4j.projectFile}"
     * @required
     */
    private File projectFile;

    /**
     * @parameter expression="${install4j.verbose}" default-value="false"
     */
    private boolean verbose;

    /**
     * @parameter expression="${install4j.quiet}" default-value="false"
     */
    private boolean quiet;

    /**
     * @parameter expression="${install4j.test}" default-value="false"
     */
    private boolean test;

    /**
     * @parameter expression="${install4j.debug}" default-value="false"
     */
    private boolean debug;

    /**
     * @parameter expression="${install4j.faster}" default-value="false"
     */
    private boolean faster;

    /**
     * @parameter expression="${install4j.disableSigning}" default-value="false"
     */
    private boolean disableSigning;

    /**
     * @parameter expression="${install4j.winKeystorePassword}"
     */
    private String winKeystorePassword;

    /**
     * @parameter expression="${install4j.macKeystorePassword}"
     */
    private String macKeystorePassword;

    /**
     * @parameter expression="${install4j.release}" default-value="${project.version}"
     */
    private String release;

    /**
     * @parameter expression="${install4j.destination}" default-value="${project.build.directory}/media"
     */
    private File destination;

    /**
     * @parameter expression="${install4j.buildSelected}" default-value="false"
     */
    private boolean buildSelected;

    /**
     * @parameter expression="${install4j.buildIds}"
     */
    private String buildIds;

    /**
     * @parameter expression="${install4j.mediaTypes}"
     */
    private String mediaTypes;

    /**
     * @parameter expression="${install4j.variableFile}"
     */
    private File variableFile;

    /**
     * @parameter expression="${install4j.attach}" default-value="true"
     */
    private boolean attach;

    /**
     * @component
     */
    private MavenProjectHelper projectHelper;

    @Override
    protected void execute(final AntHelper ant, final ExecTask task) throws Exception {
        task.createArg().setFile(projectFile);

        if (verbose) {
            task.createArg().setValue("--verbose");
        }

        if (quiet) {
            task.createArg().setValue("--quiet");
        }

        if (test) {
            task.createArg().setValue("--test");
        }

        if (debug) {
            task.createArg().setValue("--debug");
        }

        if (faster) {
            task.createArg().setValue("--faster");
        }

        if (disableSigning) {
            task.createArg().setValue("--disable-signing");
        }

        if (winKeystorePassword != null) {
            task.createArg().setValue("--win-keystore-password");
            task.createArg().setValue(winKeystorePassword);
        }

        if (winKeystorePassword != null) {
            task.createArg().setValue("--mac-keystore-password");
            task.createArg().setValue(macKeystorePassword);
        }

        task.createArg().setValue("--destination");
        task.createArg().setFile(destination);

        if (buildSelected) {
            task.createArg().setValue("--build-selected");
        }

        if (buildIds != null) {
            task.createArg().setValue("--build-ids");
            task.createArg().setValue(buildIds);
        }

        if (mediaTypes != null) {
            task.createArg().setValue("--media-types");
            task.createArg().setValue(mediaTypes);
        }

        if (variableFile != null) {
            task.createArg().setValue("--var-file");
            task.createArg().setFile(variableFile);
        }

        // TODO: Add support for -D variables

        task.execute();

        if (attach) {
            for (AttachedFile attachedFile : parseAttachedFiles()) {
                String type = getType(attachedFile.fileName);
                String classifier = attachedFile.classifier;
                File file = new File(destination, attachedFile.fileName);
                projectHelper.attachArtifact(project, type, classifier, file);
            }
        }
    }

    private String getType(final String fileName) {
        int i = fileName.lastIndexOf(".");
        return fileName.substring(i + 1, fileName.length());
    }

    private static class AttachedFile
    {
        public final String fileName;

        public final String classifier;

        private AttachedFile(final String fileName, final String classifier) {
            this.fileName = fileName;
            this.classifier = classifier;
        }
    }

    private List<AttachedFile> parseAttachedFiles() throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        File file = new File(destination, "updates.xml");
        if (!file.exists()) {
            log.warn("Missing updates.xml file: " + file);
            return Collections.emptyList();
        }

        Document doc = builder.parse(file);
        NodeList entries = doc.getElementsByTagName("entry");
        if (entries == null) {
            log.warn("Failed to parse updates.xml entries");
            return Collections.emptyList();
        }

        List<AttachedFile> files = new ArrayList<AttachedFile>(entries.getLength());
        for (int i=0; i < entries.getLength(); i++) {
            Element element = (Element) entries.item(i);
            AttachedFile attachedFile = new AttachedFile(
                element.getAttribute("fileName"),
                // FIXME: Sort out if this is correct or if newMediaFileId is better
                element.getAttribute("targetMediaFileId"));
            files.add(attachedFile);
        }

        return files;
    }
}
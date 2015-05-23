/*
 * Copyright (c) 2008-2015 Sonatype, Inc. All rights reserved.
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

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.SystemStreamLog;
import org.junit.Before;
import org.junit.Test;

import org.sonatype.sisu.litmus.testsupport.TestSupport;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * Tests for {@link VersionHelper}.
 */
public class VersionHelperTest
    extends TestSupport
{
  private VersionHelper helper;

  @Before
  public void setUp() throws Exception {
    helper = new VersionHelper(new SystemStreamLog());
  }

  @Test
  public void parseSingleVersion() {
    String input = "install4j version 5.1.3 (build 5521), built on 2012-09-21";

    String version = helper.parseVersion(input);
    assertThat(version, is("5.1.3"));
  }

  @Test
  public void parseMultiLineVersion() {
    String input = "testing JVM in /usr …\n" +
        "install4j version 5.1.3 (build 5521), built on 2012-09-21";

    String version = helper.parseVersion(input);
    assertThat(version, is("5.1.3"));
  }

  @Test
  public void parseMultiLineCrLFVersion() {
    String input = "testing JVM in /usr …\r\n" +
        "install4j version 5.1.3 (build 5521), built on 2012-09-21";

    String version = helper.parseVersion(input);
    assertThat(version, is("5.1.3"));
  }

  @Test
  public void parseMultiLineVersionWithExtraJunk() {
    String input = System.currentTimeMillis() + "\n" +
        System.currentTimeMillis() + "\n" +
        System.currentTimeMillis() + "\n" +
        System.currentTimeMillis() + "\n" +
        "testing JVM in /usr …\n" +
        "install4j version 5.1.3 (build 5521), built on 2012-09-21";

    String version = helper.parseVersion(input);
    assertThat(version, is("5.1.3"));
  }

  @Test
  public void ensureSameVersionCompatible() throws Exception {
    helper.ensureVersionCompatible("install4j version 5.1.2 XXX");
  }

  @Test
  public void ensureNewVersionCompatible() throws Exception {
    helper.ensureVersionCompatible("install4j version 5.1.3 XXX");
  }

  @Test(expected = MojoExecutionException.class)
  public void ensureOldVersionNotCompatible() throws Exception {
    helper.ensureVersionCompatible("install4j version 5.1.1 XXX");
  }
}

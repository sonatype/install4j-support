/*
 * Copyright (c) 2012-present Sonatype, Inc. All rights reserved.
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
package org.sonatype.install4j.ga;

/**
 * Action bean-info for {@link ConfigureProxyAction}.
 *
 * @since 1.0
 */
public class ConfigureProxyActionBeanInfo
    extends ActionBeanInfoSupport
{
  public ConfigureProxyActionBeanInfo() {
    super(
        "Configure Proxy",
        "Configure proxy for Google Analytics",
        CATEGORY_GA,
        SINGLE_INSTANCE,
        INSTALLED_FILES_NOT_REQUIRED,
        DEFAULT_SORT_KEY,
        ConfigureProxyAction.class
    );
  }
}
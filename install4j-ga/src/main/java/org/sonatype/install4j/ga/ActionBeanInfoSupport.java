/*
 * Copyright (c) 2007-2014 Sonatype, Inc. All rights reserved.
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

import com.install4j.api.beaninfo.ActionBeanInfo;
import com.install4j.api.beaninfo.BeanValidationException;
import com.install4j.api.beaninfo.BeanValidator;
import com.install4j.api.beans.Bean;

/**
 * Support for {@link ActionBeanInfo} instances.
 *
 * @since 1.0
 */
public abstract class ActionBeanInfoSupport
    extends ActionBeanInfo
    implements BeanValidator
{
  public static final String CATEGORY_GA = "Google Analytics";

  public static final Integer DEFAULT_SORT_KEY = null;

  public static final boolean MULTIPLE_INSTANCES = true;

  public static final boolean SINGLE_INSTANCE = false;

  public static final boolean INSTALLED_FILES_REQUIRED = true;

  public static final boolean INSTALLED_FILES_NOT_REQUIRED = false;

  protected ActionBeanInfoSupport(final String displayName,
                                  final String shortDescription,
                                  final String category,
                                  final boolean multipleInstancesSupported,
                                  final boolean installedFilesRequired,
                                  final Integer sortKey,
                                  final Class beanClass,
                                  final Class customizerClass)
  {
    super(displayName, shortDescription, category, multipleInstancesSupported, installedFilesRequired, sortKey,
        beanClass, customizerClass);
  }

  protected ActionBeanInfoSupport(final String displayName,
                                  final String shortDescription,
                                  final String category,
                                  final boolean multipleInstancesSupported,
                                  final boolean installedFilesRequired,
                                  final Integer sortKey,
                                  final Class beanClass)
  {
    super(displayName, shortDescription, category, multipleInstancesSupported, installedFilesRequired, sortKey,
        beanClass);
  }

  public void validateBean(final Bean bean) throws BeanValidationException {
    // empty
  }
}
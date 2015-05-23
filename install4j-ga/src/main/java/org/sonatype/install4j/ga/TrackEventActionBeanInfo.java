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
package org.sonatype.install4j.ga;

import com.install4j.api.beaninfo.BeanValidationException;
import com.install4j.api.beans.Bean;

import static com.install4j.api.beaninfo.Install4JPropertyDescriptor.create;

/**
 * Action bean-info for {@link TrackEventAction}.
 *
 * @since 1.0
 */
public class TrackEventActionBeanInfo
    extends ActionBeanInfoSupport
{
  private static final String PROPERTY_TRACKING_CODE = "trackingCode";

  private static final String PROPERTY_CATEGORY = "category";

  private static final String PROPERTY_ACTION = "action";

  private static final String PROPERTY_LABEL = "label";

  private static final String PROPERTY_VALUE = "value";

  public TrackEventActionBeanInfo() {
    super(
        "Track Event",
        "Send a Google Analytics tracking event",
        CATEGORY_GA,
        MULTIPLE_INSTANCES,
        INSTALLED_FILES_NOT_REQUIRED,
        DEFAULT_SORT_KEY,
        TrackEventAction.class
    );

    addPropertyDescriptor(create(
        PROPERTY_TRACKING_CODE,
        getBeanClass(),
        "Tracking Code",
        "Tracking code"
    ));

    addPropertyDescriptor(create(
        PROPERTY_CATEGORY,
        getBeanClass(),
        "Category",
        "Event category"
    ));

    addPropertyDescriptor(create(
        PROPERTY_ACTION,
        getBeanClass(),
        "Action",
        "Event action"
    ));

    addPropertyDescriptor(create(
        PROPERTY_LABEL,
        getBeanClass(),
        "Label",
        "Event label"
    ));

    addPropertyDescriptor(create(
        PROPERTY_VALUE,
        getBeanClass(),
        "Value",
        "Event value"
    ));
  }

  @Override
  public void validateBean(final Bean bean) throws BeanValidationException {
    checkNotEmpty(PROPERTY_TRACKING_CODE, bean);
    checkNotEmpty(PROPERTY_CATEGORY, bean);
    checkNotEmpty(PROPERTY_ACTION, bean);
    // label is optional
    // value is optional
  }
}
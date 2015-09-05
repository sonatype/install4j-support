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

import com.install4j.api.context.Context;

/**
 * Google Analytics event tracking action.
 *
 * @since 1.0
 */
public class TrackEventAction
    extends GoogleAnalyticsActionSupport
{
  private String category;

  private String action;

  private String label;

  private Integer value;

  public String getCategory() {
    return category;
  }

  public void setCategory(final String category) {
    this.category = category;
  }

  public String getAction() {
    return action;
  }

  public void setAction(final String action) {
    this.action = action;
  }

  public String getLabel() {
    return label;
  }

  public void setLabel(final String label) {
    this.label = label;
  }

  public Integer getValue() {
    return value;
  }

  public void setValue(final Integer value) {
    this.value = value;
  }

  @Override
  protected boolean execute(final Context context) throws Exception {
    if (isDisabled()) {
      log.debug("Tracking disabled; ignoring event: {}", action);
    }
    else {
      trackEvent(context);
    }
    return true;
  }

  private void trackEvent(final Context context) throws Exception {
    log.debug("Tracking event: {}", action);
    getTracker().trackEvent(category, action, label, value);
  }
}
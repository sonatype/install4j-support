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

package org.sonatype.install4j.ga;

import java.util.HashMap;
import java.util.Map;

import com.dmurph.tracking.AnalyticsConfigData;
import com.dmurph.tracking.JGoogleAnalyticsTracker;
import com.dmurph.tracking.JGoogleAnalyticsTracker.DispatchMode;
import com.dmurph.tracking.JGoogleAnalyticsTracker.GoogleAnalyticsVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Factory to create trackers.
 *
 * Allows more than one task implementation to share the same tracker instance for a specific tracking code.
 *
 * @see <a href="https://code.google.com/p/jgoogleanalyticstracker">jgoogleanalyticstracker</a>
 * @since 1.0
 */
public class TrackerFactory
{
  private static final Logger log = LoggerFactory.getLogger(TrackerFactory.class);

  private final Map<String, JGoogleAnalyticsTracker> trackers = new HashMap<String, JGoogleAnalyticsTracker>();

  /**
   * Create a new tracker for the given tracking code.
   */
  public JGoogleAnalyticsTracker create(final String code) {
    assert code != null;
    AnalyticsConfigData config = new AnalyticsConfigData(code);
    JGoogleAnalyticsTracker tracker = new JGoogleAnalyticsTracker(config, GoogleAnalyticsVersion.V_4_7_2,
        DispatchMode.SYNCHRONOUS);
    trackers.put(code, tracker);
    log.debug("Created tracker: {} -> {}", code, tracker);
    return tracker;
  }

  /**
   * Lookup an existing tracker for the given tracking code, if one does not exist, it is created and cached.
   */
  public JGoogleAnalyticsTracker get(final String code) {
    assert code != null;
    JGoogleAnalyticsTracker tracker = trackers.get(code);
    if (tracker == null) {
      tracker = create(code);
    }
    return tracker;
  }
}
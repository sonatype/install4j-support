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

import com.install4j.api.Util;
import com.install4j.api.actions.AbstractInstallOrUninstallAction;
import com.install4j.api.context.Context;
import com.install4j.api.context.InstallerContext;
import com.install4j.api.context.UninstallerContext;
import com.install4j.api.context.UserCanceledException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Support for actions.
 *
 * @since 1.0
 */
public abstract class ActionSupport
    extends AbstractInstallOrUninstallAction
{
    protected final Logger log = LoggerFactory.getLogger(getClass());

    protected boolean reportFailure = true;

    protected static boolean getFlag(final Class type, final String name) {
        return Boolean.getBoolean(type.getName() + "." + name);
    }

    protected boolean getFlag(final String name) {
        return getFlag(getClass(), name);
    }

    private void reportFailure(final Context context, final Exception e) {
        log.error("Action execution failed", e);

        if (reportFailure) {
            // FIXME: Sort out how to contribute i18n for an extension/module
            Util.showErrorMessage(context.getMessage("ActionFailed", new Object[] { e }));
        }
    }

    @Override
    public boolean install(final InstallerContext context) throws UserCanceledException {
        try {
            return execute(context);
        }
        catch (Exception e) {
            reportFailure(context, e);
            return false;
        }
    }

    @Override
    public boolean uninstall(final UninstallerContext context) throws UserCanceledException {
        try {
            return execute(context);
        }
        catch (Exception e) {
            reportFailure(context, e);
            return false;
        }
    }

    protected abstract boolean execute(Context context) throws Exception;
}
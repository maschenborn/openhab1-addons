/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.persistence.autoremote.internal;

import flexjson.JSONContext;
import flexjson.transformer.AbstractTransformer;

/**
 * This class defines what kind of content should be written for a {@link AutoremoteEventBean}.
 *
 * @author Kai Kreuzer
 * @since 1.0.0
 */
public class AutoremoteEventTransformer extends AbstractTransformer {

    @Override
    public void transform(Object obj) {
        if (obj instanceof AutoremoteEventBean) {
            AutoremoteEventBean autoremoteEventBean = (AutoremoteEventBean) obj;
            JSONContext context = getContext();
            context.writeOpenObject();
            context.writeName("feed_id");
            context.write("" + autoremoteEventBean.getFeedId());
            context.writeComma();
            context.writeName("value");
            context.write("\"" + autoremoteEventBean.getValue() + "\"");
            context.writeCloseObject();
        }
    }

}

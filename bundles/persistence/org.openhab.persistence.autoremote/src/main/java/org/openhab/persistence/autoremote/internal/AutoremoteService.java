/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.persistence.autoremote.internal;

import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.openhab.core.items.Item;
import org.openhab.core.persistence.PersistenceService;
import org.openhab.io.net.http.HttpUtil;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import flexjson.JSONSerializer;

/**
 * This is the implementation of the AutoRemote {@link PersistenceService}. To learn
 * more about AutoRemote please visit their <a href="http://xxx/">website</a>.
 *
 * @author Michael Aschenborn
 * @author Thomas.Eichstaedt-Engelen
 * @author Kai Kreuzer
 * @since 1.0.0
 */
public class AutoremoteService implements PersistenceService {

    private static final Logger logger = LoggerFactory.getLogger(AutoremoteService.class);

    private String apiKey;
    private String url;

    private final static String DEFAULT_EVENT_URL = "https://autoremotejoaomgcd.appspot.com/sendmessage?key=";
    private boolean initialized = false;

    /**
     * @{inheritDoc}
     */
    @Override
    public String getName() {
        return "autoremote";
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public void store(Item item, String alias) {
        if (initialized) {
            JSONSerializer serializer = new JSONSerializer().transform(new AutoremoteEventTransformer(),
                    AutoremoteEventBean.class);
            String serializedBean = serializer.serialize(new AutoremoteEventBean(alias, item.getState().toString()));

            String serviceUrl = url + apiKey;
            String response = HttpUtil.executeUrl("POST", serviceUrl, IOUtils.toInputStream(serializedBean),
                    "application/json", 5000);
            logger.debug("Stored item '{}' as '{}' in AutoRemote and received response: {} ",
                    new String[] { item.getName(), alias, response });
        }
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public void store(Item item) {
        throw new UnsupportedOperationException(
                "The AutoRemote service requires aliases for persistence configurations that should match the feed id");
    }

    /**
     * @{inheritDoc}
     */
    public void activate(final BundleContext bundleContext, final Map<String, Object> config) {
        if (config != null) {

            url = (String) config.get("url");
            if (StringUtils.isBlank(url)) {
                url = DEFAULT_EVENT_URL;
            }

            apiKey = (String) config.get("apikey");
            if (StringUtils.isBlank(apiKey)) {
                logger.warn("The AutoRemote API-Key is missing - please configure it in openhab.cfg");
            }

            initialized = true;
        }
    }

}

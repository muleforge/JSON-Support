/*
 * $Id$
 * --------------------------------------------------------------------------------------
 * Copyright (c) MuleSource, Inc.  All rights reserved.  http://www.mulesource.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.module.json.transformers.wire;

import org.mule.DefaultMuleMessage;
import org.mule.module.json.transformers.JsonToObject;
import org.mule.module.json.transformers.ObjectToJson;
import org.mule.transformer.wire.TransformerPairWireFormat;

import net.sf.json.JsonConfig;

/**
 * TODO ignore this class it does not work
 * Serializes objects using JSON encoding. This is equivelent of using the {@link org.mule.module.json.transformers.ObjectToJson} and
 * {@link org.mule.module.json.transformers.JsonToObject}.  this means you have the same configuration options for the wire firemat as
 * the Json transformers.
 */
public class JsonWireFormat extends TransformerPairWireFormat
{
    public JsonWireFormat()
    {
        this(new JsonConfig(), null, null);
    }

    public JsonWireFormat(JsonConfig config, String inclusions, String exclusions)
    {
        if (config == null)
        {
            throw new IllegalArgumentException("jsonConfig cannot be null");
        }

        JsonToObject in = new JsonToObject();
        in.setJsonConfig(config);
        in.setReturnClass(DefaultMuleMessage.class);
        setInboundTransformer(in);

        ObjectToJson out = new ObjectToJson();
        out.setJsonConfig(config);
        out.setExcludeProperties(exclusions);
        out.setIncludeProperties(inclusions);
        setOutboundTransformer(out);
    }


}

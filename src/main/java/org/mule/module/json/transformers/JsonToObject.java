/*
 * $Id$
 * --------------------------------------------------------------------------------------
 * Copyright (c) MuleSource, Inc.  All rights reserved.  http://www.mulesource.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.module.json.transformers;

import org.mule.config.i18n.CoreMessages;
import org.mule.transformer.AbstractTransformer;
import org.mule.api.transformer.TransformerException;
import org.mule.module.json.util.JsonUtils;

import java.util.HashMap;

import net.sf.json.JSONObject;
import net.sf.json.JSON;
import net.sf.json.JSONSerializer;

import org.apache.commons.beanutils.DynaBean;

/**
 * TODO
 */
public class JsonToObject extends AbstractTransformer
{
    public JsonToObject()
    {
        this.registerSourceType(JSONObject.class);
        this.registerSourceType(String.class);
        this.registerSourceType(byte[].class);
    }

    protected Object doTransform(Object src, String encoding) throws TransformerException
    {
        try
        {
            ObjectBean objectBean = null;
            Object returnValue = null;

            if (src instanceof byte[])
            {
                src = new String((byte[])src, encoding);
            }

            if (src instanceof String)
            {
                if(getReturnClass().equals(DynaBean.class))
                {
                    JSON json = JSONSerializer.toJSON(src.toString());
                    returnValue = JSONObject.toBean((JSONObject) json);
                }
                else
                {
                    objectBean = (ObjectBean) JsonUtils.convertJsonToBean((String) src, ObjectBean.class);
                }
            }
            else if(src instanceof JSONObject){
                objectBean = (ObjectBean) JSONObject.toBean((JSONObject) src, ObjectBean.class, new HashMap());
            }

            if(objectBean!=null)
            {
                if(objectBean.getObject() instanceof DynaBean){
                    returnValue = JsonUtils.getObjectFromDynaBean(objectBean.getType(), (DynaBean) objectBean.getObject());
                }
                else{
                    returnValue = objectBean.getObject();
                }
            }
            return returnValue;
        }
        catch (Exception e)
        {
            throw new TransformerException(CoreMessages.transformFailed( "json", getReturnClass().getName()), this, e);
        }
    }

    
}

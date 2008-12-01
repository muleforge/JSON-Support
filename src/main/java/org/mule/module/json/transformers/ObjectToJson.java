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

import org.mule.module.json.util.JsonUtils;
import org.mule.transformer.AbstractTransformer;
import org.mule.transformer.AbstractMessageAwareTransformer;
import org.mule.api.transformer.TransformerException;
import org.mule.api.MuleMessage;

import java.util.List;
import java.util.ArrayList;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

/**
 * TODO
 */
public class ObjectToJson extends AbstractMessageAwareTransformer
{
    /**
     * logger used by this class
     */
    protected transient final Log logger = LogFactory.getLog(ObjectToJson.class);

    private boolean handleException = false;

    public ObjectToJson(){
        this.registerSourceType(Object.class);
        this.setReturnClass(String.class);
    }

    public boolean isAcceptNull()
    {
        return true;
    }

    public Object transform(MuleMessage message, String s) throws TransformerException
    {
        ObjectBean object = new ObjectBean();
        Object src = message.getPayload();

        // Checks if there's an exception
        if (message.getExceptionPayload() != null && this.isHandleException())
        {
            if (logger.isDebugEnabled())
            {
                logger.debug("Found exception with null payload");
            }
            src = this.getException(message.getExceptionPayload().getException());
        }

        object.setType((src == null) ? null : src.getClass().getName());
        object.setObject(src);

        if (logger.isDebugEnabled())
        {
            logger.debug("Converting payload " + src + " to " + String.class);
        }

        String returnValue = JsonUtils.convertJavaObjectToJson(object);

        if (logger.isDebugEnabled())
        {
            logger.debug("Successfully converted to value: " + returnValue);
        }

        return returnValue;
    }

    /**
     * The reason of having this is because the original exception object is way too
     * complex and it breaks JSON-lib.
     *
     * @return
     */
    private Exception getException(Throwable t)
    {

        Exception returnValue = null;
        List causeStack = new ArrayList();

        for (Throwable tempCause = t; tempCause != null; tempCause = tempCause.getCause())
        {
            causeStack.add(tempCause);
        }

        for (int i = causeStack.size() - 1; i >= 0; i--)
        {
            Throwable tempCause = (Throwable) causeStack.get(i);

            // There is no cause at the very root
            if (i == causeStack.size())
            {
                returnValue = new Exception(tempCause.getMessage());
                returnValue.setStackTrace(tempCause.getStackTrace());
            }
            else
            {
                returnValue = new Exception(tempCause.getMessage(), returnValue);
                returnValue.setStackTrace(tempCause.getStackTrace());
            }
        }

        return returnValue;
    }

    // Getter/Setter
    // -------------------------------------------------------------------------
    public boolean isHandleException()
    {
        return this.handleException;
    }

    public void setHandleException(boolean handleException)
    {
        this.handleException = handleException;
    }
}

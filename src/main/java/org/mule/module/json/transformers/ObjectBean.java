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

/**
 * OBject wrapper
 */
public class ObjectBean
{
    private String type = null;
    private java.lang.Object object = null;

    public String getType()
    {
        return this.type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public java.lang.Object getObject()
    {
        return this.object;
    }

    public void setObject(java.lang.Object src)
    {
        this.object = src;
    }
}

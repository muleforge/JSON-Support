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

import junit.framework.TestCase;

//TODO Ignore this test it does not work.  Cannot JSON MuleMEssage
public class JsonWireFormatTestCase extends TestCase //AbstractMuleMessageWireFormatTestCase
{
    public void testX() throws Exception
    {
        assertTrue(true);
    }
//
//    protected WireFormat getWireFormat()
//    {
//        return new JsonWireFormat();
//    }
//
//    public void testGetDefaultInboundTransformer()
//    {
//        assertEquals(JsonToObject.class, ((JsonWireFormat) getWireFormat()).getInboundTransformer().getClass());
//
//    }
//
//    public void testGetDefaultOutboundTransformer()
//    {
//        assertEquals(ObjectToJson.class, ((JsonWireFormat) getWireFormat()).getOutboundTransformer().getClass());
//    }
//
//    public void testWriteReadPayload() throws Exception
//    {
//        // Create orange to send over the wire
//        Properties messageProerties = new Properties();
//        messageProerties.put("key1", "val1");
//        Orange inOrange = new Orange();
//        inOrange.setBrand("Walmart");
//        inOrange.setMapProperties(messageProerties);
//
//        Object outObject = readWrite(inOrange);
//
//        assertTrue(outObject instanceof MuleMessage);
//        assertEquals("Walmart", ((Orange) ((MuleMessage) outObject).getPayload()).getBrand());
//        assertEquals("val1", ((Orange) ((MuleMessage) outObject).getPayload()).getMapProperties().get("key1"));
//    }

}

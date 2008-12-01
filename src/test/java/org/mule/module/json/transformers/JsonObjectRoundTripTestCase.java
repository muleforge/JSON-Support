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

import org.mule.transformer.AbstractTransformerTestCase;
import org.mule.module.json.TestBean;
import org.mule.module.json.util.JsonUtils;
import org.mule.api.transformer.Transformer;
import org.mule.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Iterator;

public class JsonObjectRoundTripTestCase extends AbstractTransformerTestCase
{

    public void testTransformBean() throws Exception
    {


        TestBean testBean = new TestBean("json", 23, 2.2, "function(i){ return i; }");
        ObjectToJson transformer = new ObjectToJson();
        String transformed = (String) transformer.transform(testBean);

        TestBean testBean2 = (TestBean) JsonUtils.getObjectFromJsonString(transformed);
        assertEquals("json", testBean2.getName());
        assertEquals(23, testBean2.getId());
        assertEquals(2.2, testBean2.getDoublev());
        assertEquals("function(i){ return i; }", testBean2.getFunc1());
    }

    public void testTransformList() throws Exception
    {
        String[] list = new String[]{"foo", "bar"};
        ObjectToJson transformer = new ObjectToJson();
        String transformed = (String) transformer.transform(list);

        Object[] list2 = (Object[]) JsonUtils.getObjectFromJsonString(transformed);

        assertNotNull(list2);
        assertTrue(list2.getClass().isArray());
        assertEquals("foo", list2[0]);
        assertEquals("bar", list2[1]);
    }

    public Transformer getTransformer() throws Exception
    {
        return new ObjectToJson();
    }

    public Object getTestData()
    {
        TestBean testBean = new TestBean("json", 23, 2.2, "function(i){ return i; }");
        return testBean;
    }

    public Object getResultData()
    {
        return "{\"object\":{\"func1\":function(i){ return i; },\"name\":\"json\",\"id\":23,\"options\":[],\"doublev\":2.2},\"type\":\"org.mule.module.json.TestBean\"}";
    }

    public Transformer getRoundTripTransformer() throws Exception
    {
       return new JsonToObject();
    }

}

package org.mule.module.json.transformers;

import org.mule.tck.AbstractMuleTestCase;
import org.mule.api.transformer.TransformerException;
import org.mule.module.json.TestBean;

public class DynaBeanToJsonTestCase extends AbstractMuleTestCase
{

    private final String JSON_RESULT = "{\"object\":{\"func1\":function(i){ return i; },\"name\":\"json\",\"id\":23,\"options\":[],\"doublev\":2.2},\"type\":\"org.mule.module.json.TestBean\"}";
	ObjectToJson transformer;

    protected void doSetUp() throws Exception
	{
		transformer = new ObjectToJson();
	}
	
	public void testTransform() throws Exception
	{
		transformer.setReturnClass(String.class);
	    TestBean bean = new TestBean("json", 23, 2.2, "function(i){ return i; }");
		String trasfRes = (String) transformer.transform(bean);
		assertEquals(JSON_RESULT, trasfRes);
	}
}

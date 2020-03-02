package com.teamium.domain.utils;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import com.teamium.domain.prod.resources.functions.RatedFunction;

public class FunctionClassAdapter extends XmlAdapter<String, Class<? extends RatedFunction>> {

	@Override
	public String marshal(Class<? extends RatedFunction> c) throws Exception {
		return c.getName();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Class<? extends RatedFunction> unmarshal(String c) throws Exception {
		return (Class<? extends RatedFunction>) Class.forName(c);
	}


}

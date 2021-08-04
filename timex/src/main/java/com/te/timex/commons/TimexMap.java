package com.te.timex.commons;

import java.util.HashMap;

@SuppressWarnings("rawtypes")
public class TimexMap extends HashMap {
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	public Object put(Object key, Object value) {
		return super.put(((String)key).toLowerCase(), value);
	}
}

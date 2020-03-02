package com.teamium.domain.utils;

import java.util.Locale;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class LocaleAdapter extends XmlAdapter<String, Locale> {


	/**
	 * Convertit un objet en XML
	 */
	@Override
	public String marshal(Locale locale) throws Exception {
		try{
			return locale.getLanguage();
		}
		catch(NullPointerException e){
			return null;
		}
	}

	/**
	 * Convertit l'expression xml en objet
	 * @param xml
	 */
	@Override
	public Locale unmarshal(String xml) throws Exception {
		try{
			return new Locale(xml.substring(0, 2));
		}
		catch(NullPointerException e){}
		return null;
	}

}

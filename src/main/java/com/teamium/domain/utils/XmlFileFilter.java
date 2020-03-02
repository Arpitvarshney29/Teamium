/**
 * 
 */
package com.teamium.domain.utils;

import java.io.File;
import java.io.FileFilter;

/**
 * FileFilter permettant de n'accepter que les fichiers se terminant par .xml et les sous-dossiers
 * @author JS
 *
 */
public class XmlFileFilter implements FileFilter {
	
	/**
	 * Constructeur
	 */
	public XmlFileFilter(){
	}

	/**
	 * 
	 * @see java.io.FileFilter#accept(java.io.File)
	 */
	@Override
	public boolean accept(File file) {
		try{
			return file.getName().endsWith(".xml") || file.isDirectory();
		}
		catch(Exception e){
			return false;
		}
	}

}

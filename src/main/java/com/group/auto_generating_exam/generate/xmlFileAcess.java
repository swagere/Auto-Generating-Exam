package com.group.auto_generating_exam.generate;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Vector;



/**
 * @author liuzeheng 15104591449
 * @modify
 * @version 1.0 2010-04
 * @description
 */
public class xmlFileAcess {

	/**
	 * @param args
	 */

	private  XMLEncoder writeOut=null;

	public static XMLEncoder getWriteOut(String filePath) {
		XMLEncoder writeOut = null;
		try {
			writeOut = new XMLEncoder(new BufferedOutputStream(
					new FileOutputStream(filePath)));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return writeOut;
	}

	private static Object getObject(String filePath) {
		XMLDecoder d = null;
		Object object = null;
		try {
			d = new XMLDecoder(new BufferedInputStream(new FileInputStream(
					filePath)));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		object = d.readObject();
		d.close();
		return object;
	}

	public static Vector<Question> getObjects(String objs_path) {
		Vector<Question> objs = new Vector<Question>();
		String[] DataFileNames = new File(objs_path).list();
		for (int i = 0; i < DataFileNames.length; i++) {
			objs.add((Question) getObject(objs_path + "/" + DataFileNames[i]));
		}
		return objs;
	}
}

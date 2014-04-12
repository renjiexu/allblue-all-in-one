package org.allblue.util;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;
import javax.xml.xpath.XPathConstants;

import org.allblue.R;
import org.allblue.activity.GlobalActivity;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

import android.app.Activity;

public final class XMLUtil {
	
	private static String run(String expression) {
		XPath xpath = XPathFactory.newInstance().newXPath();
		InputSource source = new InputSource(GlobalActivity.getGlobalContext().getResources().openRawResource(R.raw.account_api));
		
		try {
			Element node = (Element) xpath.evaluate(expression, source, XPathConstants.NODE);
			return node.getTextContent();
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}
	
	public static String getKey(String accountName) {
		String expression = "//accounts/account[@name='" + accountName + "']/authenticate/key";
		return run(expression);
	}
	
	public static String getValue(String accountName) {
		String expression = "//accounts/account[@name='" + accountName + "']/authenticate/value";
		return run(expression);
	}
	
	public static String getRequestToken(String accountName) {
		String expression = "//accounts/account[@name='" + accountName + "']/authenticate/request_token";
		return run(expression);
	}
	
	public static String getAccessToken(String accountName) {
		String expression = "//accounts/account[@name='" + accountName + "']/authenticate/access_token";
		return run(expression);
	}
	
	public static String getAuthorized(String accountName) {
		String expression = "//accounts/account[@name='" + accountName + "']/authenticate/authorized";
		return run(expression);
	}
	
	public static String getHomeline(String accountName) {
		String expression = "//accounts/account[@name='" + accountName + "']/api/home_timeline";
		return run(expression);
	}
	
	public static String getVerifyCredentials(String accountName) {
		String expression = "//accounts/account[@name='" + accountName + "']/api/verify_credentials";
		return run(expression);
	}
	
	public static String getUpdate(String accountName) {
		String expression = "//accounts/account[@name='" + accountName + "']/api/update";
		return run(expression);
	}
	
	public static String getUserProfile(String accountName) {
		String expression = "//accounts/account[@name='" + accountName + "']/api/user_profile";
		return run(expression);
	}
}

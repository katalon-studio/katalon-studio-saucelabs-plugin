package com.kms.katalon.keyword.saucelabs

import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.checkpoint.Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.keyword.CustomProfile
import com.kms.katalon.core.keyword.IActionProvider
import com.kms.katalon.core.keyword.IContext
import com.kms.katalon.core.keyword.IControlSelectionEventHandler
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.setting.BundleSettingStore
import com.kms.katalon.core.testcase.TestCase
import com.kms.katalon.core.testdata.TestData
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.driver.WebUIDriverType
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import internal.GlobalVariable

public class SauceLabsGenerateProfileButtonSelectionEventHandler implements IControlSelectionEventHandler {

	void handle(IActionProvider actionProvider, Map<String, Object> dataFields, IContext context) {
		CustomProfile webProfile = new CustomProfile();
		def webCustomCapabilities = [:];
		String webProfileName = SauceLabsUtils.SAUCE_LABS_RUN_CONFIG_NAME + "_web_" + dataFields.getOrDefault("web_profileName", "default");

		webCustomCapabilities['username'] = dataFields.getOrDefault("username", "");
		webCustomCapabilities['accessKey'] = dataFields.getOrDefault("accessKey", "");
		webCustomCapabilities['browserName'] = dataFields.getOrDefault("web_browserName", "");
		webCustomCapabilities['platform'] = dataFields.getOrDefault("web_platform", "");
		webCustomCapabilities['version'] = dataFields.getOrDefault("web_version", "");
		webCustomCapabilities['name'] = dataFields.getOrDefault("web_name", "");
		webCustomCapabilities['remoteWebDriverType'] = 'Selenium';
		webCustomCapabilities['remoteWebDriverUrl'] = 'http://ondemand.saucelabs.com:80/wd/hub';

		webProfile.setName(webProfileName);
		webProfile.setDriverType("Remote");
		webProfile.setDesiredCapabilities(webCustomCapabilities);

		actionProvider.saveCustomProfile(webProfile);

		CustomProfile mobileProfile = new CustomProfile();
		def mobileCustomCapabilities = [:];
		String mobileProfileName = SauceLabsUtils.SAUCE_LABS_RUN_CONFIG_NAME  + "_mobile_" + dataFields.getOrDefault("mobile_profileName", "default");

		mobileCustomCapabilities['username'] = dataFields.getOrDefault("username", "");
		mobileCustomCapabilities['accessKey'] = dataFields.getOrDefault("accessKey", "");
		mobileCustomCapabilities['appiumVersion'] = dataFields.getOrDefault("mobile_appiumVersion", "");
		mobileCustomCapabilities['browserName'] = dataFields.getOrDefault("mobile_browserName", "");
		mobileCustomCapabilities['deviceName'] = dataFields.getOrDefault("mobile_deviceName", "");
		mobileCustomCapabilities['platformName'] = dataFields.getOrDefault("mobile_platformName", "");
		mobileCustomCapabilities['platformVersion'] = dataFields.getOrDefault("mobile_platformVersion", "");
		mobileCustomCapabilities['app'] = dataFields.getOrDefault("mobile_app", "");
		mobileCustomCapabilities['name'] = dataFields.getOrDefault("mobile_name", "");
		mobileCustomCapabilities['remoteWebDriverType'] = 'Selenium';
		mobileCustomCapabilities['remoteWebDriverUrl'] = 'http://ondemand.saucelabs.com:80/wd/hub';

		mobileProfile.setName(mobileProfileName);
		mobileProfile.setDriverType("Remote");
		mobileProfile.setDesiredCapabilities(mobileCustomCapabilities);

		actionProvider.saveCustomProfile(mobileProfile);
	}
}

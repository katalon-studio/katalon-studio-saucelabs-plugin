package com.kms.katalon.keyword.saucelabs

import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import org.json.JSONArray
import org.json.JSONObject

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.checkpoint.Checkpoint
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.setting.BundleSettingStore
import com.kms.katalon.core.testcase.TestCase
import com.kms.katalon.core.testdata.TestData
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.RequestObject
import com.kms.katalon.core.testobject.ResponseObject
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.TestObjectProperty
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import groovy.json.JsonOutput

import com.kms.katalon.core.testobject.RestRequestObjectBuilder

import internal.GlobalVariable

public class SauceLabsUtils {
	public static String SAUCE_LABS_RUN_CONFIG_NAME = "saucelabs_";

	public static String getAllJobs(boolean full) {

		String projectLocation = RunConfiguration.getProjectDir();
		BundleSettingStore store = new BundleSettingStore(projectLocation, "com.kms.katalon.keyword.Saucelabs-keywords", true);
		String username = store.getString("username", "");
		String accessKey = store.getString("accessKey", "");

		String auth = "${username}:${accessKey}";
		String endpoint = "https://saucelabs.com/rest/v1/${username}/jobs";

		endpoint += full ? "?full=true" : "";

		String requestMethod = "GET";
		byte[] authEncBytes = Base64.getEncoder().encode(auth.getBytes());
		String authStringEnc = new String(authEncBytes);

		TestObjectProperty header1 = new TestObjectProperty("Authorization", ConditionType.EQUALS, "Basic " + authStringEnc);
		TestObjectProperty header2 = new TestObjectProperty("Accept", ConditionType.EQUALS, "application/json");
		ArrayList defaultHeaders = Arrays.asList(header1, header2);

		def builder = new RestRequestObjectBuilder();
		def requestObject = builder
				.withRestRequestMethod(requestMethod)
				.withRestUrl(endpoint)
				.withHttpHeaders(defaultHeaders)
				.build();

		ResponseObject respObj = WS.sendRequest(requestObject);
		return respObj.getResponseText();
	}

	public static String getLatestJobId() {
		def allJobIdsText = SauceLabsUtils.getAllJobs(false);
		JSONArray jsonArray = new JSONArray(allJobIdsText);
		return jsonArray.getJSONObject(0).getString("id");
	}

	public static String updateJob(String jobId, Map argsMap) {
		String projectLocation = RunConfiguration.getProjectDir();
		BundleSettingStore store = new BundleSettingStore(projectLocation, "com.kms.katalon.keyword.Saucelabs-keywords", true);
		String username = store.getString("username", "");
		String accessKey = store.getString("accessKey", "");

		String auth = "${username}:${accessKey}";
		String endpoint = "https://saucelabs.com/rest/v1/${username}/jobs/" + jobId;
		String requestMethod = "PUT";
		byte[] authEncBytes = Base64.getEncoder().encode(auth.getBytes());
		String authStringEnc = new String(authEncBytes);

		TestObjectProperty header1 = new TestObjectProperty("Authorization", ConditionType.EQUALS, "Basic " + authStringEnc);
		TestObjectProperty header2 = new TestObjectProperty("Content-Type", ConditionType.EQUALS, "application/json");
		TestObjectProperty header3 = new TestObjectProperty("Accept", ConditionType.EQUALS, "application/json");
		ArrayList defaultHeaders = Arrays.asList(header1, header2, header3);

		def textBodyContent = JsonOutput.toJson(argsMap);

		def builder = new RestRequestObjectBuilder();
		def requestObject = builder
				.withRestRequestMethod(requestMethod)
				.withRestUrl(endpoint)
				.withHttpHeaders(defaultHeaders)
				.withTextBodyContent(textBodyContent)
				.build();

		ResponseObject respObj = WS.sendRequest(requestObject);
		return respObj.getResponseText();
	}
}

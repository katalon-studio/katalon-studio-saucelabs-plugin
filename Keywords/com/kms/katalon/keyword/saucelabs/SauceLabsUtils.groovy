package com.kms.katalon.keyword.saucelabs

import org.json.JSONArray

import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.logging.KeywordLogger
import com.kms.katalon.core.setting.BundleSettingStore
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.FormDataBodyParameter
import com.kms.katalon.core.testobject.ResponseObject
import com.kms.katalon.core.testobject.RestRequestObjectBuilder
import com.kms.katalon.core.testobject.TestObjectProperty
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS

import groovy.json.JsonOutput

public class SauceLabsUtils {
	public static String SAUCE_LABS_RUN_CONFIG_NAME = "saucelabs_";

	public static String getAllJobs(boolean full) {
		try {
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
		} catch (Exception e) {
			KeywordLogger.getInstance(KeywordUtil.class).logInfo(e.getMessage());
		}
		return "null";
	}

	public static String getLatestJobId() {
		JSONArray jsonArray = null;
		def allJobIdsText = null;
		try {
			KeywordUtil.logInfo("[SAUCELABS] Sauce Labs Plugin is retrieving the latest job ...")
			allJobIdsText = SauceLabsUtils.getAllJobs(false);
			if(allJobIdsText.equals("null")) {
				KeywordLogger.getInstance(KeywordUtil.class)
						.logInfo("Failed to retrieve jobs ! Please check your credentials or internet connection");
				return "null";
			}
			jsonArray = new JSONArray(allJobIdsText);
			KeywordUtil.logInfo("[SAUCELABS] Latest job's ID: " + jsonArray.getJSONObject(0).get("id"))
			return jsonArray.getJSONObject(0).get("id");
		} catch(Exception e) {
			KeywordLogger.getInstance(KeywordUtil.class).logInfo(e.getMessage());
			if(jsonArray != null && jsonArray.length() != 0) {
				KeywordUtil.logInfo(jsonArray.toString());
			} else {
				KeywordUtil.logInfo(allJobIdsText);
			}
			KeywordUtil.logInfo("[SAUCELABS] Sauce Labs Plugin fails to retrieve the latest job !");
		}
		return "null";
	}

	public static String updateJob(String jobId, Map argsMap) {
		try {
			KeywordUtil.logInfo("[SAUCELABS] Sauce Labs Plugin is auto updating job status and information ...")
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
		} catch (Exception e) {
			KeywordUtil.logInfo("[SAUCELABS] Sauce Labs Plugin failed to update the latest job status and information!");
			KeywordLogger.getInstance(KeywordUtil.class).logInfo(e.getMessage());
		}
		return "null";
	}

	public static String uploadApp() {
		String projectLocation = RunConfiguration.getProjectDir();
		BundleSettingStore store = new BundleSettingStore(projectLocation, "com.kms.katalon.keyword.Saucelabs-keywords", true);
		String username = store.getString("username", "");
		String accessKey = store.getString("accessKey", "");
		String name = store.getString("mobile_filename", "");
		String location = store.getString("mobile_filepath", "");
		String overwrite = store.getString("mobile_overwrite", "false");

		if(location.equals("") || name.equals("")) {
			KeywordUtil.logInfo("[SAUCELABS] App name or location is empty, Sauce Labs Plugin will not upload the application!");
			return "App name or location is empty";
		}

		try {
			KeywordUtil.logInfo("[SAUCELABS] Sauce Labs Plugin is uploading the application ... ");
			String auth = "${username}:${accessKey}";
			String endpoint = "https://saucelabs.com/rest/v1/storage/" + username + "/" + name;
			String requestMethod = "POST";
			byte[] authEncBytes = Base64.getEncoder().encode(auth.getBytes());
			String authStringEnc = new String(authEncBytes);

			TestObjectProperty header1 = new TestObjectProperty("Authorization", ConditionType.EQUALS, "Basic " + authStringEnc);
			TestObjectProperty header2 = new TestObjectProperty("Content-Type", ConditionType.EQUALS, "application/octet-stream");
			ArrayList defaultHeaders = Arrays.asList(header1, header2);

			FormDataBodyParameter multiPartBodyOverwrite = new FormDataBodyParameter("--data-binary", location, "File");
			ArrayList multiPartBodies = Arrays.asList(multiPartBodyOverwrite);

			def textBodyContent = JsonOutput.toJson(["overwrite": overwrite]);

			def builder = new RestRequestObjectBuilder();
			def requestObject = builder
					.withRestRequestMethod(requestMethod)
					.withMultipartFormDataBodyContent(multiPartBodies)
					.withRestUrl(endpoint)
					.withHttpHeaders(defaultHeaders)
					.withTextBodyContent(textBodyContent)
					.build();

			ResponseObject respObj = WS.sendRequest(requestObject);
			KeywordUtil.logInfo(respObj.getResponseText());
			return respObj.getResponseText();
		} catch (Exception e) {
			KeywordUtil.logInfo("[SAUCELABS] Sauce Labs Plugin failed to upload the application !");
			KeywordUtil.logInfo(e.getMessage());
		}
		return "null";
	}
}

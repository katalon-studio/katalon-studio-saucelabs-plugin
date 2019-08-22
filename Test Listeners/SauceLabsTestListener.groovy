import com.kms.katalon.core.annotation.AfterTestCase
import com.kms.katalon.core.annotation.BeforeTestCase
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.context.TestCaseContext
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.keyword.saucelabs.SauceLabsUtils

public class SauceLabsTestListener {

	private static String SAUCE_LABS_RUN_CONFIG_NAME = SauceLabsUtils.SAUCE_LABS_RUN_CONFIG_NAME;

	@BeforeTestCase
	def enableSauceLabs(TestCaseContext testCaseContext) {
		String runConfigName = (String) RunConfiguration.getProperty("Name")
		KeywordUtil.logInfo("[SAUCELABS] Current run configuration: " + runConfigName)
		if(!runConfigName.toLowerCase().startsWith(SAUCE_LABS_RUN_CONFIG_NAME)) {
			KeywordUtil.logInfo("[SAUCELABS] Saucelabs Plugin will not attempt to auto update job status and information !");
		}
		SauceLabsUtils.uploadApp();
	}

	@AfterTestCase
	def autoUpdateJobStatus(TestCaseContext testCaseContext) {
		String runConfigName = (String) RunConfiguration.getProperty("Name");
		KeywordUtil.logInfo("[SAUCELABS] Current run configuration: " + runConfigName)
		if(runConfigName.toLowerCase().startsWith(SAUCE_LABS_RUN_CONFIG_NAME)){
			if(testCaseContext.isSkipped()) {
				KeywordUtil.logInfo("[SAUCELABS] This test case is skipped !")
				return;
			}

			String latestJobId = SauceLabsUtils.getLatestJobId()
			if(latestJobId.equals("null")) {
				KeywordUtil.logInfo("[SAUCELABS] Cannot retrieve latest job ID ! Please check your credentials or your internet connection.")
				return;
			}
			String status = testCaseContext.getTestCaseStatus()
			SauceLabsUtils.updateJob(latestJobId,
					, ["name":testCaseContext.getTestCaseId(), "passed": status.equals("PASSED") ? true: false])
		}
	}
}
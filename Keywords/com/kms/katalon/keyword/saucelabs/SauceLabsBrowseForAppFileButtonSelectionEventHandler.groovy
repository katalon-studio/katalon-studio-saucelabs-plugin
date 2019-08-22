package com.kms.katalon.keyword.saucelabs

import com.kms.katalon.core.keyword.IActionProvider
import com.kms.katalon.core.keyword.IContext
import com.kms.katalon.core.keyword.IControlSelectionEventHandler

public class SauceLabsBrowseForAppFileButtonSelectionEventHandler implements IControlSelectionEventHandler  {
	void handle(IActionProvider actionProvider, Map<String, Object> dataFields, IContext context) {
		String pathToApp = actionProvider.openFileDialogAndGetResult();
		actionProvider.setText("saucelabs-katalon", "mobile_filepath", pathToApp);
	}
}

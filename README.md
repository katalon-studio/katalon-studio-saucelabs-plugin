# katalon-studio-saucelabs-plugin

Katalon Studio SauceLabs Plugin is a Custom Keyword Plugin that provides integration with SauceLabs. Compatible with Katalon Studio from 6.2.0 onward.

## Katalon Studio Report Plugin provides:
- Automatically generate Custom Capabilities that allow remote driver to connect to SauceLabs.
- Automatically update SauceLabs' job information by test case's name and status.
- Ability to configure SauceLabs REST API. ([UK URL](https://wiki.saucelabs.com/display/DOCS/Sauce+Labs+European+Data+Center+Configuration+Information) and default URL)
<img width="602" alt="Screen Shot 2019-10-13 at 1 17 42 AM" src="https://user-images.githubusercontent.com/16775806/66705942-5f667a80-ed57-11e9-91c9-555b013dee57.png">

## Limitation:
- Katalon Studio SauceLabs Plugin is for Test Case only, not Test Suite or Test Suite Collection.

## Build project
1. Run:
```sh
gradle katalonPluginPackage
```
2. Copy *build/libs/katalon-studio-report-plugin.jar* and paste into Plugins folder of your Katalon Studio project to start using the keyword or upload to Katalon Store

## For developers

The essential components and logic:

* **Keywords/katalon-plugin.json**: It lets you define the UI of a setting page for your own plug-in. This setting page will be available under Project > Settings > Plugins. You can define text inputs, checkboxes and buttons. For a button, you can define what it does when clicked by specifying an implementation class path.

* **Test Listeners/SauceLabsTestListener.groovy**: A test listener before/after a test case that retrieves the current running configuration's name. You can use the name to condition your logic. In this case the after test case listener will update job's information with test case name and status if the current running configuration's name contains a *saucelabs_ prefix*.

* **Keywords/com/kms/katalon/keyword/saucelabs/SauceLabsButtonSelectionEventHandler.groovy**: The implementation class for a button that contains the logic of what will occur when the button is clicked. 

    * The implementation class for a button must implement the interface *IControlSelectionEventHandler* which has a *handle* method. 

    * In the *handle* method, plug-in can retrieve information that were input in the corresponding plug-in setting page through the second argument. The first argument *IActionProvider* is an interface that provides some operations that can be applied on the information retrieved. In this case a CustomProfile is created using the information retrieved in the setting page and is saved using method *saveCustomProfile*. The third argument is an interface *IContext* that can be used to retrieve additional informatiom.
    
    * After *saveCustomProfile* is called, the custom profile is saved and is available under Custom Capabilities when executing a test case.


## Usage
[Usage guide](docs/tutorials/usage.md)

## Companion products

### Katalon TestOps

[Katalon TestOps](https://analytics.katalon.com) is a web-based application that provides dynamic perspectives and an insightful look at your automation testing data. You can leverage your automation testing data by transforming and visualizing your data; analyzing test results; seamlessly integrating with such tools as Katalon Studio and Jira; maximizing the testing capacity with remote execution.

* Read our [documentation](https://docs.katalon.com/katalon-analytics/docs/overview.html).
* Ask a question on [Forum](https://forum.katalon.com/categories/katalon-analytics).
* Request a new feature on [GitHub](CONTRIBUTING.md).
* Vote for [Popular Feature Requests](https://github.com/katalon-analytics/katalon-analytics/issues?q=is%3Aopen+is%3Aissue+label%3Afeature-request+sort%3Areactions-%2B1-desc).
* File a bug in [GitHub Issues](https://github.com/katalon-analytics/katalon-analytics/issues).

### Katalon Studio
[Katalon Studio](https://www.katalon.com) is a free and complete automation testing solution for Web, Mobile, and API testing with modern methodologies (Data-Driven Testing, TDD/BDD, Page Object Model, etc.) as well as advanced integration (JIRA, qTest, Slack, CI, Katalon TestOps, etc.). Learn more about [Katalon Studio features](https://www.katalon.com/features/).

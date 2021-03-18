package com.qa.addProductsSO.stepDefinition;

import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import com.aventstack.extentreports.ExtentTest;
import com.qa.addProductsSO.pages.AddProductsPage;
import com.qa.addProductsSO.pages.AuthenticationPage;
import com.qa.addProductsSO.pages.HomePage;
import com.qa.addProductsSO.pages.LoginPage;
import com.qa.addProductsSO.pages.POCreationPage;
import com.qa.addProductsSO.util.ExcelDataToDataTable;
import com.qa.addProductsSO.util.TestBase;

import cucumber.api.DataTable;
import cucumber.api.Transform;
import cucumber.api.java.After;
import cucumber.api.java.en.Then;

public class AddProductsStepDefinition extends TestBase{
	
	LoginPage loginPage;
	AuthenticationPage authPage;
	HomePage homePage;
	POCreationPage poCreationPage;
	AddProductsPage addProductsPage;
	
	@After
	public void closeBrowser() {
		driver.close();
	}

	@Then("^User is on Sales order page$")
	public void navigate_to_Purchase_order_page() throws InterruptedException {
		ExtentTest loginfo = null;
		try {
			test = extent.createTest("Verification of alert when we add item using Add Products without selecting the Customer");
			loginfo = test.createNode("Login Test");
			
			TestBase.init();
			loginPage = new LoginPage();
			authPage = loginPage.login();
			homePage = authPage.Authentication();
			homePage.changeRole(prop.getProperty("companyName"), prop.getProperty("role"), prop.getProperty("roleType"), prop.getProperty("selectedRole"));
			Thread.sleep(1000);
			
			loginfo.pass("Login Successful in Netsuite");
		} catch (AssertionError | Exception e) {
			testStepHandle("FAIL", driver, loginfo, e, "Login Test");
		}
		
		ExtentTest loginfo2 = null;
		try {
			loginfo2 = test.createNode("User is in Sales order page");
			poCreationPage = homePage.clickOnNewSOLink();
			loginfo2.pass("Navigated to Sales order page");
		} catch (AssertionError | Exception e) {
			testStepHandle("FAIL", driver, loginfo2, e, "User is in Sales order page");
		}
	}
	
	@Then("^Verify the alert when we do not select the Customer$")
	public void verify_the_alert() throws InterruptedException {
		ExtentTest loginfo = null;
		try {
			loginfo = test.createNode("Verify the alert message when we do not select the Customer");
			Thread.sleep(6000);
			poCreationPage.verifyAlert(loginfo);
		} catch (AssertionError | Exception e) {
			testStepHandle("FAIL", driver, loginfo, e, "Verification of alert when we add item using Add Products without selecting the Customer");
		}
	}
	
	@Then("^Verify whether the filters are updated in Add Products page based on the filters applied in the Add Products setup screen$")
	public void verify_filters_in_add_products_page() throws InterruptedException {
		ExtentTest loginfo = null;
		try {
			test = extent.createTest("Verify whether the filters are updated in Add Products page based on the filters applied in the Setup screen");
			loginfo = test.createNode("Test Data >> Filter1: Class, Filter2: Department & Filter3: Location");
			homePage.clickOnAddProductsSetupLink();
			homePage.setUpAddProducts("Class","Department","Location");
			homePage.clickOnNewSOLink();
			poCreationPage.selectCustomer("Fabre Art Gallery");
			poCreationPage.verifyFilters(loginfo);
		} catch (AssertionError | Exception e) {
			testStepHandle("FAIL", driver, loginfo, e, "Verify the alert message when we do not select the vendor");
		}
	}
	
	@Then("^Select Customer \"([^\"]*)\" & Add the line items using Add Products with the excel data at \"([^\"]*)\" & Verify the items in the item sublist$")
	public void select_Vendor_Add_the_line_items_using_Add_Products_with_the_excel_data_at_Verify_the_items_in_the_item_sublist(String customer, @Transform(ExcelDataToDataTable.class) DataTable addProductsData) throws InterruptedException {
		ExtentTest loginfo = null;
		try {
			test = extent.createTest("Verification of adding items using Add Products");
			homePage.clickOnNewSOLink();
			poCreationPage.selectCustomer(customer);
			int itemsCount=0;
			int previousItemCount = 0;
			for(Map<String,String> data: addProductsData.asMaps(String.class, String.class)) {
				String classFilter = data.get("Class");
				String department = data.get("Department");
				String location = data.get("Location");
				String item = data.get("Item");
				String quantity = data.get("Quantity");
				String matrixFlag = data.get("Matrix flag");
				loginfo = test.createNode("Verify adding item(s): '"+item+"' using Add Products with Quantity: '"+quantity+"' & Matrix Flag: '"+matrixFlag);
				if(matrixFlag.equals("No")) {
					if(item.contains(",")) {
						StringTokenizer st = new StringTokenizer(item, ",");
						int tokensCount = st.countTokens();
						previousItemCount = itemsCount;
						itemsCount = itemsCount + tokensCount;
					}
					else {
						previousItemCount = itemsCount;
						itemsCount++;
					}
				}
				addProductsPage = poCreationPage.clickOnAddProductsBtn();
				addProductsPage.selectItemsInAddProductsPage(classFilter, department, location, item, quantity, matrixFlag);
				poCreationPage.verifyItems(item, quantity, matrixFlag, itemsCount, previousItemCount, loginfo);
			}
		} catch (AssertionError | Exception e) {
			testStepHandle("FAIL", driver, loginfo, e, "Verification of adding items using Add Products");
		}
	}

	@Then("^Verify the Item filters in the Add Products screen using excel data at \"([^\"]*)\"$")
	public void verify_the_items_in_the_Click_to_Add_list_based_on_filters_using_excel_data_at(@Transform(ExcelDataToDataTable.class) DataTable addProductsData) throws Throwable {
		ExtentTest loginfo = null;
		try {
			test = extent.createTest("Verification of Item filters in the Add Products screen");
			homePage.clickOnNewSOLink();
			for(Map<String,String> data: addProductsData.asMaps(String.class, String.class)) {
				List<String> itemsAfterFilterFromApPage;
				List<String> itemsAfterFilterFromSearch;
				String customer = data.get("Customer");
				String classFilter = data.get("Class");
				String department = data.get("Department");
				String location = data.get("Location");
				loginfo = test.createNode("Verification of items in the Click to add list after applying the following filters: "
						+ "Class = '"+classFilter+"', Department = '"+department+"', Location = '"+location+"'");
				poCreationPage.selectCustomer(customer);
				addProductsPage = poCreationPage.clickOnAddProductsBtn();
				itemsAfterFilterFromApPage = addProductsPage.verifyClickToAddList(classFilter, department, location); 
				itemsAfterFilterFromSearch = poCreationPage.getItemsWithFilterInNS(classFilter, department, location);
				poCreationPage.compareItemResultsAfterFilter(classFilter, department, location, itemsAfterFilterFromApPage, itemsAfterFilterFromSearch, loginfo);
			}
		} catch (AssertionError | Exception e) {
			testStepHandle("FAIL", driver, loginfo, e, "Verification of Item filters in the Add Products screen");
		}

	}
	
	@Then("^Verify the search results in the Add Products screen using excel data at \"([^\"]*)\"$")
	public void verify_the_search_results_in_the_Click_to_Add_list_using_excel_data_at(@Transform(ExcelDataToDataTable.class) DataTable addProductsData) throws Throwable {
		ExtentTest loginfo = null;
		try {
			test = extent.createTest("Verification of Item Search functionality in the Add Products screen");
			homePage.clickOnNewSOLink();
			for(Map<String,String> data: addProductsData.asMaps(String.class, String.class)) {
				String customer = data.get("Customer");
				String itemSearch = data.get("Item Search");
				loginfo = test.createNode("Verification of search results in the Click to add list for the Search criteria: '"+itemSearch+"'");
				poCreationPage.selectCustomer(customer);
				addProductsPage = poCreationPage.clickOnAddProductsBtn();
				addProductsPage.verifySearchResults(itemSearch, loginfo);
			}
			
		} catch (AssertionError | Exception e) {
			testStepHandle("FAIL", driver, loginfo, e, "Verification of Item Search functionality in the Add Products screen");
		}
	}
	
	@Then("^Verify the Item addition validations using excel data at \"([^\"]*)\"$")
	public void verify_the_popup_after_item_addition_using_excel_data_at(@Transform(ExcelDataToDataTable.class) DataTable addProductsData) throws Throwable {
		ExtentTest loginfo = null;
		try {
			test = extent.createTest("Verification of alerts when we add matrix item with non-matrix item exist in the Current Selection list and vice versa");
			loginfo = test.createNode("Verify the alert");
			homePage.clickOnNewSOLink();
			poCreationPage.selectCustomer("Fabre Art Gallery");
			addProductsPage = poCreationPage.clickOnAddProductsBtn();
			String poHandle = addProductsPage.switchToApPage();
			for(Map<String,String> data: addProductsData.asMaps(String.class, String.class)) {
				String classFilter = data.get("Class");
				String department = data.get("Department");
				String location = data.get("Location");
				String matrixFlag = data.get("Matrix flag");
				String item = data.get("Item name");
				addProductsPage.verifyItemAdditionValidations(classFilter, department, location, matrixFlag, item, loginfo);
			}
			driver.close();
			driver.switchTo().window(poHandle);
		} catch (AssertionError | Exception e) {
			testStepHandle("FAIL", driver, loginfo, e, "Verification of alerts when we add matrix item with non-matrix item exist in the Current Selection list and vice versa");
		}
	}
}

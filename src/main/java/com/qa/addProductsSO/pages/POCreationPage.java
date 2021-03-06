package com.qa.addProductsSO.pages;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.aventstack.extentreports.ExtentTest;
import com.qa.addProductsSO.util.TestBase;

public class POCreationPage extends TestBase{
		
	@FindBy(xpath = "//input[@id='custpage_addmultiple']")
	WebElement addProductsBtn;
	
	@FindBy(xpath = "//input[@id='entity_display']")
	WebElement customerBox;
	
	@FindBy(xpath = "//span[@id='parent_actionbuttons_entity_fs']//a[2]")
	WebElement customerArrowBtn;
	
	@FindBy(xpath = "//a[@id='entity_popup_list']")
	WebElement customerListBtn;
	
	@FindBy(xpath = "//div[@id='popup_outerdiv']//input[@id='st']")
	WebElement searchBox;
	
	@FindBy(xpath = "//div[@id='popup_outerdiv']//input[@id='Search']")
	WebElement searchBtn;
	
	@FindBy(xpath = "//div[@id='popup_outerdiv']//div[@id='inner_popup_div']//table//tr//td//following-sibling::td//a")
	List<WebElement> searchResults;
	
	@FindBy(xpath = "//div[@class='dropdownDiv']//div")
	List<WebElement> dropdownList;
	
	@FindBy(xpath = "//table[@id='item_splits']//tr[contains(@id, 'item_row')]//td[(count(//table[@id='item_splits']//div[text()='Item']//parent::td//preceding-sibling::*)+1)]")
	List<WebElement> itemNames;
	
	@FindBy(xpath = "//table[@id='item_splits']//tr[contains(@id, 'item_row')]//td[(count(//table[@id='item_splits']//div[text()='Quantity']//parent::td//preceding-sibling::*)+1)]")
	List<WebElement> quantityValues;
	
	@FindBy(xpath = "//span[text()='Lists']")
	WebElement listsLink;
	
	@FindBy(xpath = "//span[text()='Accounting']")
	WebElement accountingLink;
	
	@FindBy(xpath = "//span[text()='Items']")
	WebElement itemsLink;
	
	@FindBy(xpath = "//input[contains(@id,'inpt_searchid')]")
	WebElement viewDropdown;
	
	@FindBy(xpath = "//span[@id='searchid_fs_lbl']//a[@class='smalltextnolink']")
	WebElement viewLabel;
	
	@FindBy(xpath = "//span[@class='ns-icon ns-filters-onoff-button']")
	WebElement filtersDiv;
	
	@FindBy(xpath = "//div[@class='uir_filters_header']")
	WebElement filtersHeader;
	
	@FindBy(xpath = "//input[@name='inpt_Item_CLASS']")
	WebElement classDropdown;
	
	@FindBy(xpath = "//span[@id='Item_CLASS_fs_lbl']//a[@class='smalltextnolink']")
	WebElement classLabel;
	
	@FindBy(xpath = "//input[@name='inpt_Item_DEPARTMENT']")
	WebElement departmentDropdown;
	
	@FindBy(xpath = "//span[@id='Item_DEPARTMENT_fs_lbl']//a[@class='smalltextnolink']")
	WebElement departmentLabel;
	
	@FindBy(xpath = "//input[@name='inpt_Item_LOCATION']")
	WebElement locationDropdown;
	
	@FindBy(xpath = "//span[@id='Item_LOCATION_fs_lbl']//a[@class='smalltextnolink']")
	WebElement locationLabel;
	
	@FindBy(xpath = "//table[@id='div__bodytab']//tr[contains(@class,'uir-list-row-tr')]//a[contains(@id,'qsTarget_')]")
	List<WebElement> itemsInSearch;
	
	@FindBy(xpath = "//div[@id='filterdisplay']")
	WebElement filter1Div;
	
	@FindBy(xpath = "//div[@id='filter1display']")
	WebElement filter2Div;
	
	@FindBy(xpath = "//div[@id='filter2display']")
	WebElement filter3Div;
	
	public POCreationPage() {
		PageFactory.initElements(driver, this);
	}
	
	public void selectCustomer(String customer) throws InterruptedException {
		customerArrowBtn.click();
		customerListBtn.click();
		eleAvailability(driver, searchBox, 10);
		searchBox.sendKeys(customer);
		searchBtn.click();
		Thread.sleep(3000);
		for (int i = 0; i < searchResults.size(); i++) {
			if(searchResults.get(i).getText().trim().equals(customer)) {
				searchResults.get(i).click();
				break;
			}
		}
		Thread.sleep(5000);
	}
	
	public void verifyFilters(ExtentTest logInfo) {
		JavascriptExecutor je = (JavascriptExecutor) driver;
		je.executeScript("window.scrollBy(0,document.body.scrollHeight)");
		addProductsBtn.click();
		ArrayList<String> tabs = new ArrayList<String> (driver.getWindowHandles());
	    driver.switchTo().window(tabs.get(1));
	    eleAvailability(driver, filter1Div, 10);
		String filter1Text = filter1Div.getText().trim();
		String filter2Text = filter2Div.getText().trim();
		String filter3Text = filter3Div.getText().trim();
		if(filter1Text.equals("Class") && filter2Text.equals("Department") && filter3Text.equals("Location")) {
			System.out.println("Add Products page displayed all the filters correctly as selected in the Setup page");
			logInfo.pass("Add Products page displayed all the filters correctly as selected in the Setup page");
		}else {
			System.out.println("Filters in the Add Products page does'nt match with the filters selected in the Setup page");
			logInfo.fail("Filters in the Add Products page does'nt match with the filters selected in the Setup page");
		}
		driver.close();
		driver.switchTo().window(tabs.get(0));
	}
	
	public void verifyItems(String item, String quantity, String matrixFlag, int itemsCount, int previousItemCount, ExtentTest loginInfo) {
		boolean flag = false;
		if(matrixFlag.equals("No")) {
			System.out.println(itemsCount);
			if(previousItemCount != itemsCount || itemsCount != 0) {
				if(item.contains(",")) {
					String[] items = item.split(",");
					for(int i=items.length-1;i>=0;i--,itemsCount--) {
						String itemNameText = driver.findElement(By.xpath("//table[@id='item_splits']//tr[@id='item_row_"+itemsCount+
							"']//td[(count(//table[@id='item_splits']//div[text()='Item']//parent::td//preceding-sibling::*)+1)]"))
							.getText().trim();
						if(itemNameText.equals(items[i]))
							flag = true;
					}
				}
				else {
					String itemNameText = driver.findElement(By.xpath("//table[@id='item_splits']//tr[@id='item_row_"+itemsCount+
							"']//td[(count(//table[@id='item_splits']//div[text()='Item']//parent::td//preceding-sibling::*)+1)]"))
							.getText().trim();
					if(itemNameText.equals(item))
						flag = true;
				}
			}
			else
				flag = false;
			
			if(flag) {
				System.out.println(item+" added successfully");
				loginInfo.pass(item+" added successfully");
			}
			else {
				System.out.println(item+" unable to add");
				loginInfo.fail(item+" unable to add");
			}
		}else if(matrixFlag.equals("Yes")) {
			List<String> matrixComponentsAdded = new ArrayList<>();
			String[] quantities = quantity.split(",");
			int matrixChildCount = 0;
			int actualMatrixItemsCount = 0;
			for (int i = 0; i < quantities.length; i++) {
				if(!quantities[i].equals("null"))
					matrixChildCount ++;
			}
			for (int i = 0; i < itemNames.size(); i++) {
				if(itemNames.get(i).getText().contains(item)) {
					actualMatrixItemsCount++;
					matrixComponentsAdded.add(itemNames.get(i).getText().trim());
				}
			}
			
			System.out.println(actualMatrixItemsCount+" "+matrixChildCount);
			if(actualMatrixItemsCount >= matrixChildCount) {
				for (int i = 0; i < matrixComponentsAdded.size(); i++) {
					System.out.println(matrixComponentsAdded.get(i)+" added successfully");
					loginInfo.pass(matrixComponentsAdded.get(i)+" added successfully");
				}
			}else {
				System.out.println(item+" unable to add");
				loginInfo.fail(item+" unable to add");
			}
		}
		
	}
	
	public void compareItemResultsAfterFilter(String classFilter, String department, String location, List<String> itemResultsInApPage, List<String> itemResultsInSearch, ExtentTest logInfo) {
		
		if(classFilter.isBlank())
			classFilter="NULL";
		if(department.isBlank())
			department="NULL";
		if(location.isBlank())
			location="NULL";
		
		boolean flag = true;
		if(itemResultsInApPage.size() == itemResultsInSearch.size()) {
			for (int i = 0; i < itemResultsInApPage.size(); i++) {
				if(!itemResultsInApPage.get(i).contains(itemResultsInSearch.get(i)))
					flag = false;
			}
		}
		else
			flag = false;
		
		if(flag) {
			System.out.println("Items are displayed correctly in the Click to Add list after applying the filters");
			logInfo.pass("Items are displayed correctly in the Click to Add list after applying the following filters:\nClass = "+classFilter+", Department = "+department+", Location = "+location);
		}
		else {
			System.out.println("Items don't match");
			logInfo.fail("Items are not displayed correctly in the Click to add list after applying the following filters:\nClass = "+classFilter+", Department = "+department+", Location = "+location);
		}
	}
	
	public List<String> getItemsWithFilterInNS(String classFilter, String department, String location) throws InterruptedException {
		
		// Navigate to Items list
		eleAvailability(driver, listsLink, 10);
		action.moveToElement(listsLink).build().perform();
		eleAvailability(driver, accountingLink, 10); // Explicit Wait
		action.moveToElement(accountingLink).build().perform();
		eleAvailability(driver, itemsLink, 10); // Explicit Wait
		action.keyDown(Keys.CONTROL).click(itemsLink).keyUp(Keys.CONTROL).build().perform();
		
		List<String> itemsAfterFilterInNS = new ArrayList<>();
		String poPageWindow = driver.getWindowHandle();
		Set<String> allWindows = driver.getWindowHandles();
		for(String child: allWindows) {
			if(!poPageWindow.equals(child)) {
				driver.switchTo().window(child);
				
				// Change the view
				viewDropdown.sendKeys(prop.getProperty("search"));
				viewLabel.click();
				
				// Expand the filters section
				eleClickable(driver, By.xpath("//span[@class='ns-icon ns-filters-onoff-button']"), 5);
				String flag = filtersDiv.getAttribute("aria-expanded");
				if(flag.equals("false")) {
					action.moveToElement(filtersDiv).build().perform();
					filtersDiv.click();
				}
				
				// Select class
				if(!classFilter.isEmpty()) {
					eleAvailability(driver, By.xpath("//input[@id='inpt_Item_CLASS1']"), 3);
					classDropdown.sendKeys(classFilter);
					classLabel.click();
					Thread.sleep(2000);
				}
				else {
					eleAvailability(driver, By.xpath("//input[@id='inpt_Item_CLASS1']"), 3);
					classDropdown.sendKeys("- All -");
					classLabel.click();
					Thread.sleep(2000);
				}
				
				// Select Department
				if(!department.isEmpty()) {
					eleAvailability(driver, By.xpath("//input[@id='inpt_Item_DEPARTMENT2']"), 3);
					departmentDropdown.sendKeys(department);
					departmentLabel.click();
					Thread.sleep(2000);
				}
				else {
					eleAvailability(driver, By.xpath("//input[@id='inpt_Item_DEPARTMENT2']"), 3);
					departmentDropdown.sendKeys("- All -");
					departmentLabel.click();
					Thread.sleep(2000);
				}
				
				// Select Location
				if(!location.isEmpty()) {
					eleAvailability(driver, By.xpath("//input[@id='inpt_Item_LOCATION3']"), 3);
					locationDropdown.sendKeys(location);
					locationLabel.click();
					Thread.sleep(2000);
				}
				else {
					eleAvailability(driver, By.xpath("//input[@id='inpt_Item_LOCATION3']"), 3);
					locationDropdown.sendKeys("- All -");
					locationLabel.click();
					Thread.sleep(2000);
				}
				
				
				// Get items list
				for(int j=0;j<itemsInSearch.size();j++) {
					String text = itemsInSearch.get(j).getText().trim();
					itemsAfterFilterInNS.add(text);
				}
			}
		}
		
		driver.close();
		// Switch to PO Window
		driver.switchTo().window(poPageWindow);
		action.sendKeys(Keys.ESCAPE).build().perform();
		return itemsAfterFilterInNS;
	}
	
	public AddProductsPage clickOnAddProductsBtn() {
		JavascriptExecutor je = (JavascriptExecutor) driver;
		je.executeScript("window.scrollBy(0,document.body.scrollHeight)");
		addProductsBtn.click();
		return new AddProductsPage();
	}
	
	public void verifyAlert(ExtentTest logInfo) throws InterruptedException {
		JavascriptExecutor je = (JavascriptExecutor) driver;
		je.executeScript("window.scrollBy(0,document.body.scrollHeight)");
		addProductsBtn.click();
		Thread.sleep(2000);
		if(isAlertPresent()) {
			Alert alert = driver.switchTo().alert();
			String alertMsg = alert.getText();
			System.out.println("Alert occured: "+alertMsg);
			logInfo.pass("Alert occured: "+alertMsg);
			alert.accept();
		}
	}
}

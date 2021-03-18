package com.qa.addProductsSO.pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.qa.addProductsSO.util.TestBase;

public class HomePage extends TestBase{
	
	@FindBy(xpath = "//li[contains(@id,'ns-header-menu-userrole-item')]/a/span[@class='ns-role-menuitem-text']")
	List<WebElement> rolesList;
	
	@FindBy(xpath = "//span[text()='Transactions']")
	WebElement transactionsLink;
	
	@FindBy(xpath = "//span[text()='Sales']")
	WebElement salesLink;
	
	@FindBy(xpath = "//span[text()='Enter Sales Orders']")
	WebElement enterSalesOrderLink;
	
	@FindBy(xpath = "//span[text()='Setup']")
	WebElement setupLink;
	
	@FindBy(xpath = "//span[text()='Setup Multiple Item Selection']")
	WebElement setupMultipleItemLink;
	
	@FindBy(xpath = "//span[text()='TSS Add Products Set Up Screen']")
	WebElement addProductsSetupLink;
	
	@FindBy(xpath = "//a[contains(@class,'ns-scroll-button-bottom')]")
	WebElement scrollBtn;
	
	@FindBy(xpath = "//div[@class='ns-role']/span[2]")
	WebElement role;
	
	@FindBy(xpath = "//div[@id='devpgloadtime']//following-sibling::div[@class='ns-logo']//img")
	List<WebElement> images;
	
	@FindBy(xpath = "//span[@id='thankyou_val']//iframe")
	WebElement addProdSetupFrame;
	
	@FindBy(xpath = "//input[@value='Edit']")
	WebElement editBtn;

	@FindBy(xpath = "//input[@value='Save']")
	WebElement saveBtn;
	
	@FindBy(xpath = "//select[@id='filter']")
	WebElement filter1Dropdown;
	
	@FindBy(xpath = "//select[@id='filter1']")
	WebElement filter2Dropdown;
	
	@FindBy(xpath = "//select[@id='filter2']")
	WebElement filter3Dropdown;
	
	public HomePage() {
		action = new Actions(driver);
		PageFactory.initElements(driver, this);
	}
	
	public void changeRole(String company, String roleData, String roleType, String selectedRole) {
		action = new Actions(driver);
		String currentRole = role.getText().trim();
		if(currentRole.equals(selectedRole)) {
			System.out.println("Role already selected");
		} else {
			action.moveToElement(driver.findElement(By.xpath("//div[@id='spn_cRR_d1']/a"))).build().perform();
			for(int i=0;i<rolesList.size();i++) {
				WebElement roleElement = rolesList.get(i);
				String roleValue = roleElement.getText().trim();
				if(roleValue.contains(company) && roleValue.contains(roleData) && roleValue.contains(roleType)) {
					roleElement.click();
					break;
				}
			}
		}
		
	}
	
	public POCreationPage clickOnNewSOLink() throws InterruptedException {
		eleAvailability(driver, transactionsLink, 10);
		action.moveToElement(transactionsLink).build().perform();
		eleAvailability(driver, salesLink, 10); // Explicit Wait
		action.moveToElement(salesLink).build().perform();
		eleAvailability(driver, enterSalesOrderLink, 10); // Explicit Wait
		enterSalesOrderLink.click();
		if(isAlertPresent()) {
			driver.switchTo().alert().accept();
		}
		
		return new POCreationPage();
	}

	public void clickOnAddProductsSetupLink() throws InterruptedException {
		eleAvailability(driver, setupLink, 5);
		action.moveToElement(setupLink).build().perform();
		try {
			eleAvailability(driver, setupMultipleItemLink, 5);
		} catch (Exception e) {
			eleAvailability(driver, scrollBtn, 5);
			action.moveToElement(scrollBtn).build().perform();
			Thread.sleep(1500);
			eleAvailability(driver, setupMultipleItemLink, 5);
		}
		action.moveToElement(setupMultipleItemLink).build().perform();
		eleAvailability(driver, addProductsSetupLink, 5);
		addProductsSetupLink.click();
	}

	public void setUpAddProducts(String filter1, String filter2, String filter3) throws InterruptedException {
		driver.switchTo().frame(addProdSetupFrame);
		eleClickable(driver, editBtn, 10);
		editBtn.click();
		eleClickable(driver, saveBtn, 10);
		filter1Dropdown.sendKeys(filter1);
		filter2Dropdown.sendKeys(filter2);
		filter3Dropdown.sendKeys(filter3);
		saveBtn.click();
		eleClickable(driver, editBtn, 10);
		driver.switchTo().defaultContent();
	}
	
}

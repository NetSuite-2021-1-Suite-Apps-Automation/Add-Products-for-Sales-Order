Feature: Testing Add Products Suite App

Scenario: Test scenario to automate Add Products Suite App
Given User is on Sales order page
Then Verify the alert when we do not select the Customer
Then Verify whether the filters are updated in Add Products page based on the filters applied in the Add Products setup screen
Then Select Customer "Fabre Art Gallery" & Add the line items using Add Products with the excel data at "C:\Users\Sravan Kumar\Desktop\2021.1 SuiteApp Automation Data\Add ProductsSO_DevProdRP.xlsx,Item addition" & Verify the items in the item sublist
Then Verify the Item filters in the Add Products screen using excel data at "C:\Users\Sravan Kumar\Desktop\2021.1 SuiteApp Automation Data\Add ProductsSO_DevProdRP.xlsx,Filters Testing"
Then Verify the search results in the Add Products screen using excel data at "C:\Users\Sravan Kumar\Desktop\2021.1 SuiteApp Automation Data\Add ProductsSO_DevProdRP.xlsx,Search Testing"
Then Verify the Item addition validations using excel data at "C:\Users\Sravan Kumar\Desktop\2021.1 SuiteApp Automation Data\Add ProductsSO_DevProdRP.xlsx,Item Addition validations"
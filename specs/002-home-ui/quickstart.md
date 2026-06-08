# Quickstart: Home UI Testing

This guide provides scenarios to manually test the Home UI implementation.

## Scenario 1: Initial Load & RTL Layout
1. Launch the application and navigate to the Home screen (الرئيسية).
2. Set the device language to Arabic to verify RTL layout.
3. Verify that the "Total Balance" (إجمالي الرصيد) card displays correctly with the balance amount and percentage chip aligned properly.

## Scenario 2: Action Buttons
1. Tap the "Add" (إضافة) button. Verify no crash occurs (action is a no-op per spec).
2. Tap the "Transfer" (تحويل) button. Verify no crash occurs (action is a no-op per spec).

## Scenario 3: Spending Analysis Toggle
1. Scroll down to the "Spending Analysis" (تحليل الإنفاق) section.
2. Tap "Weekly" (أسبوعي). Verify the data list updates (mock data changes).
3. Tap "Monthly" (شهري). Verify the data list reverts to the monthly mock data.

## Scenario 4: Transactions List
1. Scroll to the "Transactions" (المعاملات) list.
2. Verify that the list renders the mocked transaction items with the correct styling, typography, and RTL support.

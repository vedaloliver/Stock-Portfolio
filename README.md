# Stock-Portfolio

A simple portfolio tracker, allowing the user to find real time stock data and save results to a collected wallet.

Rationale behind creating this project was due to the surprising difficulty i found in obtaining a clear and easy way to track and observe the progress of my various stock holdings i had, which were spread across different entities. Most applets/pieces of software were bloated with unnecessary information and features which were not related to my functional requirements.

________________________________________________________________________________________________
Features:

1. Pulls stock data from Alpha Vantage's API's and processes necessary information
2. Use of PostGreSQL, to i) store company stock data and ii) store user wealth data corresponding to company
3. User has ability to add, delete and update new or existing data which may or may not be found in the database already
4. User will be prompted to provide monetary input to the program which allows user to see their wealth appreciation/depreciation over time
5. Simple UI which allows user to perform program functionality and observe stock metrics

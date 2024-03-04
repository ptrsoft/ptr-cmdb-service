
**Appkube Reporting APIs**

**Base Url:** https://api.synectiks.net/reporting

1.
/reporting/cost?orgId=1&cloud=all&granularity=quarterly&compareTo=last\_quarter&spendType=forcast

`	`Description: API provides actual cost and comparative stats based on the given query parameters

`	`Type: 		 GET

`	`Parameters:

`		`orgId		= 	Ogranization id. A long unique database identifier. It as organization id of logged-in user

`		`cloud		=	all (get consolidated cost of all clouds)

`						`aws (get cost of aws only)

`						`azure (get cost of azure only)

`						`gcp (get cost of gcp only)

`		`granularity = 	daily, weekly, monthly, quarterly, half\_yearly, yearly

`		`compareTo 	=	it is based on granularity, if granularity is quarterly, compareTo can be fist\_quarter, second\_quarter, third\_quarter, fourth\_quarter or last\_quarter

`		`spendType	=	Optional parameter

`						`forcast

`						`actual



/reporting/actual-cost-saving?orgId=1&cloud=all&granularity=quarterly&compareTo=last\_quarter	

`	`Description: API provides actual cost saved and comparative stats based on the given query parameters

`	`Type: 		 GET

`	`Parameters:

`		`orgId		= 	Ogranization id. A long unique database identifier. It as organization id of logged-in user

`		`cloud		=	all (get consolidated cost saved of all clouds)

`						`aws (get cost saved of aws only)

`						`azure (get cost saved of azure only)

`						`gcp (get cost of saved gcp only)

`		`granularity = 	daily, weekly, monthly, quarterly, half\_yearly, yearly

`		`compareTo 	=	it is based on granularity, if granularity is quarterly, compareTo can be fist\_quarter, second\_quarter, third\_quarter, fourth\_quarter or last\_quarter

/reporting/estimated-cost-saving?orgId=1&cloud=all&granularity=quarterly&compareTo=last\_quarter	

`	`Description: API provides estimated cost saved and comparative stats based on the given query parameters

`	`Type: 		 GET

`	`Parameters:

`		`orgId		= 	Ogranization id. A long unique database identifier. It as organization id of logged-in user

`		`cloud		=	all (get consolidated cost saved of all clouds)

`						`aws (get consolidated cost saved of aws only)

`						`azure (get consolidated cost saved of azure only)

`						`gcp (get consolidated cost of saved gcp only)

`		`granularity = 	daily, weekly, monthly, quarterly, half\_yearly, yearly

`		`compareTo 	=	it is based on granularity, if granularity is quarterly, compareTo can be fist\_quarter, second\_quarter, third\_quarter, fourth\_quarter or last\_quarter



/reporting/service-category-wise-cost?orgId=1&service-category=all&cloud=all&granularity=quarterly&compareTo=last\_quarter		

`	`Description: API provides service category wise actual cost based on the given query parameters

`	`Type: 		 GET

`	`Parameters:

`		`orgId		= 	Ogranization id. A long unique database identifier. It as organization id of logged-in user

`		`service-category	= all (it provides cost of every service category)

`							  `compute (it provides cost of compute service category)

`							  `storage (it provides cost of storage service category)

`							  `database (it provides cost of database service category)

`							  `networking (it provides cost of networking service category)

`							  `data-transfer (it provides cost of data-transfer service category)

`							  `monitoring (it provides cost of monitoring service category)

`		`cloud		=	all (get service category wise actual cost of all clouds)

`						`aws (get service category wise actual cost of aws only)

`						`azure (get service category wise actual cost of azure only)

`						`gcp (get service category wise actual cost of gcp only)

`		`granularity = 	daily, weekly, monthly, quarterly, half\_yearly, yearly

`		`compareTo 	=	it is based on granularity, if granularity is quarterly, compareTo can be fist\_quarter, second\_quarter, third\_quarter, fourth\_quarter or last\_quarter

/reporting/service-wise-cost?orgId=1&service=all&cloud=all&granularity=quarterly&compareTo=last\_quarter&noOfRecords=10&order=top		

`	`Description: API provides service wise actual cost based on the given query parameters

`	`Type: 		 GET

`	`Parameters:

`		`orgId		= 	Ogranization id. A long unique database identifier. It as organization id of logged-in user

`		`service		= 	all (it provides cost of all services)

`							  `EC2 (it provides cost of EC2 service)

`							  `RDS (it provides cost of RDS service)

`							  `S3 (it provides cost of S3 service)

`							  `EKS (it provides cost of EC2 service)

`							  `Lambda (it provides cost of Lambda service)

`		`cloud		=	all (get service category wise actual cost of all clouds)

`						`aws (get service category wise actual cost of aws only)

`						`azure (get service category wise actual cost of azure only)

`						`gcp (get service category wise actual cost of gcp only)

`		`granularity = 	daily, weekly, monthly, quarterly, half\_yearly, yearly

`		`compareTo 	=	it is based on granularity, if granularity is quarterly, compareTo can be fist\_quarter, second\_quarter, third\_quarter, fourth\_quarter or last\_quarter

`		`noOfRecords	=	Optional parameter. can be any positive integer > 0 (default is 0. that means all records). -ve will be discarded/ignored

`						`10

`		`order		=	Optional parameter. (Default order will be top (descending))

`						`top (descending order). if noOfRecords records provided, it will be like top 10

`						`bottom (ascending order). if noOfRecords records provided, it will be like bottom 10

/reporting/service-wise-cost-details?orgId=1&service=all&cloud=all&granularity=quarterly&compareTo=last\_quarter&noOfRecords=10&order=top		

`	`Description: API provides service wise actual cost details based on the given query parameters

`	`Type: 		 GET

`	`Parameters:

`		`orgId		= 	Ogranization id. A long unique database identifier. It as organization id of logged-in user

`		`service		= 	all (it provides cost of all services)

`							  `EC2 (it provides cost of EC2 service)

`							  `RDS (it provides cost of RDS service)

`							  `S3 (it provides cost of S3 service)

`							  `EKS (it provides cost of EC2 service)

`							  `Lambda (it provides cost of Lambda service)

`		`cloud		=	all (get service category wise actual cost of all clouds)

`						`aws (get service category wise actual cost of aws only)

`						`azure (get service category wise actual cost of azure only)

`						`gcp (get service category wise actual cost of gcp only)

`		`granularity = 	daily, weekly, monthly, quarterly, half\_yearly, yearly

`		`compareTo 	=	it is based on granularity, if granularity is quarterly, compareTo can be fist\_quarter, second\_quarter, third\_quarter, fourth\_quarter or last\_quarter

`		`noOfRecords	=	Optional parameter. can be any positive integer > 0 (default is 0. that means all records). -ve will be discarded/ignored

`						`10

`		`order		=	Optional parameter. (Default order will be top (descending))

`						`top (descending order). if noOfRecords records provided, it will be like top 10

`						`bottom (ascending order). if noOfRecords records provided, it will be like bottom 10





/reporting/cost-trend?orgId=1&cloud=all&granularity=weekly&weekStart=saturday&startFrom=last\_quarter&compareTo=last\_quarter&forcast=true		

`	`Description: API provides cost trend based on the given query parameters

`	`Type: 		 GET

`	`Parameters:

`		`orgId		= 	Ogranization id. A long unique database identifier. It as organization id of logged-in user

`		`cloud		=	all (get service category wise actual cost of all clouds)

`						`aws (get service category wise actual cost of aws only)

`						`azure (get service category wise actual cost of azure only)

`						`gcp (get service category wise actual cost of gcp only)

`		`granularity = 	daily, weekly, monthly, quarterly, half\_yearly, yearly

`		`weekStart	=	if granularity is weekly.

`						`saturday, sunday or monday

`		`startFrom	=	last\_quarter, last\_month, last\_year, current\_quarter, current\_year\_start or last\_n\_months (n = 1,2,3,4....)

`		`compareTo 	=	Optional Parameter

`						`it is based on granularity, if granularity is quarterly, compareTo can be fist\_quarter, second\_quarter, third\_quarter, fourth\_quarter or last\_quarter

`		`forcast		= 	optional parameter (default false)

`						`true (will provide forcasted cost based on the granularity)

`						`false (forcasted cost will not be included)



/reporting/cost-saving-distribution?orgId=1&cloud=all&granularity=quarterly

`	`Description: API provides by wich strategy, how much cost saved based on the given query parameters

`	`Type: 		 GET

`	`Parameters:

`		`orgId		= 	Ogranization id. A long unique database identifier. It as organization id of logged-in user

`		`cloud		=	all (get consolidated cost saved of all clouds)

`						`aws (get consolidated cost saved of aws only)

`						`azure (get consolidated cost saved of azure only)

`						`gcp (get consolidated cost of saved gcp only)

`		`granularity = 	daily, weekly, monthly, quarterly, half\_yearly, yearly



/reporting/region-wise-cost?orgId=1&cloud=all&region=all&granularity=quarterly&compareTo=last\_quarter&noOfRecords=10&order=top

`	`Description: API provides region wise actual cost and comparative stats based on the given query parameters

`	`Type: 		 GET

`	`Parameters:

`		`orgId		= 	Ogranization id. A long unique database identifier. It as organization id of logged-in user

`		`cloud		=	all (get consolidated cost of all clouds)

`						`aws (get cost of aws only)

`						`azure (get cost of azure only)

`						`gcp (get cost of gcp only)

`		`region		=	all (get cost of all the regions)

`						`us-east-1 etc.. (get cost of provided region only)				

`		`granularity = 	daily, weekly, monthly, quarterly, half\_yearly, yearly

`		`compareTo 	=	optional parameter. it is based on granularity, if granularity is quarterly, compareTo can be fist\_quarter, second\_quarter, third\_quarter, fourth\_quarter or last\_quarter

`		`noOfRecords	=	Optional parameter. can be any positive integer > 0 (default is 0. that means all records). -ve will be discarded/ignored

`						`10

`		`order		=	Optional parameter. (Default order will be top (descending))

`						`top (descending order). if noOfRecords records provided, it will be like top 10

`						`bottom (ascending order). if noOfRecords records provided, it will be like bottom 10

/reporting/region-wise-cost-details?orgId=1&cloud=all&region=all&granularity=quarterly&compareTo=last\_quarter&noOfRecords=10&order=top

`	`Description: API provides region wise actual cost and comparative stats based on the given query parameters

`	`Type: 		 GET

`	`Parameters:

`		`orgId		= 	Ogranization id. A long unique database identifier. It as organization id of logged-in user

`		`cloud		=	all (get consolidated cost of all clouds)

`						`aws (get cost of aws only)

`						`azure (get cost of azure only)

`						`gcp (get cost of gcp only)

`		`region		=	all (get cost of all the regions)

`						`us-east-1 etc.. (get cost of provided region only)				

`		`granularity = 	daily, weekly, monthly, quarterly, half\_yearly, yearly

`		`compareTo 	=	optional parameter. it is based on granularity, if granularity is quarterly, compareTo can be fist\_quarter, second\_quarter, third\_quarter, fourth\_quarter or last\_quarter

`		`noOfRecords	=	Optional parameter. can be any positive integer > 0 (default is 0. that means all records). -ve will be discarded/ignored

`						`10

`		`order		=	Optional parameter. (Default order will be top (descending))

`						`top (descending order). if noOfRecords records provided, it will be like top 10

`						`bottom (ascending order). if noOfRecords records provided, it will be like bottom 10



/reporting/account-wise-cost?orgId=1&cloud=all&account=all&granularity=quarterly&compareTo=last\_quarter&noOfRecords=10&order=top

`	`Description: API provides account wise actual cost and comparative stats based on the given query parameters

`	`Type: 		 GET

`	`Parameters:

`		`orgId		= 	Ogranization id. A long unique database identifier. It as organization id of logged-in user

`		`cloud		=	all (get consolidated cost of all clouds)

`						`aws (get cost of aws only)

`						`azure (get cost of azure only)

`						`gcp (get cost of gcp only)

`		`account		=	all (get cost of all the accounts)

`						`12345678 etc.. (get cost of provided account only)				

`		`granularity = 	daily, weekly, monthly, quarterly, half\_yearly, yearly

`		`compareTo 	=	optional parameter. it is based on granularity, if granularity is quarterly, compareTo can be fist\_quarter, second\_quarter, third\_quarter, fourth\_quarter or last\_quarter

`		`noOfRecords	=	Optional parameter. can be any positive integer > 0 (default is 0. that means all records). -ve will be discarded/ignored

`						`10

`		`order		=	Optional parameter. (Default order will be top (descending))

`						`top (descending order). if noOfRecords records provided, it will be like top 10

`						`bottom (ascending order). if noOfRecords records provided, it will be like bottom 10



/reporting/department-wise-cost?orgId=1&cloud=all&department=all&granularity=quarterly&compareTo=last\_quarter

`	`Description: API provides department wise actual cost and comparative stats based on the given query parameters

`	`Type: 		 GET

`	`Parameters:

`		`orgId		= 	Ogranization id. A long unique database identifier. It as organization id of logged-in user

`		`cloud		=	all (get consolidated cost of all clouds)

`						`aws (get cost of aws only)

`						`azure (get cost of azure only)

`						`gcp (get cost of gcp only)

`		`department	=	all (get cost of all the departments)

`						`IT-INFRA etc.. (get cost of provided department only)				

`		`granularity = 	daily, weekly, monthly, quarterly, half\_yearly, yearly

`		`compareTo 	=	optional parameter. it is based on granularity, if granularity is quarterly, compareTo can be fist\_quarter, second\_quarter, third\_quarter, fourth\_quarter or last\_quarter



/reporting/current-month-cost?orgId=1&cloud=aws

`	`Description: API provides current month cost/spend and consumed budget till date out of total allocated budget based on the given query parameters

`	`Type: 		 GET

`	`Parameters:

`		`orgId		= 	Ogranization id. A long unique database identifier. It as organization id of logged-in user

`		`cloud		=	aws (get cost of aws only)

`						`azure (get cost of azure only)

`						`gcp (get cost of gcp only)



/reporting/product-wise-cost?orgId=1&cloud=all&product=all&granularity=quarterly&compareTo=last\_quarter&noOfRecords=10&order=top

`	`Description: API provides product wise actual cost and comparative stats based on the given query parameters

`	`Type: 		 GET

`	`Parameters:

`		`orgId		= 	Ogranization id. A long unique database identifier. It as organization id of logged-in user

`		`cloud		=	all (get consolidated cost of all clouds)

`						`aws (get cost of aws only)

`						`azure (get cost of azure only)

`						`gcp (get cost of gcp only)

`		`product		=	all (get cost of all the products)

`						`Procurement etc.. (get cost of provided product only)				

`		`granularity = 	daily, weekly, monthly, quarterly, half\_yearly, yearly

`		`compareTo 	=	optional parameter. it is based on granularity, if granularity is quarterly, compareTo can be fist\_quarter, second\_quarter, third\_quarter, fourth\_quarter or last\_quarter

`		`noOfRecords	=	Optional parameter. can be any positive integer > 0 (default is 0. that means all records). -ve will be discarded/ignored

`						`1,2,3,4,5 etc.......

`		`order		=	Optional parameter. (Default order will be top (descending))

`						`top (descending order). if noOfRecords records provided, it will be like top 10

`						`bottom (ascending order). if noOfRecords records provided, it will be like bottom 10



/reporting/service-type-wise-cost?orgId=1&serviceType=all&cloud=all&granularity=quarterly&compareTo=last\_quarter&noOfRecords=10&order=top		

`	`Description: API provides service type wise actual cost based on the given query parameters

`	`Type: 		 GET

`	`Parameters:

`		`orgId		= 	Ogranization id. A long unique database identifier. It as organization id of logged-in user

`		`serviceType	= 	all (it provides cost of all service types)

`							  `App (it provides cost of App service)

`							  `Data (it provides cost of Data service)

`							  `Network (it provides cost of Network service)

`							  `Other (it provides cost of Other service)

`		`cloud		=	all (get service category wise actual cost of all clouds)

`						`aws (get service category wise actual cost of aws only)

`						`azure (get service category wise actual cost of azure only)

`						`gcp (get service category wise actual cost of gcp only)

`		`granularity = 	daily, weekly, monthly, quarterly, half\_yearly, yearly

`		`compareTo 	=	it is based on granularity, if granularity is quarterly, compareTo can be fist\_quarter, second\_quarter, third\_quarter, fourth\_quarter or last\_quarter

`		`noOfRecords	=	Optional parameter. can be any positive integer > 0 (default is 0. that means all records). -ve will be discarded/ignored

`						`10

`		`order		=	Optional parameter. (Default order will be top (descending))

`						`top (descending order). if noOfRecords records provided, it will be like top 10

`						`bottom (ascending order). if noOfRecords records provided, it will be like bottom 10 		







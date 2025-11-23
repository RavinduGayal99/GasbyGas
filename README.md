Description and requirements:
----------------------------

You are required to produce a solution using appropriate approach to analyse, design,
implement and test the software for the scenario described below:

Scenario
--------

GasByGas is a LP gas cylinder distributor establish in Sri Lanka where they provide gas distribution island wide.
They have several registered outlet shops service centers at all districts. GasByGas wishes to introduce an Online gas
requesting and delivering system for the requirement of gas distribution and tracking. End consumers are to be given facilities
to request a gas from an outlet and obtain a token with expected pickup period with two weeks of tolerance. If the gas distribution
cannot make available the requested gas at the outlet within the period given a new scheduled period will be issued.
If there is no any scheduled delivery to a particular outlet or scheduled gas stock finished, the necessary message will be
given and requests are not permitted. Once the delivery schedule to the given outlet is confirmed by the dispatch office in the head office,
notification SMS/email will be sent to all token holders to handover empty cylinders and the money for the requested gas.
If the customer unable to satisfy the handing over within the given date, outlet manager can reallocate it to another customer after
communicating the decision to the customer through email/sms or phone call. End consumers can take a token from the gas outlet also using the
same system when scheduled delivery is available. On the day before the date of delivery to the outlet, end consumers will receive sms/email to receive the cylinder.
System will be able to limit request based on the personal identity which coupled with the NIC/Phone/email etc. The outlet requests are to be taken separately using the system.
Gas for the industries and business organizations need to have a separate category of request upon registration and validation with the certification of the organization.    
 
You are required to design a web and mobile apps to achieve above objectives. High-level system requirement as follows.
⦁	System has two components 
⦁	Web server with the database. 
⦁	Client web Application with the Database Server.
⦁	Mobile app can use the same application services provided by the server (Data operations).
⦁	The consumers should be able to register in the system and multiple registrations per identity should be prevented. 
⦁	Outlet manager should be able to verify the tokens and details of the consumers/customers.
⦁	Outlet manager should be able to mark the status of the requests upon receiving empties and payments.
⦁	Based on the distribution of gas for the outlets, head office should be able to view the status of each outlet. 
By considering the brief outline requirement given above, the hidden and potential requirements are to be derived by you for the purpose and mention those clearly. The hidden requirements are to be logically match with the outline requirement given above.

Note
The security mechanisms are considered as Top priority in this application system

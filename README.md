# HotelSystem
Full Stack Java hotel management system

## Table of contents
* [Project Overview](#project-overview)
* [Technologies](#technologies)
* [Setup](#setup)

## Project Overview
This project is a hotel management system intended for customers and employees. The application starts with a splash screen with two available options. Users can either book a room or enter login credentials to view their current reservations.

![Screen Shot 2022-02-09 at 1 32 00 PM](https://user-images.githubusercontent.com/98786865/153276539-e8e22187-f50a-4cc3-a550-a1539293b8f2.png)

Selecting to book a room will have customers go through the booking process. They will select a room from a list of available rooms, then provide personal information and payment information to complete the reservation.

![selectroom_cust](https://user-images.githubusercontent.com/98786865/153285888-9dd5474e-12be-4aa5-b8f5-cf6b2cff5024.png)

![booking_cust](https://user-images.githubusercontent.com/98786865/153286239-f3818b44-97ea-4ff3-8faf-77ea01c8dbd8.png)

Completing the reservation will bring you to the home page, wherein you have the option to check in, cancel booking, adjust length of stay, view and make requests. Users can only access the latter two after checking in. 

![user_home](https://user-images.githubusercontent.com/98786865/153286668-21e9fffc-a803-4d60-abee-f4433d3177d1.png)

![adjuststay](https://user-images.githubusercontent.com/98786865/153286891-aff53de8-ca10-400a-989a-85a37868b758.png)

![makerequest](https://user-images.githubusercontent.com/98786865/153286921-6954fa78-cb29-453b-8eba-b7fd2302fbe0.png)

Users can additionally login to their current reservations using the email address they provided, and the confirmation number that was sent to their email.

![login_cust](https://user-images.githubusercontent.com/98786865/153287112-1dc4b3ee-cd56-4cf5-9533-3b1844d40b0b.png)

The employee login page is accessible from here as well. Regular employees and admins can login through this page, with admins having the additional capability of adding new employees. They also have the capability of checking customer requests, and creating reservtions for customers.

![emp_login](https://user-images.githubusercontent.com/98786865/153287616-88b5defa-596e-4775-989e-2affbf33ec62.png)

![admin_home](https://user-images.githubusercontent.com/98786865/153287952-7ad85a64-4336-477f-add9-927701583d9c.png)

![add_employee](https://user-images.githubusercontent.com/98786865/153288018-6eca1450-1b33-4402-a56d-791fce8d8bf4.png)

![check_requests](https://user-images.githubusercontent.com/98786865/153288076-9d096c1a-5a90-4cbf-9591-455428a4c1c2.png)



## Technologies
Langauges
* Java
* MySQL

Tools and API's
* Javafx
* Javamail
* JBCrypt
* JDBC
* SceneBuilder

## Setup
This project runs on Java SE 14, so make sure you have atleast that version or newer. 



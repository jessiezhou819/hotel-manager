# Hotel Management System


The application serves as a *hotel management system* that provides a 
simple yet efficient user interface for users to keep track of guest 
information during check-in and check-out. A registration list will be
provided that gives a summary of current guests.The **main users** of this 
app would be hotel front desk staff and administrators. By collecting
personal profiles including contact info, room type, and extra 
charges to the customer, the hotel can be managed in an organized manner.

This project is of interest to me because I have always enjoyed travelling
and I noticed that having a well-designed management system in a hotel
can greatly increase one's staying experience. This inspires me to develop 
a similar real-world application to investigate how the system
actually works. Also, since the scale of this project can be fairly flexible, 
I am allowed to gradually add more features to this application as I 
gain experiences. 

## User Stories
- As a user, I want to be able to add a **guest** to the registration list
- As a user, I want to be able to select from two different room types
- As a user, I want to be able to obtain an initial bill based on the room type
- As a user, I want to be able to view all current guests in **registration history**
- As a user, I want to be able to view total revenue and current number of guests
- As a user, I want to be able to remove a guest from the registration list and obtain a final bill during check-out
- As a user, I want to be able to add different Mini Bar items purchased by the guest
- As a user, I want to be able to calculate and display extra charges from Mini Bar
- As a user, I want to be able to add the extra charges to total payments

- As a user, I want to be able to save my registration list to file (if I so choose)
- As a user, I want to be able to be able to load my registration list from file (if I so choose)

## Instructions for Grader

- You can generate the first required action related to the user story "adding multiple guests to the registration list" 
  by clicking the button labelled "Guest Check-in" in the welcome menu and fill in the required information
- You can generate the second required action related to the user story "removing guests from the registration history" 
  by selecting the guest's name on the scrolling pane and clicking the "Check-out" button
- You can view each guest's information by selecting the guest name and clicking "View Info"
- You can locate my visual component on the background of the welcome page
- You can save the state of my application by selecting the option YES on the pop-up window when the application ends
- You can reload the state of my application by selecting YES on the pop-up window when the application starts

## Phase 4: Task 2
*SAMPLE:*

Tue Nov 28 21:17:41 PST 2023<br>
New Guest Jessie is added to Registration History.

Tue Nov 28 21:17:48 PST 2023<br>
New Guest Joe is added to Registration History.

Tue Nov 28 21:17:55 PST 2023<br>
Guest Joe is removed from Registration History.


## Phase 4: Task 3

**Splitting up classes to improve Single Responsibility Principle**
- Instead of manipulating a list of String in the Guest class, I can introduce a new class - MiniBarItems -
that represents the list 
of purchases with related actions such as calculating bill for distinct items. This will give me more flexibility 
to add more operations and adhere to the Single Responsibility Principle

**Improving Coupling by extracting codes into representative classes**
- extracting a class responsible for only pop-up messages and loading/saving data
- extracting a class responsible for designing buttons/panels etc.

**Adding more constants**
- setting dimensions, fonts etc. as constants to keep track of any up-to-date changes
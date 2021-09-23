# Weather-Forecast
Android - Java - SQLite - Room - MVVM - REST API {Volley}

Overview

First, the application on the main screen contains 4 default cities, and each user can add the cities he wants and remove unwanted cities, and he can also remove all cities together with one click.

There is a search bar in it to facilitate the process of finding the city added to the list.

When you click on a specific city, it will move to another screen that contains the weather forecast, time, and date in this city, and also the background shows you whether this city is in the morning or the evening

The user can update the weather data for the desired country by swiping the screen down.

The weather forecast for the next 5 days is also shown on the screen, and some thumbnails show the expected weather condition.
(note: only the weather appears in the application
For the current day and two additional days because the site used has some restrictions (www.worldweatheronline.com) where it provides you with forecasts of 3 days only for free and to get forecasts for additional days you must pay a monthly subscription).

I’ve used the MVVM design pattern to organize the project and to avoid data loss and recall when rotating the screen etc.
And it was relied on the (SQLite database + room) to store the cities that the user enters also REST API used to get the weather data from the previous website and to facilitate the process of accessing the data stored in the database and API from anywhere within the application, singleton was used in their construction.

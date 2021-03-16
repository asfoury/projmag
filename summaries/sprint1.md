# Summary for week 1

## Mohamed
I implemented the user interface that shows the list of projects, it took about 8 hours, I was unable to test the view as I was not sure how to test.

## Giovanni
I implemented the UI that allow teacher to submit their project, it took me less than 3 hours.

## Luke
I implemented the sign in UI, connected it to the Google API to enable a log in with Google and made it such that the app opens to the project list if already signed in, otherwise it goes to the sign in page which then leads to the project list. It took me longer than the estimated 8 hours, approximately 10 hours.

## Arnaud
I created the Firebase project and setup all dependencies. It took me less than 3 hours. After finishing my task, I started reading documentations about firebase.

## Kaourintin (Scrum Master)
I implemented the project class and tag class. It took me around ten hours because I had to read a lot of kotlin doc to get used to the language and make sure that the way I was implementing things was in kotlin style and not in java style.
## Paul
I created an Interface for a Database of `Project`s and also an implementation of it using FireStore from Firebase, which required some time reading the FireStore API documentation. The base code is present an functional, but I still need to write tests (if possible as it has strong external dependencies). It took me more than the estimated 8h, until now.

## Overall Team
There was a misunderstanding with the way the week was structured which led us to a sprint presentation without any code merged on the server. We fixed this over the weekend. Everyone did their tasks, participated in multiple stand up meetings and nobody had major problems. We did have a lot of initial problems around branch bugs and cirrus problems but this should not be a problem in upcoming weeks. Test coverage is currently lower than the 80% required, this is because we have a lot of code which uses the Google or Firebase API and therefore cannot be tested, we will make sure to get overall test coverage over 80% ASAP.

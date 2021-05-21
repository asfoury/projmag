# Summary for sprint 10

## Mohamed (Scrum Master)
This sprint I had 2 tasks the first one was to implement the feature that makes students un-apply automatically to other projects they applied to once they are accepted to a project, I planned to work 4 hours on this task, but it ended up taking more time because I was trying to find a way to remove a firebase listener that was added to the candidature so that users no longer receive  updates after they unapply to a project this was difficult for the following reasons firebase does not offer a remove listener method they only let you remove the listener by calling a method on it which removes it. Because this was not implemented in the interface I had to change the interface to support that and then change all the classes that implement it, because we only do something when a user is accepted I realized that there is no need to remove the listener because if a user un-applies they can no longer be accepted and it was only in that case that the listener did something. If however we decide to use the listener for other things we would need to change a lot of code.  Also I had to understand a lot of code before started to code which was done in small meetings after the standups. This ended up taking about 10 hours so I was unable to start my second task

## Giovanni
This week first I finish my of the preview sprint which was not merge. I add some test which was easly but take me 
time because I need to better understand how the orientation work in the phone. Then I begin my second task, which take
which was  to find a way to send notification from device to another device to notify professor when student apply to their project,
this task me more time then expected because there is not many documentation about that topic. Need to better estimate my time
## Luke
This week I had 3 tasks. I completed the first one in more time than expected (6hrs instead of 3, did some CRs as well). I then looked into my second task (notifications) for an hour and a half with little luck, so I moved onto my third, making projects editable. This is not yet merged since I finished coding quite late (and having spent much more than 8hrs). I have so far spent about 5hrs on this task but it is not yet merged, this is not counting the CRs I've done. I need to estimate my time more correctly.

## Arnaud

## Kaourintin

## Paul
This week I continued and finished the setup for Hilt dependency injection.
I also tested my setup my implementing DI for the PreferencesActivity, which is working.

I had a lot of difficulties, by adapting other activities as the I didn't understand well when Dependency Injections where injected or bound with another value in tests. Next week I'll need to learn more about the unexpected behaviours of the Hilt framework.

## Overall Team
Overall we worked well this sprint and we did 3 stand up meetings, 
the only problem however is that some of us forgot a standup meeting so we had to postpone it.

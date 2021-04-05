# Summary for week 3

## Mohamed(Scrum Master)
This week I added some tests for the video selection when submitting a project and updated the UI of the project form submission to support the addition of the video and a space to play the video once it's selected. The main problem I encountered with this sprint is with the coverage of the testing. To test my code I need to select a video from the phone gallery which is not possible to do using Cirrus. Another problem I encountered is copying the video from the phone gallery to the apps internal storage as the information available online is either very new or uses deprecated code or does not work foll all versions of android, in the end we decided not to copy the video to the local app storage and upload it directly to FireBase. My time estimate was accurate this sprint.
## Giovanni
I allow user to add a video from gallery to the cv. I spent a lot of time to search how to use gallery and video api on android studio and to seach how to test the selection on th
gallery. I need next time to add more test in my code because my coverage was low

## Luke 
I have implemented deep linking. This has taken much more time than estimated. I have run into many issues. I configured the Firebase project to allow deep linking. I implemented the necessary dependencies, but these at first would not resolve. I then added the code necessary to manage a deep link but this was just caused the application to crash initially. I have spent roughly 10 hours on this task, even though it was estimated at 4, and that I could also perform another task during this sprint. I will undertake my remaining task next sprint along with another.

## Arnaud
I have implemented an offline database for projects (and modified how are organiszed projects file).  
I have change how the list of projects is displayed, it is now directly up to date without manual refresh.  
I added images and videos support to the project info UI, these are (if not already) downloaded to local storage.  
I also display the description of project as some basic HTML code.  
I have spent more than 16 hours this week, mostly because only the video part was my original task but I needed the others to continue.  
Next time I'll try to not add more work to my tasks.

## Kaourintin 
Improved the tagsbase. I actually spent a lot of time on the computer trying to understand my UI tasks but I just didn't manage to focus and wasted it. Work discipline is going down a blackhole need to find a solution to it. For next week I need to catch up this week's work and also manage the tasks that are given to me better. 

## Paul
This week I had first to configure `codeclimate` to relax the code duplication constraint. It was quite fast and easy to do, thanks to codeclimate docs.
My second task was to upload project, a video and link both together. This implementation was quite challenging and the testing even more. I had some issues with URI and had to modify the File Database interface.
In the following weeks, I will need to get more familiar with handling mutlimedia in android, and to learn more about Firebase services.

## Overall Team
This week we did 3 standup meetings and we can see that the amount of small meetings after each standup is increasing because we are modifying code that depends on other people. As for this weeks tasks we were not able to finish all tasks in time, next time we should try to select our tasks more accuretly so we can all finish our pull requests before the end of the sprint.


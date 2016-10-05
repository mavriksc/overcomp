# overcomp #

Currently this is a Spring Boot and JavaCV application that will recognize characters in Overwatch and return marked up image with what it thinks are the characters. This is incomplete but close. I need to add templates for dead characters and maybe some more preprocessing to fix false positives but is already surprisingly good. should be fixed within days. 

## Requirements ##
java 1.8  
libgomp (usually comes with git)  
on debian based systems `apt-get install libgomp1`  
cent/redhat: `yum install libgomp`  

## Running/ Packaging ##
I run from eclipse but you can also use maven from the command line.  
running : `mvn spring-boot:run`  
Build runnable jar: `mvn package`  

in eclipse i have a maven build config with current workspace and goals = "package" right click pom and run as build config... to make jar  

to run deployed
`java -jar overcomp-x.x.x-SNAPSHOT.jar` 


will "flash host" images from the img-proc folder. once viewed they are deleted. this is to prevent having to set up chron job or other persistence at this time. 

## future goals:  ##
(daily) finish the analysis template. add theming and thumbnailing  
(daily) ~~use a max in roi technique instead of over the whole image to elim false pos and duplicate in same region~~  **done**  
(daily) add dead characters template  
(daily) ~~complete counters matrix~~ and implement reqs based on comp  **done-ish**  
(near) it may be usefull to make the results page use ajax to load changes so that the image isn't lost and the recomendations can be updated.  
(near) build process. deploy to not snapshot. and not 8080   
(far future)on the analysis page when the user corrects the results the image will be saved and learned from. once many many examples good and bad a haar cascade can be created for the characters and use real machine learning to pick these out.   


## Possibilities:##
integrate with stats apis 
scrape letters for usernames from the screen and see who's best for the job

make an option to just run locally and monitor the screenshots folder for updates and display the analysis in a window. 
if you play with consistent team this could just be done in csv or embedded db(probably easier with spring boot on board)

currently pictures of the "tab" screen aren't easily processed. but after the AI is done it should be more accurate. 

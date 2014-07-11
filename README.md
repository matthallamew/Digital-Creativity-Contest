#Digital Creativity Contest


This is a generic Grails application that can be used as a judging/ranking platform.

##About
There are three main user groups in this application; contestants, judges, and administrators.  These three groups currently work as described below.  If this does not fit your needs, it can be modified with minimal effort. 

- Contestants

This group does not have to login.  They can submit projects they created themselves or on a team.  The application asks for an email address so they can be contacted later if they win.


- Judges

Users in this group are created by administrators and they have to login.  Eligible submissions are designated by a cut-off date and time.  Submissions after that point are not shown in the judges list of submissions to rank.  A judge can only rank a submission once.  Ranked submissions disappear from the list of unranked submissions.


- Administrators

Users in this group are created by other administrators. The application calculates a grand total score for each submission so administrators can easily see which submissions are ranked the highest per category.


##Future
The cut-off date, among other variables, will be controlled by the administrator and live in a configuration table in the database.  Right now, these live in the Grails configuration file.

Administrators have to manually send the winners an email from their own email client.  They should be able to email winners right from the application. 
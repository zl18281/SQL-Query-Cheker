
Instructions for deployment:

step 0: step into src/main/webapp and run "npm install"

step 1: download the latest maven build tool and add path environment

step 2: download the latest Tomcat server and write "bin/" to path environment

step 3: from root project folder, run: "mvn package". A "target" folder should be generated

step 4: copy the war file in "target" folder to "webapp" folder of Tomact server

step 5: run "startup.sh" of Tomcat

step 6: visit site via: http://localhost:8080/SQL

step 7: run "shutdown.sh" of Tomcat to stop the server

note:
there might be bugs related to differences between platforms, possible reasons might be:

1. some relative dir path in the program while touching files (on my pc, some classes(servlet, etc)' default root when touching files is the "bin/" dir of Tomcat).


Software Usage：

1. Login using MariaDB usernmae and password (like 'student'). Refresh the page, your username should be seen.
2. Press 'Init database', the databases which your MariaDB account has access should be available as a drop down list.
3. Choose one database from the list. At this time, when cursor is after 'FROM' keyword, all the tables will show up when you hover 'Code Hint'. Then Press 'Init table'. All tables in this db will show as a drop down list.
4. Select one table from the list. At this time, when cursor is after '.' or 'SELECT', all the columns will show up when you hover 'Code Hint'.
5. Type in some SQL. Then, pressing 'ANTLR' will give you feedback of syntax error, if any. Pressing 'tree' will show you the syntax tree of your code in a new window (as well as the text lisp style tree). Press 'Execute Query' will show resulting tables at the bottom.
6. Pressing 'Save' with a filename should save your script to /home/Downloads (on ubuntu, this is the case).


 

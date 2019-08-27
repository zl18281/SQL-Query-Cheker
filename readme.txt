
Instructions for deployment:

step 0: step into src/main/webapp and run "npm install"

step 1: download the latest maven build tool and add path environment

step 2: download the latest Tomcat server and set environment variable

step 3: from root project folder, run: "mvn package". A "target" folder should be generated

step 4: copy the war file in "target" folder to "webapp" folder of Tomact server

step 5: run "catalina.sh start" to start Tomcat server.

step 6: visit site via: http://localhost:8080/SQL (use Chrome Browser, or syntax tree will not show up)

step 7: run "catalina.sh stop" to stop the server

note:
there might be bugs related to differences between platforms or browsers, possible reasons might be:

1. The project is only tested on ubuntu Linux system with chrome browser. On windows or other browsers, there are unexpected behaviors.

2. some relative dir path in the program when touching files (on my pc, some classes(servlet, etc)' default root when touching files is the "bin/" dir of Tomcat). There might be file dir in the code specific to my own laptop for debugging purpose, thus on a different machine, there will be file not found exception.

3. MariaDB should be installed and configured as coursework one of "Databases". If not, semantic check is not possible.

Software Usage：

1. Login using MariaDB username and password (like 'student').
2. Press 'Init database', the databases which your MariaDB account has access to will be available as a drop-down list.
3. Choose one database from the list. At this time, when cursor is after 'FROM' keyword, all the tables in this chosen DB will show up when you hover 'Code Hint'. Then Press 'Init table'. All tables in this DB will shown as a drop-down list.
4. Select one table from the list. At this time, when cursor is after '.' or 'SELECT', all the columns will show up when you hover 'Code Hint'.
5. Type in some SQL. Then, clicking 'ANTLR' will give you feedback of syntax error. clicking 'semantic' will check semantic errors. Clicking 'tree' will show you the syntax tree of your code in a new window. Clicking 'Execute Query' will show resulting tables at the bottom.
6. Clicking 'Save' with a filename should save your script to /home/Downloads (on ubuntu, this is the case).

recent update:

1. Parsing and semantic checking is going on while typing. Result is updated continously.
2. When syntax is right, there will be semantic check of correctness of columns and tables, even for a "JOIN SELECT" with a "WHERE" clause.
3. When column is ambiguous, it is treated as semantic error. But with a table name(plus a ".") as prefix, it is treated as right. 
4. Column and table alias can be checked.

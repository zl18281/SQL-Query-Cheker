
Instructions for usage:

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
 

####
# One-time Setup
####

    unzip james-server-app-3.6.2-app.zip

## TLDR

For the 'davidb' user, operate in the 7000s range:

    cp -r conf-7000/* james-server-app-3.6.2/conf/.

    cd james-server-app-3.6.2

    ./bin/james start
    sleep 5 && ./bin/james status

    nmap -p0-10000 localhost
    
    ./bin/james-cli.sh -h localhost -p 7999 AddDomain email-ai.interactwith.us
    ./bin/james-cli.sh -h localhost -p 7999 AddUser davidb@email-ai.interactwith.us 12345
    ./bin/james-cli.sh -h localhost -p 7999 AddUser annetteb@email-ai.interactwith.us 12345

## In More Detail


Having unzipped the Java binary distribution, james-server-app-3.6.2-app.zip,
you need to change 4 config files so the various ports the email server
responds to are in a range that can be run by a regular user, and don't
conflict with other ports in use.

This approach will let you run and operate a web server that can send email
message between email users that are 'local' to this server.  Local in this
context means that the web-based email-client software (rainloop) is also
installed on this computer.  While the email addresses used have fully
qualified names such as 'testuser@email-ai.interactwith.us' they work
because behind the scenes (in the admin setting of rainloop) the IMAP
and SMTP serives the client specifies are 'localhost'.

Let's assume you want to run the email server on ports in the '2000s' range.

The config files to edit are:

  conf/imapserver.xml
  conf/pop3server.xml
  conf/smtpserver.xml
  conf/jmx.properties


For imapserver, set the port to 2143
For pop3server, set the port to 2110
For smtp,       set the port to 2525

AND for smtpserver.xml, uncomment:

        <!--
        <authRequired>true</authRequired>
         -->

For jmx.properties, set the port to 2999


To run the email server:

  ./bin/james start

Then view the log files:

  less logs/wrapper.log



To check what ports are open:

    nmap -p0-10000 localhost
    

To user the email server, you will need to create a domain and some users

  ./bin/james-cli.sh -h localhost -p 2999 AddDomain email-ai.interactwith.us
  ./bin/james-cli.sh -h localhost -p 2999 AddUser user1@email-ai.interactwith.us 12345
  ./bin/james-cli.sh -h localhost -p 2999 AddUser user2@email-ai.interactwith.us 12345

For futher details see:

  https://james.apache.org/server/install.html
  https://www.linuxbabe.com/redhat/run-your-own-email-server-centos-postfix-smtp-server


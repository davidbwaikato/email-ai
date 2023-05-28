#
# Preamble
#

Assuming you have gone through the Apache James email server setup
described in

    ../email-server/README.txt

And have setup a domain and (as a minimum) 2 email user accounts
to experiment with then ...

#
# Setup Rainloop Webmail Client
#

The rainloop installer checks for supporting packages, If any are
missing a message is generated identifying them.  This is how we
figured out that some additional php-flavoured packages needed to be
installed.

    sudo mkdir /var/www/html/rainloop-$USER
    sudo chown $USER /var/www/html/rainloop-$USER

    rainloop_installer_home="$PWD"
    
    pushd "/var/www/html/rainloop-$USER"
    /bin/cp "$rainloop_installer_home/rainloop-installer.php" .

    php ./rainloop-installer.php
	

#----
 [RainLoop Webmail Installer]


 * Connecting to repository ...
 * Downloading package ...
 * Complete downloading!
 * Installing package ...
 * Complete installing!

 * [Success] Installation is finished!
#----

    ls
#----
  data  index.php  rainloop
#----

Return to the directory with README.txt and rainloop-installer.txt

Rainloop requires 'data' to be restirct access so it is only accessible by the web-server

   chmod go-rwx data
   sudo chown -R www-data:www-data data


Now visit the rainloop admin login panel in your web browser:

    echo  http://email-ai.interactwith.us/interactwith.us/rainloop-$USER/?admin

At this stage, default password to log in, as noted in rainloop website

Through the rainloop admin console, add an email domain:

    email-ai.interactwith.us
    
    imap server is localhost, port is 2143
    smtp server is localhost, port is 2525

Select the 'test' button to confirm things are working

    popd

#
# Access an email account
#

Now log in to access an example mailbox

    echo  http://email-ai.interactwith.us/interactwith.us/rainloop-$USER/

Paste into web browser

Log in with your email, password is whatever you set of a user email account in *Apache* *James*


====


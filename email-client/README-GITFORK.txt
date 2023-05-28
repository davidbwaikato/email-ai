#
# Preamble
#

The README is written on the assumption you aready have an Apache
James email server setup.  If not, first go through the details
in:

    ../email-server/README.txt

TLDR:

Update james/conf so imap, pop3 smpt and jmx are running on
  ports that a regular user can use.

  Then go through setting up a domain and (as a minimum) 2 email
  user accounts for testing purposes
  

#
# Setup Rainloop Webmail Client
#

Get the source code by running:

    ./GIT-CLONE-RAINLOOP-WEBMAIL-AI.sh

Fix up file permissions, as the rainloop PHP code runs some
checks to make sure the 'data' directory is not widely accessible
on the file-system
 
    chmod go-rwx rainloop-webmail-ai/data
    sudo chown -R www-data:www-data rainloop-webmail-ai/data

    sudo mv rainloop-webmail-ai "/var/www/html/rainloop-ai-$USER"

    pushd "/var/www/html/rainloop-ai-$USER"

Now continue the installation by reading the details in:

    README-EMAIL-AI.txt



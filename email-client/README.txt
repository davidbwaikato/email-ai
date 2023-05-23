Assuming you have an Apache James Email server setup and running in the '2000s' range, then:


To create your own privately installed rainloop email client front end,
assuming your username is 'testuser'

# Remember the diretory where *this* README.text and the rainloop-installer.php files are
#
% source_dir="$PWD"

# Now create a directory for rainloop in the Apache2 web doc root area:
#
% pushd /var/www/html
% sudo mkdir rainloop-testuser
% sudo chown testuser rainloop-testuser
%
% cd rainloop-testuser/
% php "$source_dir/rainloop-installer.php"

       [RainLoop Webmail Installer]


 * Connecting to repository ...
 * Downloading package ...
 * Complete downloading!
 * Installing package ...
 * Complete installing!

 * [Success] Installation is finished!

% ls
data  index.php  rainloop  rainloop-installer.php

# Return to the directory with README.txt and rainloop-installer.txt
#
% popd

In a web browser, go to email-ai.interactwith.us/rainloop-testuser/?admin

default password to log in

add domain
imap server is localhost, port is 2143
smtp server is localhost, port is 2525

Then go to email-ai.interactwith.us/rainloop-uruserhere/


Log in with your email, password is whatever you set of a user email account in *james*

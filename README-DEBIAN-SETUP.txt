
sudo apt-get install emacs-nox wget npm

sudo apt-get install nmap

sudo apt-get install php

# There were a couple of other php-flavoured packages to install
# From memory, I think they were the following:
#
sudo apt-get install php-cURL php-dom

# Not clear if the php install setup apache2 with the php-module or
# not.  Certainly it was the case that after some earlier experimental
# work, when I thought to look, apache2 was setup to work with php


# Also not clear if installing php modules restarted the apache2 server
# Err on the side of caution:

sudo systemctl restart apache2

# List modules that are active
sudo apache2ctl -M



####
# One-Time Setup
####

npm install

# The above command installs the NodeJS packages/modules used by
# web-server-api.js, our top-level program for running a web server
# that can respond to API calls

# To allow the NodeJS sever (running on localhost) to be visible
# to the outside world, we are going to set up a ReverseProxy
# through the main public-facing Apache2 web server

# Add in the proxying mode to Apache2 web server:

  sudo /sbin/a2enmod proxy_http

# Now edit the Apache2 config file:

  sudo emacs /etc/apache2/sites-enabled/000-default.conf

  # and add after the logging entries:

        ProxyPass /api http://localhost:3000
        ProxyPassReverse /api http://localhost:3000

  # Finally, restart web server for changes to take affect

  sudo systemctl restart apache2


####
# Running web-server-api.js
####

# NodeJS is already installed on the VM this project uses (v12.22.12)

# You can start the server using:

  node ./web-server-api.js

The program defaults to http://localhost:3000


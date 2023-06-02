
if [ ! -f SETUP.bash ] ; then
    echo "" >&2
    echo "Failed to find: SETUP.bash" >&2
    echo "Have you remembered to create SETUP.bash based on SETUP.bash.in?" >&2
    echo "" >&2
    exit 1
fi

source ./SETUP.bash

node ./web-server-api.js



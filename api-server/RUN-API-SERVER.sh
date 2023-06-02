
if [ ! -f SETUP.bash ] ; then
    echo "" >&2
    echo "Failed to find: SETUP.bash" >&2
    echo "Have you remembered to create SETUP.bash based on SETUP.bash.in?" >&2
    echo "" >&2
    exit 1
fi

echo "----"
echo "Setting the following Environment Variables:"
source ./SETUP.bash
echo "----"

echo ""
echo "----"
echo "Running: node ./web-server-api.js"
echo "[Press ^C to stop the server running]"
echo "----"

node ./web-server-api.js



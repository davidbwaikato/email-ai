#!/bin/bash


if [ ! -f SETUP.bash ] ; then
    echo "" >&2
    echo "Failed to find: SETUP.bash" >&2
    echo "Have you remembered to create SETUP.bash based on SETUP.bash.in?" >&2
    echo "" >&2
    exit 1
fi

source ./SETUP.bash


curl -X POST \
     -H "Content-Type: application/json" \
     -d "@tldr-davidb.json" \
     "http://localhost:$EMAIL_AI_LOCAL_PORT/tldr" \
    | python3 -mjson.tool

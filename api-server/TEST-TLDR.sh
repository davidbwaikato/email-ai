#!/bin/bash


if [ "x$EMAIL_AI_LOCAL_PORT" = "x" ] ; then
    echo ""
    echo "Environment variable EMAIL_AI_LOCAL_PORT not set" >&2
    echo ""
    exit 1
fi


curl -X POST \
     -H "Content-Type: application/json" \
     -d "@tldr-davidb.json" \
     "http://localhost:$EMAIL_AI_LOCAL_PORT/tldr" \
    | python3 -mjson.tool

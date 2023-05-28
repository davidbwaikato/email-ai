#!/bin/bash

if [ ! -d rainloop-webmail-ai ] ; then
    git clone https://github.com/davidbwaikato/rainloop-webmail-ai.git
else
    echo ""
    echo "Skipping git clone as rainloop-webmail-ai/ already exists"
    echo ""
fi

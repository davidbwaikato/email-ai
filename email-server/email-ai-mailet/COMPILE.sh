#!/bin/bash

echo ""
echo "mvn -e package"
echo ""

mvn -e package

if [ $? = 0 ] ; then
    
    echo ""
    echo "Now run:"
    echo "    ./INSTALL.sh"
    echo "To copy the created jar file into the Apache james conf/lib area"
    echo ""
fi

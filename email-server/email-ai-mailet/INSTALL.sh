#!/bin/bash

echo ""
echo "Copying target/emailai-1.0-SNAPSHOT.jar to <james>/conf/lib"
echo ""

cp target/emailai-1.0-SNAPSHOT.jar  ../james-server-app-3.6.2/conf/lib/.

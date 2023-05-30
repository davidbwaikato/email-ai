
Working in the directory where this README file is ...

To recompile:

    mvn package

To add to James:

    cp target/emailai-1.0-SNAPSHOT.jar  ../james-server-app-3.6.2/conf/lib/.

Then Restart James

    pushd ../james-server-app-3.6.2
    ./bin/james restart
    popd



Working in the directory where this README file is ...

To recompile:

    mvn package

To add to James:

    cp target/emailai-1.0-SNAPSHOT.jar  ../james-server-app-3.6.2/conf/lib/.


Add to:
    ../james-server-app-3.6.2/conf/mailetcontainer.xml

Inside the first <processors> <process> element:

    <mailet match="All" class="org.example.EmailMailet"/>


Should look like:


  <processors>

    <!-- The root processor is a required processor - James routes all mail on the spool -->
    <!-- through this processor first. -->
    <!-- -->
    <!-- This configuration is a sample configuration for the root processor. -->
    <processor state="root" enableJmx="true">

      <mailet match="All" class="org.example.EmailMailet"/>




Then Restart James

    pushd ../james-server-app-3.6.2
    ./bin/james restart
    popd


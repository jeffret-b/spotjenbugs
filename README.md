# spotjenbugs
Provides detectors useful for plugins in the Jenkins project.

This is a PoC to demonstrate how we could create our own SpotBugs plugin to include Jenkins specific tests.

Currently this has two detectors:
* MyDetector -- The sample spotbugs plugin detector. It flags System.out.println. This is built using an OpcodeStackDetector. This can be tested by inserting a println in any plugin.
* CsrfDetector -- A custom CSRF detector for Jenkins. This is incomplete. It is hard to tell what would be necessary to tune this to catch the right methods and ignore the others. This one is tuned specifically to SECURITY-1691 in fortify-on-demand-uploader-plugin announced on 2020-07-02. This can be tested before and after the fix for SECURITY-1691. Commit "7c88b81" is sufficiently before.

Testing involves the following steps:
1. Build this plugin.
2. Add it to the target Jenkins plugin.
            <plugin>
                <groupId>com.github.spotbugs</groupId>
                <artifactId>spotbugs-maven-plugin</artifactId>
                <configuration>
                    <plugins>
                        <plugin>
                            <groupId>io.jenkins.spotbugs</groupId>
                            <artifactId>spotjenbugs</artifactId>
                            <version>1.0-SNAPSHOT</version>
                        </plugin>
                    </plugins>
                </configuration>
            </plugin>
3. Build the target plugin, being sure to run spotbugs.
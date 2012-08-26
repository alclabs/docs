package com.alcshare.docs

import com.controlj.green.addonsupport.access.Location
import com.controlj.green.addonsupport.access.SystemAccess
import spock.lang.Specification

/**
 * 
 */
class DocumentManagerTest extends Specification {
    def testBasicLoadConfiguration() {
        given:
           HashMap<String,List<DocumentReference>> docRefs = new HashMap<String,List<DocumentReference>>();
           SystemAccess access = Mock();
           Location location = Mock();

           StringReader testConfig = new StringReader('''\
refpath,title,docpath
#ahu1,This is a test,one/two/three
''')
           def action = new DocumentManager.LoadConfigurationAction(testConfig, docRefs)

        when:
           action.execute(access)
           def results = DocumentManager.INSTANCE.getReferencesForLocation("DBID:1:1492", docRefs)
           def result = results.getAt(0)

        then:
           access.resolveGQLPath("#ahu1") >> location
           location.getPersistentLookupString(true) >> "DBID:1:1492"
           result.docPath == "/one/two/three"
           result.getGqlPath() == "#ahu1"
           result.title == "This is a test"
    }
}

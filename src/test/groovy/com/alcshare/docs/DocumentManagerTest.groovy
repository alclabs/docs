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
Reference Path,Display Path,Title,Document Path,Path Type
"#ahu1","This is ,ignored","This is a test","one/two/three"
''')
           def action = new DocumentManager.LoadConfigurationAction(testConfig, docRefs)

        when:
           action.execute(access)
           def results = DocumentManager.INSTANCE.getReferencesForLocation("DBID:1:1492", docRefs)
           def result = results.getAt(0)

        then:
           results != null
           results.size() == 1
           access.resolveGQLPath("#ahu1") >> location
           location.getPersistentLookupString(true) >> "DBID:1:1492"
           result.docPath == "one/two/three"
           result.getGqlPath() == "#ahu1"
           result.title == "This is a test"
           result.URL == "/TEST/content/one/two/three"
    }

    def testTwoCustomColumns() {
        given:
           HashMap<String,List<DocumentReference>> docRefs = new HashMap<String,List<DocumentReference>>();
           SystemAccess access = Mock();
           Location location = Mock();

           StringReader testConfig = new StringReader('''\
"Reference Path","Display Path","Title","Document Path","Path Type",Category,Color
#ahu1,Ignored,This is a test,/one/two/three,,type1,red
another/vav,"This is ignored",Title2,"path with a space",doc,type2,blue
''')
           def action = new DocumentManager.LoadConfigurationAction(testConfig, docRefs)

        when:
           action.execute(access)
           def results = DocumentManager.INSTANCE.getReferencesForLocation("DBID:1:1492", docRefs)
           def result1 = results.getAt(0)
           results = DocumentManager.INSTANCE.getReferencesForLocation("DBID:1:1234", docRefs)
           def result2 = results.getAt(0)

        then:
           access.resolveGQLPath(_) >> location
           location.getPersistentLookupString(true) >>> ["DBID:1:1492", "DBID:1:1234"]
           result1.gqlPath == "#ahu1"
           result1.title == "This is a test"
           result1.docPath == "/one/two/three"
           result1.get('Category') == 'type1'
           result1.get('Color') == 'red'
           result1.URL == "/TEST/content/one/two/three"


           result2.gqlPath == "another/vav"
           result2.title == "Title2"
           result2.docPath == "path with a space"
           result2.get('Category') == 'type2'
           result2.get('Color') == 'blue'
           result2.URL == "/TEST/content/path%20with%20a%20space"
    }
}

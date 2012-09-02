package com.alcshare.docs.checks

import spock.lang.Specification
import com.alcshare.docs.DocumentReference
import com.controlj.green.addonsupport.access.Location
import com.alcshare.docs.util.AddOnInfoHelper
import com.alcshare.docs.util.AddOnFiles

public class MissingDocumentCheckTest extends Specification
{
   def setup()
   {
      // reset static state between tests. TODO: remove the static state!
      AddOnFiles.privateDirectoryReference.set(null);
      AddOnFiles.docDirectoryReference.set(null);
   }

   def "test performCheckOnRef with missing file"()
   {
      given:
         AddOnInfoHelper.addonNameRef.set("addonname")
         AddOnInfoHelper.addonPublicDirRef.set(new File("/install/webroot/system/webapp_public/addonname"))
         def check = new MissingDocumentCheck()

      when:
         def result = check.performCheckOnRef(new DocumentReference("#foo/bar", "", "/not/a/real/path", "", Mock(Location), [:]))
      then:
         result.gqlPath == "#foo/bar"
         result.docPath == "/not/a/real/path"
         result.getText() == 'Missing document.  Referenced from "#foo/bar", file "/not/a/real/path" could not be found (looked for it at "'+new File("/install/webroot/system/webapp_data/addonname/private/docs/not/a/real/path").absolutePath+'"'
   }

   def "test performCheckOnRef with real file"()
   {
      given:
         File tempDir = File.createTempFile("doccheck", "test")
         tempDir.delete() // createTempFile actually _creates_ the file, so delete it (we want it for a dir)
         File publicDir = new File(tempDir, "webroot/system/webapp_public/addonname")
         publicDir.mkdirs()
         File privateDocsDir = new File(tempDir, "webroot/system/webapp_data/addonname/private/docs")
         privateDocsDir.mkdirs()
         File testFile = new File(privateDocsDir, "myfile.txt")
         testFile.createNewFile()

         AddOnInfoHelper.addonNameRef.set("addonname")
         AddOnInfoHelper.addonPublicDirRef.set(publicDir)
         def check = new MissingDocumentCheck()

      when:
         def result = check.performCheckOnRef(new DocumentReference("#foo/bar", "", "myfile.txt", "", Mock(Location), [:]))
      then:
         result == null

      cleanup:
         tempDir.deleteDir()
   }

   def "test performCheckOnList"()
   {
      given:
         AddOnInfoHelper.addonNameRef.set("addonname")
         AddOnInfoHelper.addonPublicDirRef.set(new File("/install/webroot/system/webapp_public/addonname"))
         def check = new MissingDocumentCheck()

      when: "a list of document references is given"
         def results = check.performCheckOnList([new DocumentReference("#foo/bar", "", "/not/a/real/path", "", Mock(Location), [:]),
                                                 new DocumentReference("#foo/baz", "", "/also/not/a/real/path", "", Mock(Location), [:]),
                                                ])
      then: "results for a list should be returned"
         results*.gqlPath == ["#foo/bar", "#foo/baz"]
         results*.docPath == ["/not/a/real/path", "/also/not/a/real/path"]
   }
}

package com.alcshare.docs.util

import spock.lang.Specification
import javax.servlet.ServletContext

/**
 * 
 */
class FileResourceTest extends Specification {
    def testGetFileResourcesBeneathContextPath() {
        given:
            ServletContext context = Mock()
            File targetDir = new File("/target")

        when:
            Collection<FileResource> resources = FileResource.getFileResourcesBeneathContextPath(context,
                    "/WEB-INF/img", targetDir, false)

        then:
            context.getResourcePaths("/WEB-INF/img") >> ["/WEB-INF/img/test.png", "/WEB-INF/img/deeper/deep.png"]
            resources.size() == 2
            resources[0].resourceFile.path == "/target/test.png"
            resources[1].resourceFile.path == "/target/deeper/deep.png"
    }
}

package com.sourceplusplus.mapper.vcs.git

import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixture4TestCase
import jp.ac.titech.c.se.stein.PorcelainAPI
import jp.ac.titech.c.se.stein.core.Context
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.internal.storage.file.FileRepository
import org.eclipse.jgit.lib.Constants
import org.eclipse.jgit.lib.ObjectId
import org.intellij.lang.annotations.Language
import org.junit.Test
import java.io.File

class MethodRenameTest : LightPlatformCodeInsightFixture4TestCase() {

    @Test
    fun `rename java getter method`() {
//        if (File("/tmp/git-repo").exists()) File("/tmp/git-repo").deleteRecursively()
//        val git = Git.init().setDirectory(File("/tmp/git-repo")).call()
//        @Language("Java") val code = """
//                public class GetterMethod {
//                    private String str;
//                    public String getStr() {
//                        return str;
//                    }
//                }
//            """.trimIndent()
//        File(git.repository.directory.parent, "GetterMethod.java").writeText(code)
//        git.add().addFilepattern(".").call()
//        git.commit().setMessage("Initial commit").call()
//
//        @Language("Java") val renamedCode = """
//                public class GetterMethod {
//                    private String str;
//                    public String getStr2() {
//                        return str;
//                    }
//                }
//            """.trimIndent()
//        File(git.repository.directory.parent, "GetterMethod.java").writeText(renamedCode)
//        git.add().addFilepattern(".").call()
//        git.commit().setMessage("Renamed method").call()
//
//        val fileRepo = FileRepository("/tmp/git-repo/.git")
//        val mapper = GitRepositoryMapper(project)
//        mapper.initialize(fileRepo, fileRepo)
//        mapper.rewrite(Context.init())
//        PorcelainAPI(fileRepo).use {
//            it.resetHard()
//            it.clean()
//        }
//
//        val head: ObjectId = fileRepo.resolve(Constants.HEAD)
//        val log = git.log().add(head).addPath("GetterMethod.getStr2().mjava").call()
//        log.forEach {
//            println(it.fullMessage)
//        }
//
//        //git log --follow "Foo#bar().mjava"
//        ////git log --all --full-history -- <path-to-file>
//
//        val finerMethodFile = File("/tmp/git-repo/GetterMethod.getStr().mjava")
//        assertExists(finerMethodFile)
//        assertEquals(
//            """
//            public
//            String
//            getStr
//            (
//            )
//            {
//            return
//            str
//            ;
//            }
//        """.trimIndent(), finerMethodFile.readText().trimIndent()
//        )
    }
}
import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixture4TestCase
import com.sourceplusplus.mapper.vcs.git.GitRepositoryMapper
import jp.ac.titech.c.se.stein.PorcelainAPI
import jp.ac.titech.c.se.stein.core.Context
import junit.framework.TestCase
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.internal.storage.file.FileRepository
import org.junit.Test
import java.io.File

class Tester : LightPlatformCodeInsightFixture4TestCase() {

    @Test
    fun `tokenized java getter method`() {
        if (File("/tmp/git-repo").exists()) File("/tmp/git-repo").deleteRecursively()
        Git.init().setDirectory(File("/tmp/git-repo")).call().use { git ->
            println("Created repository: " + git.repository.directory)
            File(git.repository.directory.parent, "GetterMethod.java").writeText(
                """
                public class GetterMethod {
                    private String str;
                    public String getStr() {
                        return str;
                    }
                }
            """.trimIndent()
            )
            git.add().addFilepattern(".").call()
            git.commit().setMessage("Initial commit").call()
        }

        val fileRepo = FileRepository("/tmp/git-repo/.git")
        val mapper = GitRepositoryMapper(project)
        mapper.initialize(fileRepo, fileRepo)
        mapper.rewrite(Context.init())

        PorcelainAPI(fileRepo).use {
            it.resetHard()
            it.clean()
        }

        val finerMethodFile = File("/tmp/git-repo/GetterMethod.getStr().mjava")
        assertExists(finerMethodFile)
        TestCase.assertEquals(
            """
            public
            String
            getStr
            (
            )
            {
            return
            str
            ;
            }
        """.trimIndent(), finerMethodFile.readText().trimIndent()
        )
    }

    @Test
    fun `tokenized groovy getter method`() {
        if (File("/tmp/git-repo").exists()) File("/tmp/git-repo").deleteRecursively()
        Git.init().setDirectory(File("/tmp/git-repo")).call().use { git ->
            println("Created repository: " + git.repository.directory)
            File(git.repository.directory.parent, "GetterMethod.groovy").writeText(
                """
                public class GetterMethod {
                    private String str
                    public String getStr() {
                        return str
                    }
                }
            """.trimIndent()
            )
            git.add().addFilepattern(".").call()
            git.commit().setMessage("Initial commit").call()
        }

        val fileRepo = FileRepository("/tmp/git-repo/.git")
        val mapper = GitRepositoryMapper(project)
        mapper.initialize(fileRepo, fileRepo)
        mapper.rewrite(Context.init())

        PorcelainAPI(fileRepo).use {
            it.resetHard()
            it.clean()
        }

        val finerMethodFile = File("/tmp/git-repo/GetterMethod.getStr().mgroovy")
        assertExists(finerMethodFile)
        TestCase.assertEquals(
            """
            public
            String
            getStr
            (
            )
            {
            return
            str
            }
        """.trimIndent(), finerMethodFile.readText().trimIndent()
        )
    }

//    @Test
//    fun `tokenized kotlin getter method`() {
//        if (File("/tmp/git-repo").exists()) File("/tmp/git-repo").deleteRecursively()
//        Git.init().setDirectory(File("/tmp/git-repo")).call().use { git ->
//            println("Created repository: " + git.repository.directory)
//            File(git.repository.directory.parent, "GetterMethod.kt").writeText(
//                """
//                class GetterMethod(private val str: String) {
//                    fun getStr(): String {
//                        return str
//                    }
//                }
//            """.trimIndent()
//            )
//            git.add().addFilepattern(".").call()
//            git.commit().setMessage("Initial commit").call()
//        }
//
//        val fileRepo = FileRepository("/tmp/git-repo/.git")
//        val mapper = GitRepositoryMapper(project)
//        mapper.initialize(fileRepo, fileRepo)
//        mapper.rewrite(Context.init())
//
//        PorcelainAPI(fileRepo).use {
//            it.resetHard()
//            it.clean()
//        }
//
//        val finerMethodFile = File("/tmp/git-repo/GetterMethod.getStr().mjava")
//        assertExists(finerMethodFile)
//        TestCase.assertEquals(
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
//    }
}
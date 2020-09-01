import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixture4TestCase
import com.sourceplusplus.mapper.vcs.git.GitRepositoryMapper
import jp.ac.titech.c.se.stein.PorcelainAPI
import jp.ac.titech.c.se.stein.core.Context
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.internal.storage.file.FileRepository
import org.junit.Test
import java.io.File

class Tester : LightPlatformCodeInsightFixture4TestCase() {

    @Test
    fun `modify existing directory`() {
        if (File("/tmp/journey").exists()) {
            File("/tmp/journey").deleteRecursively()
        }
        Git.cloneRepository().setURI("https://github.com/codebrig/journey").setDirectory(File("/tmp/journey")).call()

        val fileRepo = FileRepository("/tmp/journey/.git")
        val mapper = GitRepositoryMapper(project)
        mapper.initialize(fileRepo, fileRepo)
        mapper.rewrite(Context.init())

        PorcelainAPI(fileRepo).use {
            it.resetHard()
            it.clean()
        }
    }
}
package com.sourceplusplus.mapper.vcs.git

import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixture4TestCase
import com.sourceplusplus.mapper.api.impl.SourceMapperImpl
import com.sourceplusplus.protocol.artifact.ArtifactQualifiedName
import com.sourceplusplus.protocol.artifact.ArtifactType
import jp.ac.titech.c.se.stein.PorcelainAPI
import jp.ac.titech.c.se.stein.core.Context
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.internal.storage.file.FileRepository
import org.eclipse.jgit.lib.*
import org.intellij.lang.annotations.Language
import org.junit.Test
import java.io.File

class MethodRenameTest : LightPlatformCodeInsightFixture4TestCase() {

    @Test
    fun `java get original method name`() {
        if (File("/tmp/git-repo").exists()) File("/tmp/git-repo").deleteRecursively()
        val git = Git.init().setDirectory(File("/tmp/git-repo")).call()
        @Language("Java") val code = """
            public class GetterMethod {
                private String str;
                public String getStr() {
                    return str;
                }
            }
            """.trimIndent()
        File(git.repository.directory.parent, "GetterMethod.java").writeText(code)
        git.add().addFilepattern(".").call()
        git.commit().setMessage("Initial commit").call()

        @Language("Java") val renamedCode = """
            public class GetterMethod {
                private String str;
                public String getStr2() {
                    return str;
                }
            }
            """.trimIndent()
        File(git.repository.directory.parent, "GetterMethod.java").writeText(renamedCode)
        git.add().addFilepattern(".").call()
        git.commit().setMessage("Renamed method").call()

        val fileRepo = FileRepository("/tmp/git-repo/.git")
        val mapper = GitRepositoryMapper(project)
        mapper.initialize(fileRepo, fileRepo)
        mapper.rewrite(Context.init())
        PorcelainAPI(fileRepo).use {
            it.resetHard()
            it.clean()
        }

        val newCommitId = fileRepo.resolve(Constants.HEAD).name
        val oldCommitId = fileRepo.resolve("$newCommitId^1").name
        val oldName = ArtifactQualifiedName(
            identifier = "GetterMethod.getStr()",
            commitId = oldCommitId,
            type = ArtifactType.METHOD
        )
        val newName = SourceMapperImpl(git, fileRepo)
            .getMethodQualifiedName(oldName, newCommitId)

        assertNotNull(newName)
        assertEquals("GetterMethod.getStr2()", newName.identifier)
        assertEquals(newCommitId, newName.commitId)
        assertEquals("GetterMethod.getStr()", oldName.identifier)
        assertEquals(oldCommitId, oldName.commitId)
    }

    @Test
    fun `java get updated method name`() {
        if (File("/tmp/git-repo").exists()) File("/tmp/git-repo").deleteRecursively()
        val git = Git.init().setDirectory(File("/tmp/git-repo")).call()
        @Language("Java") val code = """
            public class GetterMethod {
                private String str;
                public String getStr() {
                    return str;
                }
            }
            """.trimIndent()
        File(git.repository.directory.parent, "GetterMethod.java").writeText(code)
        git.add().addFilepattern(".").call()
        git.commit().setMessage("Initial commit").call()

        @Language("Java") val renamedCode = """
            public class GetterMethod {
                private String str;
                public String getStr2() {
                    return str;
                }
            }
            """.trimIndent()
        File(git.repository.directory.parent, "GetterMethod.java").writeText(renamedCode)
        git.add().addFilepattern(".").call()
        git.commit().setMessage("Renamed method").call()

        val fileRepo = FileRepository("/tmp/git-repo/.git")
        val mapper = GitRepositoryMapper(project)
        mapper.initialize(fileRepo, fileRepo)
        mapper.rewrite(Context.init())
        PorcelainAPI(fileRepo).use {
            it.resetHard()
            it.clean()
        }

        val newCommitId = fileRepo.resolve(Constants.HEAD).name
        val oldCommitId = fileRepo.resolve("$newCommitId^1").name
        val newName = ArtifactQualifiedName(
            identifier = "GetterMethod.getStr2()",
            commitId = newCommitId,
            type = ArtifactType.METHOD
        )
        val oldName = SourceMapperImpl(git, fileRepo)
            .getMethodQualifiedName(newName, oldCommitId)

        assertNotNull(oldName)
        assertEquals("GetterMethod.getStr()", oldName.identifier)
        assertEquals(oldCommitId, oldName.commitId)
        assertEquals("GetterMethod.getStr2()", newName.identifier)
        assertEquals(newCommitId, newName.commitId)
    }
}

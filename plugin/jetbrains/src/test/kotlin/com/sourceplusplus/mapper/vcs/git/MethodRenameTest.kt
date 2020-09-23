package com.sourceplusplus.mapper.vcs.git

import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixture4TestCase
import com.sourceplusplus.mapper.api.impl.SourceMapperImpl
import com.sourceplusplus.protocol.artifact.ArtifactQualifiedName
import com.sourceplusplus.protocol.artifact.ArtifactType
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.internal.storage.file.FileRepository
import org.eclipse.jgit.lib.Constants
import org.intellij.lang.annotations.Language
import org.junit.Test
import java.io.File

class MethodRenameTest : LightPlatformCodeInsightFixture4TestCase() {

    @Test
    fun `java get original method name`() {
        if (File("/tmp/git-repo").exists()) File("/tmp/git-repo").deleteRecursively()
        Git.init().setDirectory(File("/tmp/git-repo")).call().use { git ->
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
        }

        val gitMapper = GitRepositoryMapper(project)
        gitMapper.initialize(FileRepository("/tmp/git-repo/.git"))

        val newCommitId = gitMapper.targetRepo.resolve(Constants.HEAD).name
        val oldCommitId = gitMapper.targetRepo.resolve("$newCommitId^1").name
        val oldName = ArtifactQualifiedName(
            identifier = "GetterMethod.getStr()",
            commitId = oldCommitId,
            type = ArtifactType.METHOD
        )
        val newName = SourceMapperImpl(gitMapper)
            .getMethodQualifiedName(oldName, newCommitId)

        assertNotNull(newName)
        assertEquals("GetterMethod.getStr2()", newName.identifier)
        assertEquals(newCommitId, newName.commitId)
        assertEquals("GetterMethod.getStr()", oldName.identifier)
        assertEquals(oldCommitId, oldName.commitId)
        gitMapper.targetGit.close()
        gitMapper.targetSourceDirectory.deleteRecursively()
    }

    @Test
    fun `java get updated method name`() {
        if (File("/tmp/git-repo").exists()) File("/tmp/git-repo").deleteRecursively()
        Git.init().setDirectory(File("/tmp/git-repo")).call().use { git ->
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
        }

        val gitMapper = GitRepositoryMapper(project)
        gitMapper.initialize(FileRepository("/tmp/git-repo/.git"))

        val newCommitId = gitMapper.targetRepo.resolve(Constants.HEAD).name
        val oldCommitId = gitMapper.targetRepo.resolve("$newCommitId^1").name
        val newName = ArtifactQualifiedName(
            identifier = "GetterMethod.getStr2()",
            commitId = newCommitId,
            type = ArtifactType.METHOD
        )
        val oldName = SourceMapperImpl(gitMapper)
            .getMethodQualifiedName(newName, oldCommitId)

        assertNotNull(oldName)
        assertEquals("GetterMethod.getStr()", oldName.identifier)
        assertEquals(oldCommitId, oldName.commitId)
        assertEquals("GetterMethod.getStr2()", newName.identifier)
        assertEquals(newCommitId, newName.commitId)
        gitMapper.targetGit.close()
        gitMapper.targetSourceDirectory.deleteRecursively()
    }
}

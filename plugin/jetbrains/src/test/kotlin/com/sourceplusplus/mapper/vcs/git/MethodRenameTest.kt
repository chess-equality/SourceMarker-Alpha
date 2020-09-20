package com.sourceplusplus.mapper.vcs.git

import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixture4TestCase
import jp.ac.titech.c.se.stein.PorcelainAPI
import jp.ac.titech.c.se.stein.core.Context
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.diff.DiffConfig
import org.eclipse.jgit.diff.DiffEntry
import org.eclipse.jgit.internal.storage.file.FileRepository
import org.eclipse.jgit.lib.Config
import org.eclipse.jgit.lib.Constants
import org.eclipse.jgit.lib.ObjectId
import org.eclipse.jgit.revwalk.FollowFilter
import org.eclipse.jgit.revwalk.RenameCallback
import org.eclipse.jgit.revwalk.RevWalk
import org.intellij.lang.annotations.Language
import org.junit.Test
import java.io.File
import java.util.*

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

        val diffs: MutableList<DiffEntry> = ArrayList<DiffEntry>()
        RevWalk(fileRepo).use { rw ->
            val head: ObjectId = fileRepo.resolve(Constants.HEAD)
            val config: Config = fileRepo.config
            config.setBoolean("diff", null, "renames", true)
            val dc: DiffConfig = config.get(DiffConfig.KEY)
            val followFilter: FollowFilter = FollowFilter.create("GetterMethod.getStr2().mjava", dc)
            followFilter.renameCallback = object : RenameCallback() {
                override fun renamed(entry: DiffEntry) {
                    diffs.add(entry)
                }
            }
            rw.treeFilter = followFilter
            rw.markStart(rw.parseCommit(head))
            rw.count() //force iteration
        }

        assertEquals(1, diffs.size)
        assertEquals(DiffEntry.ChangeType.RENAME, diffs[0].changeType)
        assertEquals("GetterMethod.getStr().mjava", diffs[0].oldPath)
        assertEquals("GetterMethod.getStr2().mjava", diffs[0].newPath)
    }

//    @Test
//    fun `java get updated method name`() {
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
//        val diffs: MutableList<DiffEntry> = ArrayList<DiffEntry>()
//        RevWalk(fileRepo).use { rw ->
////            val files = DiffEntry.scan(rw.tr)
//
//            var diffs = git.diff()
//                .setOldTree(prepareTreeParser(repository, oldCommit))
//                .setNewTree(prepareTreeParser(repository, newCommit))
//                .setPathFilter(PathFilterGroup.createFromStrings(arrayOf("new/b.txt", "b.txt")))
//                .call()
//            val rd = RenameDetector(repository)
//            rd.addAll(diffs)
//            diffs = rd.compute()
//
//
//            val head: ObjectId = fileRepo.resolve(Constants.HEAD)
//            val config: Config = fileRepo.config
//            config.setBoolean("diff", null, "renames", true)
//            val dc: DiffConfig = config.get(DiffConfig.KEY)
//            val followFilter: FollowFilter = FollowFilter.create("GetterMethod.getStr().mjava", dc)
//            followFilter.renameCallback = object : RenameCallback() {
//                override fun renamed(entry: DiffEntry) {
//                    diffs.add(entry)
//                }
//            }
//            rw.treeFilter = followFilter
//            rw.markStart(rw.parseCommit(head))
//            rw.count() //force iteration
//        }
//
//        assertEquals(1, diffs.size)
//        assertEquals(DiffEntry.ChangeType.RENAME, diffs[0].changeType)
//        assertEquals("GetterMethod.getStr().mjava", diffs[0].oldPath)
//        assertEquals("GetterMethod.getStr2().mjava", diffs[0].newPath)
//        commit_logs()
//    }

//    @Throws(IOException::class, NoHeadException::class, GitAPIException::class)
//    fun commit_logs() {
//        val logMessages: MutableList<String> = ArrayList()
//        val builder = FileRepositoryBuilder()
//        val repo: Repository = builder.setGitDir(File("/tmp/git-repo" + "/.git"))
//            .setMustExist(true).build()
//        val git = Git(repo)
//        val log: Iterable<RevCommit> = git.log().call()
//        var previousCommit: RevCommit? = null
//        for (commit in log) {
//            if (previousCommit != null) {
//                val diffEntries = git.diff()
//                    .setOldTree(getCanonicalTreeParser(git, previousCommit))
//                    .setNewTree(getCanonicalTreeParser(git, commit))
//                    .setPathFilter(PathFilter.create("GetterMethod.getStr2().mjava"))
//                    .call()
//                println(diffEntries)
//            }
////            if (previousCommit != null) {
//////                val oldTreeIterator: AbstractTreeIterator = getCanonicalTreeParser(git, previousCommit)
//////                val newTreeIterator: AbstractTreeIterator = getCanonicalTreeParser(git, commit)
//////                val outputStream: OutputStream = ByteArrayOutputStream()
//////                DiffFormatter(outputStream).use { formatter ->
//////                    formatter.setRepository(git.getRepository())
//////                    formatter.format(oldTreeIterator, newTreeIterator)
//////                }
//////                val diff: String = outputStream.toString()
//////                println(diff)
////            }
//            println("LogCommit: $commit")
//            val logMessage = commit.fullMessage
//            println("LogMessage: $logMessage")
//            logMessages.add(logMessage.trim { it <= ' ' })
//            previousCommit = commit
//        }
//        git.close()
//    }
//
//
//    @Throws(IOException::class)
//    private fun getCanonicalTreeParser(git: Git, commitId: ObjectId): AbstractTreeIterator {
//        RevWalk(git.getRepository()).use { walk ->
//            val commit: RevCommit = walk.parseCommit(commitId)
//            val treeId = commit.tree.id
//            git.getRepository().newObjectReader().use { reader -> return CanonicalTreeParser(null, reader, treeId) }
//        }
//    }
}

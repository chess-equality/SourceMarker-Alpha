import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.PsiFileFactory
import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixture4TestCase
import org.jetbrains.kotlin.idea.KotlinFileType
import org.jetbrains.kotlin.psi.KtFile
import org.junit.Test

class Tester : LightPlatformCodeInsightFixture4TestCase() {

    @Test
    fun `KTFile test`(){
        val newFile = PsiFileFactory.getInstance(project).createFileFromText(
            "NewKotlinFile.kt", KotlinFileType.INSTANCE as FileType, "class KotlinClass"
        ) as KtFile

        println(newFile)
    }
}
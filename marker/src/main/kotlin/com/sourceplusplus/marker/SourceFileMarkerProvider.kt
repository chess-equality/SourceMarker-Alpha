package com.sourceplusplus.marker

import com.intellij.psi.PsiFile

/**
 * todo: description
 *
 * @version 0.1.4
 * @since 0.1.0
 * @author [Brandon Fergerson](mailto:brandon@srcpl.us)
 */
interface SourceFileMarkerProvider {

    fun createSourceFileMarker(psiFile: PsiFile): SourceFileMarker {
        return SourceFileMarker(psiFile)
    }
}
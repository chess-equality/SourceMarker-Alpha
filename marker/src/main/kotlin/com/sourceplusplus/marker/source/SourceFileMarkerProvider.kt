package com.sourceplusplus.marker.source

import com.intellij.psi.PsiFile

/**
 * todo: description
 *
 * @version 0.2.2
 * @since 0.1.0
 * @author [Brandon Fergerson](mailto:brandon@srcpl.us)
 */
interface SourceFileMarkerProvider {

    fun createSourceFileMarker(psiFile: PsiFile): SourceFileMarker {
        return SourceFileMarker(psiFile)
    }
}
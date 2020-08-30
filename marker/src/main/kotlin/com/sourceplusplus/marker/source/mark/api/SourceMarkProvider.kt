package com.sourceplusplus.marker.source.mark.api

import org.jetbrains.uast.UClass
import org.jetbrains.uast.UMethod

/**
 * todo: description
 *
 * @version 0.1.4
 * @since 0.1.0
 * @author [Brandon Fergerson](mailto:brandon@srcpl.us)
 */
interface SourceMarkProvider {

    fun createSourceMark(psiMethod: UMethod, type: SourceMark.Type): SourceMark
    fun createSourceMark(psiClass: UClass, type: SourceMark.Type): SourceMark
}
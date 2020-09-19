package com.sourceplusplus.portal.server.display

import com.google.common.cache.CacheBuilder
import com.google.common.cache.CacheLoader
import com.google.common.cache.LoadingCache
import org.slf4j.LoggerFactory
import java.io.Closeable
import java.util.*

import java.util.concurrent.TimeUnit

/**
 * Represents a view into a specific source artifact.
 *
 * @version 0.3.2
 * @since 0.2.0
 * @author <a href="mailto:brandon@srcpl.us">Brandon Fergerson</a>
 */
class SourcePortal(
    val portalUuid: String,
    val appUuid: String,
    val external: Boolean
) : Closeable {

    companion object {
        private val log = LoggerFactory.getLogger(SourcePortal::class.java)

        val portalMap: LoadingCache<String, SourcePortal> = CacheBuilder.newBuilder()
            .expireAfterAccess(5, TimeUnit.MINUTES)
            .build(object : CacheLoader<String, SourcePortal>() {
                override fun load(portalUuid: String): SourcePortal? {
                    return getPortal(portalUuid)
                }
            })

        fun ensurePortalActive(portal: SourcePortal) {
            log.debug("Keep alive portal: " + Objects.requireNonNull(portal).portalUuid)
            portalMap.refresh(portal.portalUuid)
            log.debug("Active portals: " + portalMap.size())
        }

        fun getInternalPortal(appUuid: String, artifactQualifiedName: String): Optional<SourcePortal> {
            return Optional.ofNullable(portalMap.asMap().values.find {
                it.appUuid == appUuid && it.portalUI.viewingPortalArtifact == artifactQualifiedName && !it.external
            })
        }

        fun getSimilarPortals(portal: SourcePortal): List<SourcePortal> {
            return portalMap.asMap().values.filter {
                it.appUuid == portal.appUuid &&
                        it.portalUI.viewingPortalArtifact == portal.portalUI.viewingPortalArtifact &&
                        it.portalUI.currentTab == portal.portalUI.currentTab
            }
        }

        fun getExternalPortals(): List<SourcePortal> {
            return portalMap.asMap().values.filter { it.external }
        }

        fun getExternalPortals(appUuid: String): List<SourcePortal> {
            return portalMap.asMap().values.filter {
                it.appUuid == appUuid && it.external
            }
        }

        fun getExternalPortals(appUuid: String, artifactQualifiedName: String): List<SourcePortal> {
            return portalMap.asMap().values.filter {
                it.appUuid == appUuid && it.portalUI.viewingPortalArtifact == artifactQualifiedName && it.external
            }
        }

        fun getPortals(appUuid: String, artifactQualifiedName: String): List<SourcePortal> {
            return portalMap.asMap().values.filter {
                it.appUuid == appUuid && it.portalUI.viewingPortalArtifact == artifactQualifiedName
            }
        }

        fun register(appUuid: String, artifactQualifiedName: String, external: Boolean): String {
            return register(UUID.randomUUID().toString(), appUuid, artifactQualifiedName, external)
        }

        fun register(portalUuid: String, appUuid: String, artifactQualifiedName: String, external: Boolean): String {
            val portal = SourcePortal(portalUuid, Objects.requireNonNull(appUuid), external)
            portal.portalUI = PortalUI(portalUuid)
            portal.portalUI.viewingPortalArtifact = Objects.requireNonNull(artifactQualifiedName)

            portalMap.put(portalUuid, portal)
            log.info("Registered external Source++ Portal. Portal UUID: $portalUuid - App UUID: $appUuid - Artifact: $artifactQualifiedName")
            log.info("Active portals: " + portalMap.size())
            return portalUuid
        }

        fun getPortals(): List<SourcePortal> {
            return ArrayList(portalMap.asMap().values)
        }

        fun getPortal(portalUuid: String): SourcePortal? {
            return portalMap.getIfPresent(portalUuid)
        }
    }

    lateinit var portalUI: PortalUI

    override fun close() {
        log.info("Closed portal: $portalUuid")
        portalMap.invalidate(portalUuid)
        log.info("Active portals: " + portalMap.size())
    }

    fun createExternalPortal(): SourcePortal {
        val portalClone = getPortal(register(appUuid, portalUI.viewingPortalArtifact, true))!!
        portalClone.portalUI.cloneUI(portalUI)
        return portalClone
    }
}

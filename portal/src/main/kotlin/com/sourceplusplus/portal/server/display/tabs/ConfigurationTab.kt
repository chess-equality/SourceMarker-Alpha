//package com.sourceplusplus.portal.display.tabs
//
//import com.sourceplusplus.api.bridge.PluginBridgeEndpoints
//import com.sourceplusplus.api.model.artifact.SourceArtifact
//import com.sourceplusplus.api.model.artifact.SourceArtifactConfig
//import com.sourceplusplus.api.model.config.SourcePortalConfig
//import com.sourceplusplus.portal.server.display.SourcePortal
//import com.sourceplusplus.portal.server.display.PortalTab
//import groovy.util.logging.Slf4j
//import io.vertx.core.json.Json
//
//import static com.sourceplusplus.api.util.ArtifactNameUtils.getShortQualifiedFunctionName
//
///**
// * Used to display and configure a given source code artifact.
// *
// * @version 0.3.2
// * @since 0.2.0
// * @author <a href="mailto:brandon@srcpl.us">Brandon Fergerson</a>
// */
//@Slf4j
//class ConfigurationTab extends AbstractTab {
//
//    public static final String CONFIGURATION_TAB_OPENED = "ConfigurationTabOpened"
//    public static final String DISPLAY_ARTIFACT_CONFIGURATION = "DisplayArtifactConfiguration"
//    public static final String UPDATE_ARTIFACT_ENTRY_METHOD = "UpdateArtifactEntryMethod"
//    public static final String UPDATE_ARTIFACT_AUTO_SUBSCRIBE = "UpdateArtifactAutoSubscribe"
//
//    private final boolean pluginAvailable
//    private boolean updateConfigurationPermitted
//
//    ConfigurationTab(boolean pluginAvailable) {
//        super(PortalTab.Configuration)
//        this.pluginAvailable = pluginAvailable
//    }
//
//    @Override
//    void start() throws Exception {
//        super.start()
//        updateConfigurationPermitted = pluginAvailable ||
//                config().getJsonObject("permissions").getBoolean("update_artifact_configuration")
//
//        vertx.eventBus().consumer(CONFIGURATION_TAB_OPENED, {
//            log.info("Configuration tab opened")
//            def message = JsonObject.mapFrom(it.body())
//            def portal = SourcePortal.getPortal(message.getString("portal_uuid"))
//            portal.portalUI.currentTab = PortalTab.Configuration
//            SourcePortal.ensurePortalActive(portal)
//            updateUI(portal)
//        })
//        vertx.eventBus().consumer(PluginBridgeEndpoints.ARTIFACT_CONFIG_UPDATED.address, {
//            log.debug("Artifact configuration updated")
//            def artifact = it.body() as SourceArtifact
//            SourcePortal.getPortals(artifact.appUuid(), artifact.artifactQualifiedName()).each {
//                cacheAndDisplayArtifactConfiguration(it, artifact)
//            }
//        })
//
//        vertx.eventBus().consumer(UPDATE_ARTIFACT_ENTRY_METHOD, {
//            def request = JsonObject.mapFrom(it.body())
//            def portal = SourcePortal.getPortal(request.getString("portal_uuid"))
//            if (!updateConfigurationPermitted) {
//                log.warn("Rejected artifact entry method update")
//                updateUI(portal)
//                return
//            }
//
//            log.info("Updating artifact entry method")
//            def config = SourceArtifactConfig.builder()
//                    .endpoint(request.getBoolean("entry_method"))
//                    .build()
//            SourcePortalConfig.current.getCoreClient(portal.appUuid).createOrUpdateArtifactConfig(
//                    portal.appUuid, portal.portalUI.viewingPortalArtifact, config, {
//                if (it.succeeded()) {
//                    log.info("Successfully updated artifact entry method")
//                } else {
//                    log.error("Failed to update artifact config: " + portal.portalUI.viewingPortalArtifact, it.cause())
//                }
//            })
//        })
//        vertx.eventBus().consumer(UPDATE_ARTIFACT_AUTO_SUBSCRIBE, {
//            def request = JsonObject.mapFrom(it.body())
//            def portal = SourcePortal.getPortal(request.getString("portal_uuid"))
//            if (!updateConfigurationPermitted) {
//                log.warn("Rejected artifact auto subscribe update")
//                updateUI(portal)
//                return
//            }
//
//            log.info("Updating artifact auto subscribe")
//            def config = SourceArtifactConfig.builder()
//                    .subscribeAutomatically(request.getBoolean("auto_subscribe"))
//                    .build()
//            SourcePortalConfig.current.getCoreClient(portal.appUuid).createOrUpdateArtifactConfig(
//                    portal.appUuid, portal.portalUI.viewingPortalArtifact, config, {
//                if (it.succeeded()) {
//                    log.info("Successfully updated artifact auto subscribe")
//                } else {
//                    log.error("Failed to update artifact config: " + portal.portalUI.viewingPortalArtifact, it.cause())
//                }
//            })
//        })
//    }
//
//    @Override
//    void updateUI(SourcePortal portal) {
//        if (portal.portalUI.currentTab != thisTab) {
//            return
//        }
//
//        if (portal.portalUI.configurationView.artifact != null) {
//            //display cached
//            cacheAndDisplayArtifactConfiguration(portal, portal.portalUI.configurationView.artifact)
//        }
//        if (!pluginAvailable || portal.portalUI.configurationView.artifact == null) {
//            //fetch latest
//            SourcePortalConfig.current.getCoreClient(portal.appUuid).getArtifact(
//                    portal.appUuid, portal.portalUI.viewingPortalArtifact, {
//                if (it.succeeded()) {
//                    cacheAndDisplayArtifactConfiguration(portal, it.result())
//                } else {
//                    log.error("Failed to get artifact: " + portal.portalUI.viewingPortalArtifact, it.cause())
//                }
//            })
//        }
//    }
//
//    private void cacheAndDisplayArtifactConfiguration(SourcePortal portal, SourceArtifact artifact) {
//        portal.portalUI.configurationView.artifact = artifact
//        if (portal.portalUI.currentTab == thisTab) {
//            vertx.eventBus().send(portal.portalUuid + "-$DISPLAY_ARTIFACT_CONFIGURATION", new JsonObject(Json.encode(
//                    artifact.withArtifactQualifiedName(getShortQualifiedFunctionName(artifact.artifactQualifiedName())))))
//        }
//    }
//}

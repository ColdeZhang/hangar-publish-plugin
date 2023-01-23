package io.papermc.hangarpublishplugin

import org.gradle.api.Action
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.Nested
import org.gradle.api.tasks.Optional

interface HangarPublication {
    @get:Input
    val apiEndpoint: Property<String>

    @get:Input
    val apiKey: Property<String>

    @get:Input
    val owner: Property<String>

    @get:Input
    val name: String

    @get:Input
    val slug: Property<String>

    @get:Input
    val version: Property<String>

    @get:Input
    val channel: Property<String>

    @get:Input
    @get:Optional
    val changelog: Property<String>

    @get:Nested
    val platforms: NamedDomainObjectContainer<PlatformDetails>

    fun registerPlatform(platform: Platform, op: Action<PlatformDetails>) {
        platforms.register(platform.name) {
            this.platform.set(platform)
            op.execute(this)
        }
    }

    interface PlatformDetails {
        @get:Input
        val name: String

        @get:Input
        val platform: Property<Platform>

        @get:Input
        val platformVersions: List<String>

        @get:Nested
        val dependencies: NamedDomainObjectContainer<DependencyDetails>

        @get:InputFile
        @get:Optional
        val jar: RegularFileProperty
    }

    interface DependencyDetails {
        @get:Input
        val name: String

        @get:Input
        val required: Property<Boolean>

        @get:Nested
        @get:Optional
        val hangarNamespace: Property<HangarProjectNamespace>

        fun Project.hangarNamespace(owner: Provider<String>, slug: Provider<String>) {
            val ns = objects.newInstance(HangarProjectNamespace::class.java)
            ns.owner.set(owner)
            ns.slug.set(slug)
            hangarNamespace.set(ns)
        }

        fun Project.hangarNamespace(owner: String, slug: String) {
            val ns = objects.newInstance(HangarProjectNamespace::class.java)
            ns.owner.set(owner)
            ns.slug.set(slug)
            hangarNamespace.set(ns)
        }

        @get:Input
        @get:Optional
        val url: Property<String>
    }

    // todo don't use enum
    enum class Platform {
        PAPER,
        WATERFALL,
        VELOCITY
    }
}

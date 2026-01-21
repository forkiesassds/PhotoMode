plugins {
    id("multiloader-loader").apply(false)
    id("net.neoforged.moddev.legacyforge")
    kotlin("jvm")
    id("com.google.devtools.ksp")
    id("dev.kikugie.fletching-table")
}

project.ext["loader"] = "forge"
project.ext["supported_loaders"] = "forge"

apply(plugin = "multiloader-loader")

stonecutter.constants.put("forge", true)
stonecutter.constants.put("neoforge", false)

legacyForge {
    enable {
        forgeVersion = commonMod.prop("forgelike_loader_version")
        //English 101, brought to you by Kotlin DSL
        isDisableRecompilation = true
    }
}

dependencies {
    compileOnly("org.jetbrains:annotations:24.1.0")
    annotationProcessor("org.spongepowered:mixin:0.8.5-SNAPSHOT:processor")

    compileOnly(annotationProcessor("io.github.llamalad7:mixinextras-common:0.5.0") as Any)
    implementation(jarJar("io.github.llamalad7:mixinextras-forge:0.5.0") as Any)
}

mixin {
    add(sourceSets.main.get(), "picturemode.refmap.json")

    config("picturemode-common.mixins.json")
}

legacyForge {
    runs {
        register("client") {
            client()
            ideName = "Minecraft Client (${project.path})"
            gameDirectory = project.file("../../../run")
        }
    }

    commonMod.propOrNull("parchment_mappings")?.let {
        parchment {
            val parts = it.split(":")

            minecraftVersion = parts[0]
            mappingsVersion = parts[1]
        }
    }

    mods {
        register(commonMod.id) {
            sourceSet(sourceSets.main.get())
        }
    }
}

tasks {
    jar {
        finalizedBy("reobfJar")

        manifest.attributes(mapOf(
            "MixinConfigs" to "picturemode-common.mixins.json"
        ))
    }

    processResources {
        exclude("assets/picturemode/icon.png")
    }
}
plugins {
    id("multiloader-loader").apply(false)
    id("net.neoforged.moddev.legacyforge")
    alias(ft.plugins.mixin)
    id("me.modmuss50.mod-publish-plugin")
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

fletchingTable {
    mixins.configure(sourceSets.main) {
        mixin("picturemode-forgelike.mixins.json", "default") {
            env("CLIENT")
        }
    }
}

dependencies {
    compileOnly("org.jetbrains:annotations:24.1.0")
    annotationProcessor("org.spongepowered:mixin:0.8.5-SNAPSHOT:processor")

    compileOnly(annotationProcessor("io.github.llamalad7:mixinextras-common:0.5.0") as Any)
    implementation(jarJar("io.github.llamalad7:mixinextras-forge:0.5.0") as Any)
    modImplementation("dev.isxander:yet-another-config-lib:${commonMod.prop("yacl_version")}-forge")
}

mixin {
    add(sourceSets.main.get(), "picturemode.refmap.json")

    config("picturemode-common.mixins.json")
    config("picturemode-forgelike.mixins.json")
}

legacyForge {
    runs {
        register("client") {
            client()
            ideName = "Minecraft Client ($path)"
            gameDirectory = file("../../../run")
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
            "MixinConfigs" to "picturemode-common.mixins.json,picturemode-forgelike.mixins.json"
        ))
    }

    processResources {
        exclude("assets/picturemode/icon.png")
    }
}

publishMods {
    file.set(tasks.getByName<Jar>("reobfJar").archiveFile)
}
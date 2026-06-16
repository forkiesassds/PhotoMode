plugins {
    id("multiloader-loader").apply(false)
    id("net.neoforged.moddev")
    kotlin("jvm")
    id("com.google.devtools.ksp")
    id("dev.kikugie.fletching-table")
    id("me.modmuss50.mod-publish-plugin")
}

project.ext["loader"] = "neoforge"
project.ext["supported_loaders"] = "neoforge"

apply(plugin = "multiloader-loader")

stonecutter.constants.put("forge", false)
stonecutter.constants.put("neoforge", true)

neoForge {
    enable {
        version = commonMod.prop("forgelike_loader_version")
        //English 101, brought to you by Kotlin DSL
        isDisableRecompilation = true
    }
}

fletchingTable {
    mixins.create("main") {
        mixin("default", "picturemode-forgelike.mixins.json") {
            env("CLIENT")
        }
    }
}

repositories {
    maven {
        name = "Maven for PR #3198" // https://github.com/neoforged/NeoForge/pull/3198
        url = uri("https://prmaven.neoforged.net/NeoForge/pr3198")
        content {
            includeModule("net.neoforged", "neoforge")
            includeModule("net.neoforged", "testframework")
        }
    }
}

dependencies {
    implementation("dev.isxander:yet-another-config-lib:${commonMod.prop("yacl_version")}-neoforge")
}

neoForge {
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

tasks.processResources {
    filesMatching("META-INF/mods.toml") {
        if (stonecutter.eval(stonecutter.current.version, ">=1.20.5")) {
            name = "neoforge.mods.toml"
        }
    }

    exclude("assets/picturemode/icon.png")
}

publishMods {
    file.set(tasks.jar.get().archiveFile)
}
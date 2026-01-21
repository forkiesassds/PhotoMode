plugins {
    id("multiloader-loader").apply(false)
    id("net.neoforged.moddev")
    kotlin("jvm")
    id("com.google.devtools.ksp")
    id("dev.kikugie.fletching-table")
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

dependencies {
}

neoForge {
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

tasks.processResources {
    filesMatching("META-INF/mods.toml") {
        if (stonecutter.eval(stonecutter.current.version, ">=1.20.5")) {
            name = "neoforge.mods.toml"
        }
    }
}
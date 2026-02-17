plugins {
    id("multiloader-loader").apply(false)
    id("net.neoforged.moddev.legacyforge")
    kotlin("jvm")
    id("com.google.devtools.ksp")
    id("dev.kikugie.fletching-table")
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
    mixins.create("main") {
        mixin("default", "picturemode-forgelike.mixins.json") {
            env("CLIENT")
        }
    }
}

repositories {
    maven("https://maven.fabricmc.net/") { name = "Fabric" }
    maven("https://maven.su5ed.dev/releases/") { name = "Sinytra" }
}

dependencies {
    compileOnly("org.jetbrains:annotations:24.1.0")
    implementation("org.sinytra.mixinbooster:mixin-booster:0.1.2+1.20.1")
    annotationProcessor("org.sinytra:sponge-mixin:0.12.11+mixin.0.8.5")

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
            "MixinConfigs" to "picturemode-common.mixins.json,picturemode-forgelike.mixins.json"
        ))
    }

    processResources {
        exclude("assets/picturemode/icon.png")
    }
}

publishMods {
    file.set(tasks.getByName<Jar>("reobfJar").archiveFile)

    if (stonecutterBuild.eval(stonecutterBuild.current.version, "=1.20.1")) {
        modrinth {
            requires("mixinbooster")
        }

        curseforge {
            requires("mixinbooster")
        }
    }
}
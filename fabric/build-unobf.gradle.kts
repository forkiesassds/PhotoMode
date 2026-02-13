plugins {
    `multiloader-loader`
    id("net.fabricmc.fabric-loom")
    kotlin("jvm")
    id("com.google.devtools.ksp")
    id("dev.kikugie.fletching-table")
}

loom {
    runConfigs.all {
        ideConfigGenerated(true)
        runDir = "../../../run"
    }

    runs {
        removeIf { it.environment == "server" }
    }
}

fletchingTable {
    mixins.create("main") {
        mixin("default", "picturemode-fabric.mixins.json") {
            env("CLIENT")
        }
    }
}

dependencies {
    minecraft("com.mojang:minecraft:${commonMod.mc}")

    implementation("net.fabricmc:fabric-loader:${commonMod.prop("fabric_loader_version")}")
    api("net.fabricmc.fabric-api:fabric-api:${commonMod.prop("fabric_api_version")}")

    if (commonMod.prop("mod_menu_supported").toBoolean()) {
        implementation("com.terraformersmc:modmenu:${commonMod.prop("mod_menu_version")}")
    } else {
        compileOnly("com.terraformersmc:modmenu:${commonMod.prop("mod_menu_version")}")
    }

    implementation("dev.isxander:yet-another-config-lib:${commonMod.prop("yacl_version")}-fabric")
}

tasks.processResources {
    exclude("assets/picturemode/banner.png")
}

publishMods {
    file.set(tasks.jar.get().archiveFile)

    modrinth {
        requires("fabric-api")
    }

    curseforge {
        requires("fabric-api")
    }
}
plugins {
    `multiloader-loader`
    id("net.fabricmc.fabric-loom")
    alias(ft.plugins.mixin)
}

loom {
    runConfigs.all {
        generateRunConfig = true
        runDirectory.set(project.file("../../../run"))
    }

    runs {
        removeIf { it.runtimeEnvironment.get() == "server" }
    }
}

fletchingTable {
    mixins.configure(sourceSets.main) {
        mixin("picturemode-fabric.mixins.json", "default") {
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
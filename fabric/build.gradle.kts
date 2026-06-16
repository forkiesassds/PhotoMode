plugins {
    `multiloader-loader`
    id("net.fabricmc.fabric-loom-remap")
    kotlin("jvm")
    id("com.google.devtools.ksp")
    id("dev.kikugie.fletching-table")
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
    mixins.create("main") {
        mixin("default", "picturemode-fabric.mixins.json") {
            env("CLIENT")
        }
    }
}

dependencies {
    minecraft("com.mojang:minecraft:${commonMod.mc}")
    mappings(loom.layered {
        officialMojangMappings()
        commonMod.propOrNull("parchment_mappings")?.let { parchmentVersion ->
            parchment("org.parchmentmc.data:parchment-$parchmentVersion@zip")
        }
    })

    modImplementation("net.fabricmc:fabric-loader:${commonMod.prop("fabric_loader_version")}")
    modApi("net.fabricmc.fabric-api:fabric-api:${commonMod.prop("fabric_api_version")}")

    if (commonMod.prop("mod_menu_supported").toBoolean()) {
        modImplementation("com.terraformersmc:modmenu:${commonMod.prop("mod_menu_version")}")
    } else {
        modCompileOnly("com.terraformersmc:modmenu:${commonMod.prop("mod_menu_version")}")
    }

    modImplementation("dev.isxander:yet-another-config-lib:${commonMod.prop("yacl_version")}-fabric")
}

tasks {
    processResources {
        exclude("assets/picturemode/banner.png")
    }

    remapJar {
        destinationDirectory = rootProject.layout.buildDirectory.dir("libs/$loader")
    }
}

publishMods {
    file.set(tasks.remapJar.get().archiveFile)

    modrinth {
        requires("fabric-api")
    }

    curseforge {
        requires("fabric-api")
    }
}
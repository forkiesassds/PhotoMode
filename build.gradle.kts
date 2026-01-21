plugins {
    id("multiloader-common")
    id("net.fabricmc.fabric-loom-remap")
    kotlin("jvm")
    id("com.google.devtools.ksp")
    id("dev.kikugie.fletching-table")
}

loom {
    mixin {
        useLegacyMixinAp = false
    }
    decompilers {
        named("vineflower") { // Adds names to lambdas - useful for mixins
            options.put("mark-corresponding-synthetics", "1")
        }
    }
}

fletchingTable {
    mixins.create("main") {
        mixin("default", "picturemode-common.mixins.json") {
            env("CLIENT")
        }
    }

    j52j.register("main") {
        extension("json", "**/*.json5")
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

    modCompileOnly("net.fabricmc:fabric-loader:${commonMod.prop("fabric_loader_version")}")
}

val commonJava: Configuration by configurations.creating {
    isCanBeResolved = false
    isCanBeConsumed = true
}

val commonResources: Configuration by configurations.creating {
    isCanBeResolved = false
    isCanBeConsumed = true
}

artifacts {
    afterEvaluate {
        val mainSourceSet = sourceSets.main.get()
        mainSourceSet.java.sourceDirectories.files.forEach {
            add(commonJava.name, it)
        }
        mainSourceSet.resources.sourceDirectories.files.forEach {
            add(commonResources.name, it)
        }
    }
}
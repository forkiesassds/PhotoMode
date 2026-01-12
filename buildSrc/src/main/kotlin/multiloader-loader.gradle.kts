plugins {
    id("java")
    id("idea")
    id("multiloader-common")
}

val commonJava: Configuration by configurations.creating {
    isCanBeResolved = true
}
val commonResources: Configuration by configurations.creating {
    isCanBeResolved = true
}

base {
    archivesName.set("${commonMod.id}-$loader")
}

dependencies {
    val commonPath = common.hierarchy.toString()
    compileOnly(project(path = commonPath))
    commonJava(project(path = commonPath, configuration = "commonJava"))
    commonResources(project(path = commonPath, configuration = "commonResources"))
}

tasks {
    compileJava {
        dependsOn(commonJava)
        source(commonJava)
    }

    processResources {
        dependsOn(commonResources)
        from(commonProject.tasks.processResources.map { it.outputs })
    }

    withType<Jar> {
        destinationDirectory = rootProject.layout.buildDirectory.dir("libs/$loader")
    }
}
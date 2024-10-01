plugins {
    id("fabric-loom") version "1.7-SNAPSHOT"
    `maven-publish`
    java
}

version = property("mod_version")!!
group = property("mod_group_id")!!

val port_lib_modules: String by extra

version = "${property("minecraft_version")}-${property("mod_version")}-fabric"

base {
    archivesName.set("${property("mod_id")}")
}

loom {
    accessWidenerPath = file("src/main/resources/lodestone.accesswidener")

    runs {
        create("data") {
            client()
            name("Data Generation")
            vmArg("-Dfabric-api.datagen")
            vmArg("-Dfabric-api.datagen.output-dir=${file("src/generated/resources")}")
            vmArg("-Dfabric-api.datagen.modid=lodestone")
            //vmArg("-Dfabric-api.datagen.strict-validation")

            property("porting_lib.datagen.existing_resources", file("src/main/resources").absolutePath)
            property("lodestone.data.server", "false")

            runDir("build/datagen")
        }
    }
}

sourceSets {
    named("main") {
        resources {
            srcDir("src/generated/resources")
        }
    }
}

repositories {
    mavenCentral()
    maven("https://maven.theillusivec4.top/") { name = "Curios maven" }
    maven("https://dvs1.progwml6.com/files/maven") { name = "JEI maven" }
    maven("https://maven.tterrag.com/") { name = "tterrag maven" }
    maven("https://maven.blamejared.com/") { name = "BlameJared maven" }
    maven("https://cursemaven.com") {
        name = "Curse Maven"
        content {
            includeGroup("curse.maven")
        }
    }
    maven(url = "https://api.modrinth.com/maven")
    maven(url = "https://maven.ladysnake.org/releases")
    maven("https://maven.terraformersmc.com/")
    maven(url = "https://maven.parchmentmc.org")
    maven("https://mvn.devos.one/snapshots/")
    maven(url = "https://mvn.devos.one/releases/")
    maven( "https://maven.jamieswhiteshirt.com/libs-release")
}

dependencies {
    minecraft("com.mojang:minecraft:${property("minecraft_version")}")

    mappings(loom.layered {
        officialMojangMappings()
        parchment("org.parchmentmc.data:parchment-${property("parchment_minecraft_version")}:${property("parchment_version")}@zip")
    })

    modImplementation("net.fabricmc:fabric-loader:${property("loader_version")}")
    modImplementation("net.fabricmc.fabric-api:fabric-api:${property("fabric_api_version")}")

    modApi("dev.emi:trinkets:${property("trinkets_version")}") { isTransitive = false }

    modApi("dev.onyxstudios.cardinal-components-api:cardinal-components-base:${property("cca_version")}")
    modApi("dev.onyxstudios.cardinal-components-api:cardinal-components-entity:${property("cca_version")}")
    modApi("dev.onyxstudios.cardinal-components-api:cardinal-components-world:${property("cca_version")}")

    port_lib_modules.split(",").forEach { module ->
        include("io.github.fabricators_of_create.Porting-Lib:$module:${property("port_lib_version")}")
        modApi("io.github.fabricators_of_create.Porting-Lib:$module:${property("port_lib_version")}")
    }

    //modImplementation("io.github.ladysnake:satin:${property("satin_version")}")

    compileOnly("maven.modrinth:sodium:${property("sodium_version")}")
    compileOnly("maven.modrinth:iris:${property("iris_version")}")
    //modImplementation("org.embeddedt:embeddium-fabric-1.20.1:${property("embeddium_version")}")
    //modImplementation("org.embeddedt:embeddium-1.20.1:0.2.11-git.23aedfb+mc1.20.1")
    modApi("com.jamieswhiteshirt:reach-entity-attributes:${property("reach_entity_attributes_version")}")
    include("com.jamieswhiteshirt:reach-entity-attributes:${property("reach_entity_attributes_version")}")
}

tasks {

    processResources {
        inputs.property("version", project.version)
        filesMatching("fabric.mod.json") {
            expand(getProperties())
            expand(mutableMapOf("version" to project.version))
        }
    }

    jar {
        from("LICENSE")
    }

    compileJava{
        targetCompatibility = "21"
    }

}

java {
    // Loom will automatically attach sourcesJar to a RemapSourcesJar task and to the "build" task
    // if it is present.
    // If you remove this line, sources will not be generated.
    withSourcesJar()
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            artifactId = base.archivesName.get()
            from(components.getByName("java"))
        }
    }
    repositories {
        maven {
            url = uri("file://${System.getenv("local_maven")}")
        }
    }
}
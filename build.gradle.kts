plugins {
    id("java-library")
    id("maven-publish")
    id("net.neoforged.moddev") version "2.0.30-beta"
}

var baseArchivesName = "${project.property("mod_id")}"
var projectGroup = "${project.property("mod_group_id")}"
var projectVersion = "${property("minecraft_version")}-${property("mod_version")}"
if (System.getenv("BUILD_NUMBER") != null) {
    projectVersion = "$projectVersion.${System.getenv("BUILD_NUMBER")}"
}
var neoVersion = "${property("neo_version")}"
var parchmentMappingsVersion = "${property("parchment_mappings_version")}"
var parchmentMinecraftVersion = "${property("parchment_minecraft_version")}"

base {
    archivesName.set(baseArchivesName)
}
subprojects {
    plugins.apply("java-library")
    plugins.apply("maven-publish")
    plugins.apply("net.neoforged.moddev")
    tasks.withType<Test> {
        useJUnitPlatform()
    }
    base {
        archivesName.set("${baseArchivesName}-${project.name}")
    }
    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(21))
        }
    }
}
allprojects {
    group = projectGroup
    version = version

    repositories {
        flatDir {
            dirs("lib")
        }
        mavenLocal()
        mavenCentral()
        maven { //Our Stuff
            name = "BlameJared maven"
            url = uri("https://maven.blamejared.com/")
        }
        maven { //JEI
            name = "JEI maven"
            url = uri("https://dvs1.progwml6.com/files/maven")
        }
        maven { //Curse Maven, Generic
            name = "Curse Maven"
            url = uri("https://cursemaven.com")
            content {
                includeGroup("curse.maven")
            }
        }
        maven { //ParchmentMC Maven, Generic
            name = "ParchmentMC"
            url = uri("https://maven.parchmentmc.org")
            content {
                includeGroup("org.parchmentmc.data")
            }
        }
        maven { //Mod Maven, Generic
            name = "ModMaven"
            url = uri("https://modmaven.dev")
        }
        maven { //Modrinth Maven, Generic
            name = "Modrinth maven"
            url = uri("https://api.modrinth.com/maven")
        }

        maven { //KubeJS
            url = uri("https://maven.latvian.dev/releases")
            content {
                includeGroup("dev.latvian.mods")
                includeGroup("dev.latvian.apps")
            }
        }
        maven { //KubeJS Dependencies
            name = "jitpack"
            url = uri("https://jitpack.io")
            content {
                includeGroup("io.github")
                includeGroup("com.github.rtyley")
            }
        }
    }
    neoForge {
        version.set(neoVersion)

        parchment {
            mappingsVersion.set(parchmentMappingsVersion)
            minecraftVersion.set(parchmentMinecraftVersion)
        }
    }
}
tasks.named<Wrapper>("wrapper") {
    distributionType = Wrapper.DistributionType.BIN
}

tasks.withType<Javadoc>().configureEach {
    isFailOnError = false
    (options as StandardJavadocDocletOptions).addStringOption("Xdoclint:none", "-quiet")
}

val localRuntime: Configuration by configurations.creating
configurations.runtimeClasspath {
    extendsFrom(localRuntime)
}

neoForge {
    version.set(neoVersion)

    parchment {
        mappingsVersion.set(parchmentMappingsVersion)
        minecraftVersion.set(parchmentMinecraftVersion)
    }
    accessTransformers {
        publish(file("src/main/resources/META-INF/blockproperties.cfg"))
        publish(file("src/main/resources/META-INF/miscellaneous.cfg"))
        publish(file("src/main/resources/META-INF/recipebuilders.cfg"))
        publish(file("src/main/resources/META-INF/rendering.cfg"))
        publish(file("src/main/resources/META-INF/renderstates.cfg"))
    }
    setAccessTransformers(
        "src/main/resources/META-INF/blockproperties.cfg",
        "src/main/resources/META-INF/miscellaneous.cfg",
        "src/main/resources/META-INF/recipebuilders.cfg",
        "src/main/resources/META-INF/rendering.cfg",
        "src/main/resources/META-INF/renderstates.cfg"
    )
    runs {
        register("client") {
            client()
            systemProperty("neoforge.enabledGameTestNamespaces", baseArchivesName)
        }

        register("server") {
            server()
            systemProperty("neoforge.enabledGameTestNamespaces", baseArchivesName)
        }

        register("gameTestServer") {
            type = "gameTestServer"
            systemProperty("neoforge.enabledGameTestNamespaces", baseArchivesName)
        }

        register("data") {
            data()
            programArguments.addAll(
                "--mod", baseArchivesName,
                "--all",
                "--output", file("src/generated/resources/").absolutePath,
                "--existing", file("src/main/resources/").absolutePath
            )
        }

        configureEach {
            systemProperty("forge.logging.markers", "REGISTRIES")
            logLevel = org.slf4j.event.Level.DEBUG
        }
    }

    mods {
        create("${property("mod_id")}") {
            sourceSet(sourceSets.main.get())
        }
    }
}

// Include resources generated by data generators.
sourceSets {
    main {
        resources.srcDir("src/generated/resources")
    }
}

val generateModMetadata by tasks.registering(ProcessResources::class) {
    val replaceProperties = mapOf(
        "minecraft_version" to project.findProperty("minecraft_version") as String,
        "minecraft_version_range" to project.findProperty("minecraft_version_range") as String,
        "neo_version" to project.findProperty("neo_version") as String,
        "neo_version_range" to project.findProperty("neo_version_range") as String,
        "loader_version_range" to project.findProperty("loader_version_range") as String,
        "mod_id" to project.findProperty("mod_id") as String,
        "mod_name" to project.findProperty("mod_name") as String,
        "mod_license" to project.findProperty("mod_license") as String,
        "mod_version" to project.findProperty("mod_version") as String,
        "mod_authors" to project.findProperty("mod_authors") as String,
        "mod_description" to project.findProperty("mod_description") as String
    )
    inputs.properties(replaceProperties)
    expand(replaceProperties)

    // Exclude .java files or any other files that shouldn't have template expansion
    filesMatching("**/*.java") {
        exclude()
    }

    from("src/main/templates")
    into("build/generated/sources/modMetadata")
}
// Include the output of "generateModMetadata" as an input directory for the build.
// This works with both building through Gradle and the IDE.
sourceSets["main"].resources.srcDir(generateModMetadata)
neoForge.ideSyncTask(generateModMetadata)


java {
    withJavadocJar()
    withSourcesJar()
}
publishing {
    publications {
        register<MavenPublication>("mavenJava") {
            artifactId = "${property("mod_id")}"
            from(components["java"])
        }
    }
    repositories {
        maven {
            url = uri("file://${System.getenv("local_maven")}")
        }
    }
}

idea {
    module {
        for (fileName in listOf("run", "out", "logs")) {
            excludeDirs.add(file(fileName))
        }
    }
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}
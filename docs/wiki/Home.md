# Overview

Lodestone is a library containing all the code that I throughout my time in the modding scene have found useful. It contains utilities such as a rendering system, static config initialization, simplified block entities & inventories, a simple multiblock system, a custom fire effects, and so on. Just a bunch of random useful stuff that I needed in my other projects.

The way I approach writing this wiki will essentially work on a priority system. You are free to ping me at any time in my discord asking for help with a certain thing, and if I have time I will write about that thing here. After enough time the wiki will contain everything, with the most frequently used things being prioritized.

# Setup Guide

Like any other mod, Lodestone needs to be added to the project.
This can be done using the [Blamejared Maven](https://maven.blamejared.com/team/lodestar/lodestone/lodestone/).

## Forge

On Forge, Lodestone requires [Curios](https://github.com/TheIllusiveC4/Curios).
```groovy
repositories {
    mavenCentral()
    maven {
        name 'Curios maven'
        url = "https://maven.theillusivec4.top/"
    }
    maven { url = 'https://maven.blamejared.com/' }
}

dependencies {
    minecraft "net.minecraftforge:forge:${project.minecraftVersion}-${project.forgeVersion}"
    implementation fg.deobf("team.lodestar.lodestone:lodestone:${minecraft_version}-${lodestone_version}")
    //1.18.2 Example: implementation fg.deobf("team.lodestar.lodestone:lodestone:1.18.2-1.4.1.435")
    //1.19.2 Example: implementation fg.deobf("team.lodestar.lodestone:lodestone:1.19.2-1.4.2.83")
    //1.20.1 Example: implementation fg.deobf("team.lodestar.lodestone:lodestone:1.20.1-1.4.2.62")

    compileOnly fg.deobf("top.theillusivec4.curios:curios-forge:${curios_version}+${minecraft_version}:api")
    runtimeOnly fg.deobf("top.theillusivec4.curios:curios-forge:${curios_version}+${minecraft_version}")
}
```

### Mixins

Lodestone utilizes mixins, and without the appropriate plugin present in your Lodestone-dependent project the game will refuse to launch.

```groovy
buildscript {
    repositories {
        maven { url = 'https://files.minecraftforge.net/maven' }
        maven { url = 'https://repo.spongepowered.org/repository/maven-public/' }
        mavenCentral()
    }
    dependencies {
        classpath group: 'net.minecraftforge.gradle', name: 'ForgeGradle', version: '5.1.+', changing: true
        classpath 'org.spongepowered:mixingradle:0.7-SNAPSHOT'
    }
}

apply plugin: 'net.minecraftforge.gradle'
apply plugin: 'eclipse'
apply plugin: 'maven-publish'
apply plugin: 'org.spongepowered.mixin'
```

I believe writing your buildscript as such, and making sure to apply the `'org.spongepowered.mixin'` plugin is enough. For reference, the buildscript is the very first codeblock in your build.gradle file. It should be right at the very top.

For more info, consult the [Mixin wiki](https://github.com/SpongePowered/Mixin/wiki/Mixins-on-Minecraft-Forge).

## Fabric

On Fabric, Lodestone requires [Cardinal Components](https://github.com/Ladysnake/Cardinal-Components-API), [Trinkets](https://github.com/emilyploszaj/trinkets), [Porting Lib](https://github.com/Fabricators-of-Create/Porting-Lib), and [Reach Entity Attributes](https://github.com/JamiesWhiteShirt/reach-entity-attributes).
```txt
port_lib_version = 2.3.4+1.20.1
port_lib_modules = \
  accessors,\
  base,\
  blocks,\
  core,\
  data,\
  extensions,\
  lazy_registration,\
  model_generators,\
  models,\
  networking,\
  tool_actions
```
```groovy

repositories {
    maven {url = 'https://maven.blamejared.com/' }
    maven {url = "https://mvn.devos.one/releases/" }
    maven {url = "https://api.modrinth.com/maven" }
    maven {url = "https://maven.theillusivec4.top/"}
    maven {url = "https://maven.jamieswhiteshirt.com/libs-release"}
    maven {url = 'https://maven.ladysnake.org/releases'}
    maven {url = "https://maven.terraformersmc.com/"}
}

dependencies { 
    modImplementation("team.lodestar.lodestone:lodestone:${project.minecraft_version}-${project.lodestone_version}-fabric")
    //For this you also need the following
    modImplementation("dev.onyxstudios.cardinal-components-api:cardinal-components-base:${project.cca_version}")
    modImplementation("dev.onyxstudios.cardinal-components-api:cardinal-components-entity:${project.cca_version}")
    modImplementation("dev.onyxstudios.cardinal-components-api:cardinal-components-world:${project.cca_version}")

    modImplementation("dev.emi:trinkets:${project.trinkets_version}")
    for (String module in port_lib_modules.split(",")) {
        modImplementation("io.github.fabricators_of_create.Porting-Lib:$module:$port_lib_version")
    }
    modImplementation("com.jamieswhiteshirt:reach-entity-attributes:${project.rea_version}")
}

```


# Documented Features
- [InWorld Particles](https://github.com/LodestarMC/Lodestone/wiki/InWorld-Particles)
- [Post Processing Shaders](https://github.com/LodestarMC/Lodestone/wiki/Post-Processing-Shaders)
- [World Events](https://github.com/LodestarMC/Lodestone/wiki/World-Events)
- [OBJ Models](https://github.com/LodestarMC/Lodestone/wiki/Obj-Models)
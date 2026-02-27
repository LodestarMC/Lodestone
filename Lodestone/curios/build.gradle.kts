repositories {
    maven { //Curios
        name = "Curios maven"
        url = uri("https://maven.theillusivec4.top/")
    }
}

dependencies {
    compileOnlyApi("top.theillusivec4.curios:curios-neoforge:${property("curios_version")}:api")
    runtimeOnly("top.theillusivec4.curios:curios-neoforge:${property("curios_version")}")
}
repositories {
}

dependencies {
    compileOnly("maven.modrinth:sodium:mc${property("minecraft_version")}-${property("sodium_version")}-neoforge")
    compileOnly("maven.modrinth:iris:${property("iris_version")}+${property("minecraft_version")}-neoforge")

}
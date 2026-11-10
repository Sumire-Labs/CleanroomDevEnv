import com.cleanroommc.gradle.api.ext.CleanroomExtension

val cleanroom = extensions.getByType<CleanroomExtension>()

repositories {
    // CleanroomGradle supplies CleanroomMC, Mojang and Maven Central.
    maven("https://cursemaven.com") { name = "CurseMaven" }
    maven("https://curse.cleanroommc.com") { name = "CleanroomCurseMaven" }
    maven("https://api.modrinth.com/maven") { name = "Modrinth" }
    if (providers.gradleProperty("cg.repos.enableLocal").orElse("false").get().toBoolean()) mavenLocal()
}

dependencies {
    // SRG Mod: add("implementation", cleanroom.deobf("group:artifact:version"))
    // Compile-only Mod: add("compileOnly", cleanroom.deobf(files("libs/other-mod.jar")))
    // MCP development jar: add("implementation", "group:artifact:version:dev")
    // Ordinary library: add("implementation", "group:artifact:version")
    // Embedded jar: add("contain", "group:artifact:version")
    // Shaded library (enable_shadow=true): add("shade", "group:artifact:version")
    // Relocation rules go in extra.gradle.kts.
}

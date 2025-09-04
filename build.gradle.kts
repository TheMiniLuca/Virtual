import xyz.jpenilla.resourcefactory.bukkit.BukkitPluginYaml
import xyz.jpenilla.runpaper.task.RunServer

plugins {
    paper
  id("io.papermc.paperweight.userdev") version "2.0.0-beta.17" apply false
  id("xyz.jpenilla.run-paper") version "2.3.1" // Adds runServer task for testing
  id("xyz.jpenilla.resource-factory-bukkit-convention") version "1.3.0" // Generates plugin.yml based on the Gradle config
  id("com.gradleup.shadow") version "8.3.5"
}

java.disableAutoTargetJvm() // Allow consuming JVM 21 projects (i.e. paper_1_21_5) even though our release is 17

repositories {
  mavenLocal()
  mavenCentral()
  maven("https://repo.codemc.io/repository/maven-releases/")
}
dependencies {
  compileOnly("io.papermc.paper:paper-api:1.20.4-R0.1-SNAPSHOT")

  compileOnly(files("run/plugins/CommandAPI.jar"))
  implementation(project(":paper_hooks"))
  annotationProcessor("org.projectlombok:lombok:1.18.34")
  compileOnly("org.projectlombok:lombok:1.18.34")
  compileOnly("com.github.retrooper:packetevents-spigot:2.9.3")
  runtimeOnly(project(":paper_1_20_4", configuration = "reobf"))
    // runtimeOnly(project(":paper_1_21_5"))
}

tasks.assemble {
  dependsOn(tasks.shadowJar)
}

tasks.jar {
  manifest.attributes(
    "paperweight-mappings-namespace" to "mojang",
  )
}

// Configure plugin.yml generation
// - name, version, and description are inherited from the Gradle project.
bukkitPluginYaml {
  main = "io.github.theminiluca.virtual.Virtual"
  load = BukkitPluginYaml.PluginLoadOrder.STARTUP
  authors.add("MiniLuca")
  apiVersion = "1.20"
  depend = listOf("packetevents", "GrimAC", "CommandAPI")
}

tasks.runServer {
  minecraftVersion("1.21.8")
}

tasks.withType(xyz.jpenilla.runtask.task.AbstractRun::class) {
  javaLauncher = javaToolchains.launcherFor {
    vendor = JvmVendorSpec.JETBRAINS
    languageVersion = JavaLanguageVersion.of(21)
  }
  jvmArgs("-XX:+AllowEnhancedClassRedefinition")
}

tasks.register("run1_17_1", RunServer::class) {
  minecraftVersion("1.17.1")
  pluginJars.from(tasks.shadowJar.flatMap { it.archiveFile })
  runDirectory = layout.projectDirectory.dir("run1_17_1")
  systemProperties["Paper.IgnoreJavaVersion"] = true
}

tasks.register("run1_19_4", RunServer::class) {
  minecraftVersion("1.19.4")
  pluginJars.from(tasks.shadowJar.flatMap { it.archiveFile })
  runDirectory = layout.projectDirectory.dir("run1_19_4")
  systemProperties["Paper.IgnoreJavaVersion"] = true
}

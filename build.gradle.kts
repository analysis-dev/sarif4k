plugins {
    kotlin("multiplatform") version "1.6.10"
    kotlin("plugin.serialization") version "1.6.10"
    `maven-publish`
    signing
    id("io.github.gradle-nexus.publish-plugin") version "1.0.0"
}

group = property("GROUP")
version = property("VERSION")

repositories {
    mavenCentral()
}

kotlin {
    jvm {
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
                freeCompilerArgs = freeCompilerArgs + "-Xjvm-default=all"
//                withJavadocJar()
//                withSourcesJar()
            }
        }
    }
    linuxX64()
    mingwX64()
    macosX64()

    sourceSets {
        commonMain {
            dependencies {
                api("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation("junit:junit:4.13.2")
            }
        }
    }
}

tasks.withType(Javadoc::class).configureEach {
    val customArgs = projectDir.resolve("javadoc-silence.txt")
    customArgs.writeText("""-Xdoclint:none
    """.trimIndent())
    options.optionFiles?.add(customArgs)
}

//publishing {
//    publications {
//        register<MavenPublication>(rootProject.name) {
//            groupId = project.group as? String
//            artifactId = project.name
//            version = project.version as? String
//            from(components["java"])
//            pom {
//                description.set("SARIF data models for Kotlinx serialization")
//                name.set(rootProject.name)
//                url.set("https://detekt.github.io/detekt")
//                licenses {
//                    license {
//                        name.set("The Apache Software License, Version 2.0")
//                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
//                        distribution.set("repo")
//                    }
//                }
//                developers {
//                    developer {
//                        id.set("Chao Zhang")
//                        name.set("Chao Zhang")
//                        email.set("zhangchao6865@gmail.com")
//                    }
//                }
//                scm {
//                    url.set("https://github.com/detekt/sarif4k")
//                }
//            }
//        }
//    }
//}

if (findProperty("signing.keyId") != null) {
    signing {
        sign(publishing.publications[rootProject.name])
    }
} else {
    logger.lifecycle("Signing Disabled as the PGP key was not found")
}

//nexusPublishing {
//    repositories {
//        sonatype()
//    }
//}

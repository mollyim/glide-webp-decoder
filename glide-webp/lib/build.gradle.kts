/*
 * Copyright 2023 Signal Messenger, LLC
 * SPDX-License-Identifier: AGPL-3.0-only
 */

plugins {
  id("com.android.library")
  id("org.jetbrains.kotlin.android")
  id("maven-publish")
}

android {
  namespace = "im.molly.glide.webp"
  compileSdkVersion = "android-34"

  defaultConfig {
    externalNativeBuild {
      cmake {
        cppFlags("-std=c++17", "-fvisibility=hidden")
        arguments("-DLIBWEBP_PATH=$rootDir/libwebp")
      }
    }

    ndk {
      //noinspection ChromeOsAbiSupport
      abiFilters += listOf("armeabi-v7a", "arm64-v8a", "x86_64")
    }
  }

  externalNativeBuild {
    cmake {
      path = file("$projectDir/src/main/cpp/CMakeLists.txt")
      version = "3.22.1"
    }
  }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
  }

  publishing {
    singleVariant("release")
  }
}

afterEvaluate {
  publishing {
    publications {
      create<MavenPublication>("lib") {
        artifactId = "glide-webp-decoder"

        from(components["release"])

        pom {
          name.set(project.name)
          description.set("A Glide integration library for decoding webp")
          url.set("https://github.com/mollyim/glide-webp-decoder")

          scm {
            url.set("scm:git@github.com:mollyim/glide-webp-decoder.git")
            connection.set("scm:git@github.com:mollyim/glide-webp-decoder.git")
            developerConnection.set("scm:git@github.com:mollyim/glide-webp-decoder.git")
          }

          licenses {
            license {
              name.set("GPLv3")
              url.set("https://www.gnu.org/licenses/gpl-3.0.txt")
              distribution.set("repo")
            }
          }
        }
      }
    }

    repositories {
      mavenLocal()
      maven {
        name = "GitHubPackages"
        url = uri("https://maven.pkg.github.com/mollyim/glide-webp-decoder")

        credentials {
          username = System.getenv("GITHUB_ACTOR")
          password = System.getenv("GITHUB_TOKEN")
        }
      }
    }
  }
}

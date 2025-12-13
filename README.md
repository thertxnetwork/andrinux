# Andrinux

![Android](https://img.shields.io/badge/Android-10%2B-green.svg)
![Language](https://img.shields.io/badge/language-Kotlin%20%7C%20Java-blue.svg)
![Material Design](https://img.shields.io/badge/Material%20Design-3-purple.svg)
![License](https://img.shields.io/badge/license-Apache%202.0-blue.svg)

A modern, Material Design 3 Android terminal emulator.

## Features

- 🎨 **Material Design 3** - Modern and beautiful UI following the latest design guidelines
- 📱 **Android 10+** - Optimized for modern Android devices
- ☕ **Java 21** - Built with the latest Java features
- 🔧 **Customizable** - Fonts, colors, and keyboard shortcuts
- 📦 **Package Manager** - Built-in APT package management
- 🖼️ **X Server Support** - Run graphical Linux applications

## Requirements

- Android 10 (API 29) or higher
- arm64-v8a, armeabi-v7a, or x86_64 architecture

## Building

### Prerequisites

- JDK 21
- Android SDK 34
- Gradle 8.5+
- Android Studio Hedgehog or later (recommended)

### Build Steps

```bash
# Clone the repository
git clone https://github.com/thertxnetwork/andrinux.git
cd andrinux

# Build debug APK
./gradlew assembleDebug

# Build release APK
./gradlew assembleRelease
```

## Package Structure

- `com.thertxnetwork.andrinux` - Main application
- `com.thertxnetwork.andrinux.bridge` - Terminal bridge library
- `com.thertxnetwork.andrinux.neolang` - NeoLang scripting library
- `com.thertxnetwork.andrinux.xorg` - X server integration

## License

This project is licensed under the Apache License 2.0 - see the [LICENSE](LICENSE) file for details.

## Credits

- Based on [NeoTerm](https://github.com/NeoTerrm/NeoTerm)
- Terminal emulator based on [Termux](https://github.com/termux/termux-app)

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

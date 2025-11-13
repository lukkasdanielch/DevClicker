package com.example.devclicker.data.repository

/**
 * Um objeto singleton para guardar uma vasta coleção de dados estáticos do jogo,
 * como snippets de código, mensagens de erro, nomes de arquivos, e outros
 * elementos textuais que simulam um ambiente de desenvolvimento.
 */
object GameData {

    /**
     * Snippets de código curtos de várias linguagens e frameworks.
     * Usados para aparecer rapidamente na tela conforme o jogador clica.
     */
    val codeSnippets = listOf(
        // Kotlin & Android
        "fun main() { ... }",
        "data class User(val name: String, val age: Int)",
        "@Composable fun MyScreen() { ... }",
        "viewModelScope.launch { ... }",
        "private val _uiState = MutableStateFlow(UiState())",
        "val uiState = _uiState.asStateFlow()",
        "lateinit var viewModel: MyViewModel",
        "suspend fun loadData() { ... }",
        "implementation(\"androidx.core:core-ktx:1.17.0\")",
        "android.util.Log.d(\"TAG\", \"Debug message\")",
        "val intent = Intent(this, OtherActivity::class.java)",
        "findViewById<Button>(R.id.my_button)",
        "override fun onCreate(savedInstanceState: Bundle?)",
        "withContext(Dispatchers.IO) { ... }",
        "list.adapter = MyAdapter(data)",
        "fun List<String>.customExtension() { ... }",
        "sealed class Result<out T> { ... }",
        "object Loading : Result<Nothing>()",

        // Java
        "System.out.println(\"Hello, World!\");",
        "public static void main(String[] args) { ... }",
        "private final String TAG = \"MyClass\";",
        "if (user != null) { ... }",
        "try { ... } catch (Exception e) { ... }",
        "ArrayList<String> names = new ArrayList<>();",
        "import java.util.List;",
        "@Override",

        // JavaScript & Web
        "const [count, setCount] = useState(0);",
        "console.log('Hello from JS!');",
        "document.getElementById('root');",
        "fetch('api/data').then(res => res.json())",
        "const express = require('express');",
        "app.get('/', (req, res) => { ... });",
        "async function fetchData() { ... }",
        "export default MyComponent;",
        "import React from 'react';",

        // HTML & CSS
        "</div>",
        "<h1>DevClicker</h1>",
        "<button onClick={handleClick}>Click me</button>",
        "<body>",
        "color: #333;",
        "background-color: white;",
        "display: flex;",
        "justify-content: center;",
        ".container { ... }",

        // Python
        "import pandas as pd",
        "import numpy as np",
        "def calculate_sum(a, b):",
        "print(f\"Value: {x}\")",
        "for i in range(10):",
        "class Player:",
        "import tensorflow as tf",
        "[x**2 for x in numbers]",

        // SQL
        "SELECT * FROM users;",
        "INSERT INTO products (name, price) VALUES (?, ?);",
        "UPDATE settings SET value = 'dark' WHERE key = 'theme';",
        "DELETE FROM logs WHERE timestamp < NOW();",
        "CREATE TABLE high_scores (id INT, score INT);",
        "INNER JOIN profiles ON users.id = profiles.user_id;",

        // Git & Shell
        "git commit -m \"fix: correct typo\"",
        "git push origin main",
        "npm install",
        "docker-compose up -d",
        "ls -la",
        "chmod +x script.sh",
        "echo 'Deployment complete'",

        // Outros
        "// TODO: Refactor this later",
        "/* This is a multi-line comment */",
        "config.json",
        "README.md",
        "final int MAX_RETRIES = 3;",
        "#include <iostream>",
        "using namespace std;",

        // Outros depois dos outros
        "androidx.core:core-ktx:1.17.0",
        "androidx.appcompat:appcompat:1.7.0",
        "com.google.android.material:material:1.12.0",
        "androidx.compose.ui:ui:1.6.8",
        "androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.2",
        "com.squareup.retrofit2:retrofit:2.9.0",
        "org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3",
        "com.github.bumptech.glide:glide:4.16.0",
        "io.ktor:ktor-server-netty:2.3.11",
        "org.springframework.boot:spring-boot-starter-web:3.3.0",
        "react: 18.2.0",
        "express: 4.18.2",
        "pandas: 2.2.1",
        "numpy: 1.26.4",
        "tensorflow: 2.15.0",
        "junit:junit:4.13.2",
        "org.mockito:mockito-core:5.11.0",

        "NullPointerException: Attempt to invoke virtual method on a null object reference",
        "SyntaxError: Unexpected token",
        "TypeError: Cannot read property 'map' of undefined",
        "404 Not Found",
        "500 Internal Server Error",
        "Connection refused: connect",
        "FATAL EXCEPTION: main",
        "Error: ENOENT: no such file or directory, open 'config.json'",
        "warning: 'lateinit' property 'viewModel' has not been initialized",
        "Merge conflict in 'src/main/java/com/example/Game.java'",
        "Task :app:compileDebugKotlin FAILED",
        "Segmentation fault (core dumped)",
        "Error: Port 8080 is already in use.",
        "java.lang.OutOfMemoryError: Java heap space",
        "Module not found: Can't resolve 'react-dom/client'",
        "warning: Each child in a list should have a unique 'key' prop.",
        "ConstraintLayout\$LayoutParams cannot be cast to android.widget.LinearLayout\$LayoutParams",
        "Unable to resolve dependency for ':app@debug/compileClasspath'",
        "Your branch is ahead of 'origin/main' by 3 commits.",

        "MainActivity.kt", "User.java", "styles.css", "index.html", "App.js",
        "UserRepository.kt", "DatabaseHelper.java", "api.py", "package.json",
        "build.gradle.kts", "settings.gradle.kts", "Dockerfile", "README.md",
        "GameViewModel.kt", "Player.kt", "utils.js", "constants.ts",
        "strings.xml", "layout_activity_main.xml", "schema.sql", "test_main.py",
        ".gitignore", "LICENSE", "config.yaml", "src/", "main/", "res/", "drawable/",
        "components/", "server/", "node_modules/", "venv/",

        "feat: Implement user login feature",
        "fix: Correct calculation for high scores",
        "docs: Update README with setup instructions",
        "style: Format code according to style guide",
        "refactor: Simplify network request logic",
        "test: Add unit tests for UserRepository",
        "chore: Bump version to 1.2.0",
        "feat: Add dark mode theme",
        "fix: Prevent crash on empty input",
        "refactor: Extract strings to resource file",
        "feat: Initial commit",
        "fix: Handle null response from API",
        "chore: Update dependencies",
        "style: Remove trailing whitespace",
        "docs: Add comments to public methods",
        "test: Fix flaky UI test",
        "revert: Revert last commit due to bug",
        "feat: Implement achievements system"
    )
}

# Custom Java CLI

A modular, AI-powered command-line interface built in Java. It features a hybrid AI engine (Cloud + Local), developer tools, and a Natural Language Action Interpreter.

##  Key Features

* ** Natural Language Action Engine:** Type requests in plain English (`do open spotify and play drake`), and the shell translates them into executable system commands.
* ** Hybrid AI Chat:**
    * **Cloud:** Google Gemini Flash (Fast, Smart).
    * **Local:** Ollama/TinyLlama (Private, Offline).
    * **Auto-Failover:** Automatically switches to Gemini if Ollama is offline.
* ** Developer Tools:**
    * `leetcode`: Fetches a random coding problem from the GraphQL API.
    * `math`: Built-in arithmetic parser.
* ** System Integration:**
    * Launch Windows Apps (Spotify, Discord, Steam).
    * Execute System Commands (`dir`, `ping`, `docker`) via passthrough.
* ** UI:** Custom ANSI color theming and command routing.

## Architecture

* **Language:** Java 21
* **Build System:** Maven
* **Design Patterns:** Command Pattern, Strategy Pattern (AI Failover), Factory Pattern (Command Registry).
* **Libraries:**
    * `Gson` (JSON Parsing)
    * `Java HttpClient` (API Requests)
    * `ProcessBuilder` (OS Process Management)

##  Installation & Setup

### For Developers (Building from Source)
1.  **Clone the repo:**
    ```bash
    git clone https://github.com/Vedanth81106/Custom-Shell.git
    ```
2.  **Build the Project:**
    ```bash
    mvn package
    ```
3.  **Run:**
    ```bash
    java -jar target/custom-shell-1.0-SNAPSHOT.jar
    ```

### For Users (Running the App)
1.  **Download:** Get `cshell.jar` from the releases page.
2.  **Configure API Key:**
    * Create a file named `config.properties` in the same folder as the jar.
    * Add your Google Gemini API Key inside it:
      ```properties
      gemini_key=AIzaSy...YourKeyHere...
      ```
    * *(Note: You can get a free key from [Google AI Studio](https://aistudio.google.com/))*
3.  **Run:**
    * Open your terminal in that folder and type: `java -jar cshell.jar`
    * OR create a `run.bat` file with that command for double-click access.

## Usage Examples

| Command | Action |
| :--- | :--- |
| `do find a tutorial for java` | Opens YouTube searching for Java tutorials. |
| `do start postgres on port 5432` | Runs `docker run -p 5432:5432 postgres`. |
| `ai explain recursion` | Chats with the default AI. |
| `leetcode` | Fetches a random LeetCode problem. |

##  Contributing
Feel free to submit a Pull Request to add new commands!

# Copilot Java
Demo laboratory for VSCode Copilot course Java edition

## Dev workflow

```bash
# Clone repository
git clone https://github.com/AlbertoBasaloLabs/copilot-java.git
cd copilot-java
# Open in VSCode
code .
# Open terminal 
# Compile Java code
javac -d target src/*.java
# Run Java code
java -cp target Main
# Or run specific module
javac -d target src/*.java && java -cp target Main
javac -d target src/*.java && java -cp target Main weather
```
## AskBot CLI

A CLI educational tool that queries public APIs to provide basic information from the user's IP and associated services: 
- location, weather, currency, and sun.

> [Ver PRD del proyecto AskBot](docs/ask-bot.PRD.md)

---

- Author: [Alberto Basalo](https://albertobasalo.dev)
- Academy: [Academy Organization](https://github.com/AlbertoBasaloAcademy)
- Laboratory: [Source Repository](https://github.com/AlbertoBasaloLabs/copilot-java)
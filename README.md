# 🧠 TypeSmart – Real-time Text Suggestion Editor

TypeSmart is a full-stack, real-time intelligent suggestion editor inspired by Smart Compose. It offers word suggestions on the fly as the user types into a rich text editor. This project demonstrates a complete system including a frontend React editor, Spring Boot microservices, Eureka service discovery, and a Levenshtein-based suggestion engine.

---

## 🖥️ Demo

![Demo](/typeSmart.gif)

---

## 🚀 Features

- ✅ Smart floating suggestion panel
- ✅ Real-time suggestions as you type
- ✅ Custom Levenshtein distance algorithm
- ✅ Microservices architecture with Spring Boot
- ✅ Eureka Discovery + Spring Cloud Gateway
- ✅ Modern React UI with TailwindCSS

---

## 🧱 Tech Stack

| Layer       | Technology                             |
|------------|-----------------------------------------|
| Frontend   | React, TipTap Editor, TailwindCSS       |
| Backend    | Spring Boot (Java 17+)                  |
| Discovery  | Eureka Discovery Server                 |
| Gateway    | Spring Cloud Gateway                    |
| Editor Logic | Levenshtein Distance (Custom Java)    |

---

## 🗂️ Project Structure

```
typesmart/
├── frontend/
│ ├── src/
│ │ ├── App.js
│ │ └── components/Editor.js
│ └── tailwind.css
│
├── api-gateway/
│ ├── ApiGatewayApplication.java
│ └── application.properties
│
├── discovery-service/
│ ├── DiscoveryServerApplication.java
│ └── application.properties
│
├── suggestion-service/
│ ├── config/
│ ├── controller/
│ ├── service/
│ ├── model/
│ ├── util/
│ └── application.properties
│
└── README.md
```

---

## 🧩 Frontend - React + TipTap Editor

### `Editor.js`

- Uses `@tiptap/react` and `StarterKit` to build a rich text editor.
- Detects the current word while typing and makes **debounced API** calls for suggestions.
- Suggestion panel appears just below the current cursor with matching suggestions.

```javascript
useEffect(() => {
  const text = editor.getText();
  const words = text.split(/\s+/);
  const lastWord = words[words.length - 1] || '';
  ...
  fetchSuggestions(lastWord);
}, []);
```

## Tailwind UI

```css
@import "tailwindcss";

.ProseMirror {
  min-height: 200px;
  outline: none;
}
```

## 🌐 API Gateway (Spring Boot)

- `ApiGatewayApplication.java`
```java
@SpringBootApplication
@EnableDiscoveryClient
public class ApiGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }
}
```

- `application.properties`
```properties

spring.application.name=api-gateway
server.port=8080
spring.cloud.gateway.discovery.locator.enabled=true
spring.cloud.gateway.discovery.locator.lower-case-service-id=true
eureka.client.service-url.defaultZone=http://localhost:8761/eureka
```

## 🧭 Eureka Discovery Server

- `DiscoveryServerApplication.java`
```java
@SpringBootApplication
@EnableEurekaServer
public class DiscoveryServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(DiscoveryServerApplication.class, args);
    }
}
```

- `application.properties`
```properties
spring.application.name=discovery-service
server.port=8761
eureka.client.register-with-eureka=false
eureka.client.fetch-registry=false
eureka.server.wait-time-in-ms-when-sync-empty=0
```

## ✨ Suggestion Service
- Provides real-time suggestions based on **Levenshtein** similarity.

- `SuggestionService.java`
```java
@PostConstruct
public void init() throws IOException {
dictionary = new HashSet<>();
ClassPathResource resource = new ClassPathResource("dictionary.txt");

    try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
        String word;
        while ((word = reader.readLine()) != null) {
            dictionary.add(word.trim().toLowerCase());
        }
    }
}
```

- `SuggestionController.java`
```java
@GetMapping
public ResponseEntity<SuggestionResponse> getSuggestions(@RequestParam String word) {
    List<String> suggestions = suggestionService.suggest(word);
    return ResponseEntity.ok(new SuggestionResponse(word, suggestions));
}
```

- `LevenshteinUtil.java`
```java
public static int calculate(String a, String b) {
    int[][] dp = new int[a.length() + 1][b.length() + 1];
    // ...
    return dp[a.length()][b.length()];
}

```

- `application.properties`
```properties
spring.application.name=suggestion-service
server.port=8081
eureka.client.service-url.defaultZone=http://localhost:8761/eureka/
```

---

## 📐 Time Complexity Analysis

The Levenshtein algorithm calculates the minimum number of single-character edits required to change one word into another. It uses dynamic programming with a 2D matrix.

### 🧮 Time and Space Complexity:

Let:
- `m` = length of input word (typed by user)
- `n` = length of dictionary word
- `D` = total number of words in the dictionary

| Metric                | Complexity       |
|-----------------------|------------------|
| Single comparison     | `O(m × n)`       |
| Suggestion lookup     | `O(D × m × n)`   |
| Space per comparison  | `O(m × n)`       |

The current implementation sorts all dictionary words by their Levenshtein distance and selects the top 5.

> Optimization: To improve performance for large dictionaries, I am planning to add a Trie or use BK-Trees for approximate matching in `O(log D)` time.

---

## 📦 Dictionary File (Required)
- Place dictionary.txt in src/main/resources/ of the Suggestion Service.

```txt
hello
world
spring
boot
editor
text
input
react
...
```

## ⚙️ How to Run Locally
- Ensure below ports are available
  - 3000 (frontend)
  - 8080 (gateway)
  - 8081 (suggestion)
  - 8761 (eureka)

1. Start Eureka
```bash
cd discovery-service
mvn spring-boot:run
```

2. Start Gateway
```bash
cd api-gateway
mvn spring-boot:run
```

3. Start Suggestion Service
```bash
cd suggestion-service
mvn spring-boot:run
```

4. Start React Frontend
```bash
cd frontend
npm install
npm start
```

## 📊 Sample API
**GET** - `/api/suggestions?word=helo`

```json
{
  "word": "helo",
  "suggestions": ["halo", "held", "hele", "hell", "helm"]
}
```

## 📝 To-Do
- Keyboard **navigation** (↑ ↓ Enter)
- Add **Docker** support
- Deploy on **Render/Netlify + Fly.io**
- **ML-powered** suggestions

## 👤 Author
**Konark Lohat**

Full Stack Developer | React + Spring Boot | Deloitte → Product Companies

[GitHub](https://github.com/LastComrade) • [LinkedIn](https://www.linkedin.com/in/konark-lohat/)

## 📄 License
**This project is open source and available under the MIT License.**

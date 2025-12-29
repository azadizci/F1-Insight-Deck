# 🏎️ F1 Insight Deck

A stunning visual dashboard for Formula 1 driver statistics built with **Spring Boot 3.x** and **Thymeleaf**. Features an impressive F1 car "drive-by" animation when viewing driver details.

![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.1-green)
![Thymeleaf](https://img.shields.io/badge/Thymeleaf-3.x-blue)
![Bootstrap](https://img.shields.io/badge/Bootstrap-5.3-purple)

---

## ✨ Features

- **Driver Grid**: Beautiful card layout displaying F1 drivers with team colors, photos, and quick stats
- **3D Car Animation**: Click any driver to trigger an impressive F1 car flying across the screen with:
  - 3D perspective rotation
  - Speed lines effect
  - Tire smoke trail
- **Stats Modal**: Detailed driver statistics including:
  - Career wins, podiums, and season points
  - Animated bar chart showing last 10 race positions
  - Position-based color coding (Gold/Silver/Bronze for P1-P3)
- **Dark F1 Theme**: Premium design with carbon fiber textures and neon accents

---

## 🚀 Quick Start

### Prerequisites
- Java 17 or higher
- No additional installations required (Maven wrapper included)

### Run the Application

```bash
# Clone or navigate to the project
cd /path/to/SpringWEB

# Start the application
./mvnw spring-boot:run
```

Open your browser at: **http://localhost:8080**

---

## 📁 Project Structure

```
SpringWEB/
├── pom.xml                                    # Maven configuration
├── src/main/java/com/f1insight/
│   ├── F1InsightDeckApplication.java         # Main entry point
│   ├── controller/
│   │   └── F1Controller.java                 # REST endpoints
│   ├── model/
│   │   ├── Driver.java                       # Driver entity
│   │   └── RaceResult.java                   # Race result entity
│   └── service/
│       └── DriverService.java                # Data service (mocked)
└── src/main/resources/
    ├── application.properties                # App configuration
    ├── static/images/
    │   └── f1-car.png                        # Car animation image
    └── templates/
        └── index.html                        # Main UI template
```

---

## 🛠️ How It Works

### Backend Architecture

#### 1. **Driver Entity** (`Driver.java`)
Represents an F1 driver with all their information:
```java
public class Driver {
    private Long id;
    private String name;
    private String team;
    private int wins;
    private int podiums;
    private int points;
    private String imageUrl;
    private String country;
    private String countryFlag;
    private String teamLogo;
    private String teamColor;
    private List<RaceResult> lastTenRaces;
}
```

#### 2. **Race Result Entity** (`RaceResult.java`)
Stores individual race results:
```java
public class RaceResult {
    private String raceName;
    private int position;
}
```

#### 3. **Driver Service** (`DriverService.java`)
Provides hardcoded data for 6 top F1 drivers with realistic 2024 season statistics:
- Max Verstappen (Red Bull)
- Lewis Hamilton (Ferrari)
- Charles Leclerc (Ferrari)
- Lando Norris (McLaren)
- Carlos Sainz (Williams)
- George Russell (Mercedes)

Each driver includes their last 10 race results for the bar chart.

#### 4. **Controller** (`F1Controller.java`)
Two endpoints:
```java
@GetMapping("/")                    // Returns the HTML page with all drivers
@GetMapping("/api/driver/{id}")     // Returns JSON for a specific driver (AJAX)
```

---

### Frontend Architecture

#### **Single Page Application** (`index.html`)
Everything is contained in one Thymeleaf template with embedded CSS and JavaScript.

#### **CSS Features**
- **Carbon Fiber Texture**: Created with repeating gradients
- **Team Colors**: Each card uses the driver's team color via CSS variables
- **Glassmorphism**: Semi-transparent cards with blur effects
- **Hover Animations**: Scale and glow effects on driver cards

#### **The `driveBy` Animation**
The star of the show - a CSS keyframe animation that makes the F1 car:
1. Start off-screen (right: -600px)
2. Accelerate toward center with 3D rotation
3. Reach maximum size and glow at center
4. Exit left with trailing smoke effect

```css
@keyframes driveBy {
    0%   { right: -600px; transform: rotateY(-15deg) scale(0.8); }
    50%  { right: 50%; transform: rotateY(0deg) scale(1.2); /* max glow */ }
    100% { right: 100%; transform: rotateY(15deg) scale(0.8); }
}
```

#### **JavaScript Flow**
1. User clicks a driver card
2. Animation overlay appears with speed lines
3. F1 car animates across (1.5 seconds)
4. Fetch driver data via `/api/driver/{id}`
5. Populate and display the stats modal
6. Bar chart animates with staggered delays

---

## 🎨 Design System

### Colors
| Name | Hex | Usage |
|------|-----|-------|
| Primary Red | `#ff1801` | F1 branding, accents |
| Neon Cyan | `#00d2be` | Highlights, stats |
| Dark Background | `#0d0d0d` | Page background |
| Card Background | `rgba(20, 20, 20, 0.9)` | Card surfaces |

### Fonts
- **Orbitron**: Headlines, logo, numbers (racing aesthetic)
- **Rajdhani**: Body text, labels (modern, readable)

### Team Colors
Each team has a signature color used in cards and modals:
- Red Bull: `#3671C6`
- Ferrari: `#E80020`
- McLaren: `#FF8000`
- Mercedes: `#27F4D2`
- Williams: `#64C4FF`

---

## 🔧 Customization

### Change the F1 Car Image
Replace the file at:
```
src/main/resources/static/images/f1-car.png
```

### Add More Drivers
Edit `DriverService.java` and add new `Driver` objects with their `RaceResult` list.

### Change Port
Edit `application.properties`:
```properties
server.port=3000
```

---

## 📋 API Reference

### GET `/`
Returns the main HTML page with all drivers rendered via Thymeleaf.

### GET `/api/driver/{id}`
Returns JSON for a specific driver.

**Response Example:**
```json
{
  "id": 1,
  "name": "Max Verstappen",
  "team": "Red Bull Racing",
  "wins": 63,
  "podiums": 111,
  "points": 437,
  "imageUrl": "https://...",
  "country": "Netherlands",
  "countryFlag": "🇳🇱",
  "teamLogo": "https://...",
  "teamColor": "#3671C6",
  "lastTenRaces": [
    { "raceName": "Abu Dhabi GP", "position": 1 },
    ...
  ]
}
```

---

## 🧪 Tech Stack

| Technology | Version | Purpose |
|------------|---------|---------|
| Java | 17 | Core language |
| Spring Boot | 3.2.1 | Backend framework |
| Spring Web | - | REST controllers |
| Thymeleaf | 3.x | Server-side templating |
| Bootstrap | 5.3 | Responsive grid |
| CSS3 | - | Animations, styling |
| Vanilla JS | ES6+ | DOM manipulation, Fetch API |

---

## 📝 License

This project is for educational/demonstration purposes.

---

## 🙏 Credits

- Driver photos and team logos from Formula 1 official media
- F1 car image provided by user
- Fonts: Google Fonts (Orbitron, Rajdhani)

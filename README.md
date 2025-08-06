# 🚀 Space Invaders Java Project 👾

A Java-based **Space Invaders** style game built as a project to practice object-oriented programming and graphics.



![Spaceinvadersv2 12025-08-0701-02-31-Trim-ezgif com-video-to-gif-converter](https://github.com/user-attachments/assets/2578c39e-f71f-4f48-8620-61f833eec2ad)


![Spaceinvadersv2 12025-08-0701-09-06-Trim-ezgif com-video-to-gif-converter](https://github.com/user-attachments/assets/933e5e17-4cbc-4bcf-8f0f-3f36059870cc)

---

## 🎮 Project Overview

This project implements a classic 2D space shooter game where the player controls a spaceship to defend against waves of alien enemies. Features include:

- Multiple types of aliens, including standard aliens with varying speeds and health, visually represented by changing colors.
- A powerful boss alien with increasing health and horizontal movement that drops health packs on defeat.
- A player-controlled spaceship that moves in four directions and shoots a laser beam damaging aliens and the boss alien.
- Health packs (medic kits) that restore the spaceship’s health when collected.
- Dynamic level progression: each level increases alien population, boss strength, and alien speed.
- Score tracking that accumulates until game over.
- Indefinite number of levels for continuous gameplay.
- Collision detection and life/health management for all game objects.

The project uses the **UWCSE Graphics library** for rendering shapes and handling the game window.

---

## 📂 Classes and Files

| File Name         | Description                                                      |
|-------------------|------------------------------------------------------------------|
| `Alien.java`      | Standard aliens with randomized speeds, multiple health points indicated by colors, and random movement.  |
| `BossAlien.java`  | Strong boss alien with increasing health each level, horizontal movement, and health bar; drops medic kits on defeat. |
| `MedicKit.java`   | Health pack dropped by the boss alien; moves straight down and restores spaceship health upon collection. |
| `MovingObject.java` | Abstract base class defining common properties and methods for all moving game objects. |
| `SpaceInvader.java` | Main game controller handling game state, level progression, alien population, score tracking, and overall gameplay logic. |
| `SpaceShip.java`  | Player-controlled spaceship that moves, shoots a laser beam, manages lives, and handles collisions with aliens, boss, and medic kits. |

---

## 🚀 How to Run

1. Make sure you have **Java 8+** installed.

2. Clone or download the repository.

3. Add the **UWCSE Graphics library** to your project's classpath.

4. Compile all `.java` files in the `finalscsc142` package:

   ```bash
   javac finalscsc142/*.java
Run the main class to start the game:

java finalscsc142.SpaceInvader

---

## 🤝 Contact

Got feedback or want to collaborate?

📬 [Open an issue](https://github.com/SeojunKim05/Programming-space-invader/issues)  
💼 [LinkedIn](https://www.linkedin.com/in/seojun-kim-089b7b339)  
📫 Email: kseojun05@gmail.com

---

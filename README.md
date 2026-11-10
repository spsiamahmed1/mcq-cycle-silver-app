# MCQ Cycle — Silver

**20 মিনিটে 100 MCQ · 1000টি সমাধানে Silver**

Java-only, offline Android study-cycle timer. Minimum Android 7.0 (API 24), target API 34, Java 17. The repository includes a GitHub Actions workflow that builds a debug APK on every push to `main` and uploads it as **MCQ-Cycle-Silver-App**.

## Features

- 20-minute cycle with a wall-clock based timer, foreground notification, pause/off controls, and timer recovery after app backgrounding and device reboot.
- A new randomized set of 100 questions per cycle, with no question repeated until the bundled bank has been exhausted.
- Offline Room database seeded on first launch with 1,100+ deterministic practice questions spanning arithmetic, English vocabulary, periodic-table science, ICT binary conversion, country capitals, SSC/HSC-style practice, and job-exam percentages.
- Answer checking, explanations, previous/next/skip navigation, cycle results/history, and Bronze/Silver/Gold/Platinum/Diamond milestones.
- Sound, vibration, dark-mode, and screenshot-block settings. Notification permission is requested at runtime; refusing it does not stop the app.

## Build APK

Open **Actions → Build APK** after a push (or run the workflow manually), then download the **MCQ-Cycle-Silver-App** artifact from a successful run. Local build command: `./gradlew assembleDebug`.

## Data and privacy

The question bank and progress are stored locally. No account, network connection, or analytics are required. The included question bank is a generated practice bank, not a substitute for an official exam syllabus or independently reviewed question set. Android and device power-management policies can affect background execution; the app uses a foreground service and boot receiver but cannot override system restrictions or force-start if the user disables app autostart.

## বাংলা

অ্যাপটি সম্পূর্ণ অফলাইনে কাজ করে। শুরু করলে ২০ মিনিটের সাইকেল চলে; Pause চাপলে থামে, Off চাপলে বন্ধ হয়। প্রতিটি সাইকেলে এলোমেলো ১০০টি প্রশ্ন আসে এবং পুরো প্রশ্নভান্ডার শেষ না হওয়া পর্যন্ত প্রশ্ন পুনরাবৃত্তি এড়ানো হয়। GitHub Actions সফল হলে **MCQ-Cycle-Silver-App** artifact থেকে debug APK ডাউনলোড করা যাবে।

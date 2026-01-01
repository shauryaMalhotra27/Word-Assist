# WordAssist : Instant Word Meaning Anywhere on Android Device

**WordAssist** is a lightweight Android utility that shows word definitions instantly i.e. without leaving your current app.  
Just select or copy any word and the meaning appears in a floating overlay.

## What It Does
Helps you understand unfamiliar words while reading in:
- Browsers.
- PDFs & e-books.
- Social media.
- Messaging apps.
- Any app with selectable/copyable text.

The meaning pops up in a non-intrusive floating widget, keeping your reading flow uninterrupted.

**WordAssist does NOT record keystrokes, monitor usage, or collect personal data.**

## Key Features
- Draggable floating bubble that expands into a definition card.
- Automatic word detection via Accessibility Service.
- Clipboard fallback support (works great with PDFs & browsers).
- Fast, concise definitions from dictionary APIs.
- Strong privacy — no data stored, no logging, no sharing.
- Very lightweight with minimal battery impact.

## How It Works
- User selects or copies a word.
- Accessibility Service or Clipboard detects it.
- Overlay service processes the word.
- Floating widget instantly displays the meaning.

## Required Permissions

- **Accessibility Service**  
  Detects selected text in any app.

- **Draw over other apps**  
  Shows the floating dictionary widget.

## Tech Stack
- Language: Java  
- UI: Native Android Views
- Accessibility API + WindowManager overlay  
- Retrofit (API calls)
- Service-based architecture  

## Requirements
Android 9.0 (API 27) and above.

## Contributing
Contributions are welcome! Feel free to open issues or submit pull requests.

Developed by Shaurya.
Android utility app : made for productivity and learning.

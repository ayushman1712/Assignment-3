# WiFi Strength Matrix App

This project is a simple Android app built using **Jetpack Compose** that scans WiFi networks and displays a **pure RSSI matrix** for selected locations. It helps visualize WiFi signal strengths with a clean grid layout.

## Features
- Select one of several predefined locations.
- Perform **100 WiFi scans** at the selected location.
- Build a **pure RSSI matrix** (10x10 grid).
- Display only the **currently selected location**'s scan results.
- Show **min and max RSSI** values for the collected data.

## How It Works

### 1. Location Selection
- A list of locations ("Location 1", "Location 2", "Location 3") is shown.
- When the user selects a location, it becomes the current context.

### 2. WiFi Scanning
- Clicking the "Scan WiFi at Location" button triggers scanning.
- Scans are collected **100 times** with a **2-second delay** between each.
- The scans are collected using a helper class `WifiScanner` (not shown here).

### 3. Building the RSSI Matrix
- For each scan, the **strongest signal** (maximum RSSI) is picked.
- These RSSI values are assembled into a **list of 100 values**.
- This list is **chunked into 10 rows**, each with **10 values**, forming a 10x10 matrix.

### 4. Displaying Results
- Only the matrix corresponding to the **currently selected location** is shown.
- Each RSSI value is displayed in a small box.
- The **minimum and maximum RSSI** values are shown below the matrix.

### 5. (Optional) Heatmap Enhancement
- (Planned) Color-code each box based on RSSI value to create a simple heatmap effect.

## Main Components
- `LocationSelectorScreen`: Main Composable managing UI and state.
- `collectMultipleScans()`: Function that handles repeated WiFi scans asynchronously.

## Technologies Used
- **Kotlin**
- **Jetpack Compose**
- **Android SDK**

## Future Improvements
- Add heatmap color coding for better visualization.
- Allow user-defined location names.
- Export scan data to a file (CSV/JSON).
- Real-time scan visualization.

---

Feel free to extend this project to include more advanced features like filtering by SSID, detecting dead zones, or even uploading results to a server for remote analysis!

#!/bin/bash

echo "Compiling Java backend..."
cd backend
javac Server.java || { echo "Compilation failed"; exit 1; }

echo "Starting Java backend..."
gnome-terminal -- bash -c "java Server; exec bash" 2>/dev/null || \
x-terminal-emulator -e "java Server" 2>/dev/null || \
echo "Open a new terminal and run: java Server" &

cd ..
echo "Starting frontend server at http://localhost:3000"
serve frontend
#!/bin/bash

echo "Compiling Java backend..."
javac -d backend/out backend/src/main/java/Server.java || { echo "❌ Compilation failed"; exit 1; }

echo "✅ Java backend compiled."

echo "Starting Java backend in this terminal..."
java -cp backend/out Server &
BACKEND_PID=$!

echo "✅ Backend started (PID: $BACKEND_PID)."
echo "Starting frontend server at http://localhost:3000..."

npx serve frontend

kill $BACKEND_PID
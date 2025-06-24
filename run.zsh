#!/bin/zsh

clear
echo "🧼 Cleaning and Building..."
mkdir -p bin
rm -rf bin/*.class
rm -rf bin/images

echo "📦 Compiling Java files..."
javac -d bin src/*.java || { echo "❌ Compilation failed"; exit 1 }

echo "📁 Copying images..."
cp -r src/images bin/

echo "🚀 Running Flappy Bird..."
java -cp bin App

#!/bin/bash

# Validate input

if [ -z "$1" ]; then
echo "❌ Usage: ./run-migration.sh <description>"
exit 1
fi

DESC=$(echo "$1" | tr ' ' '_' | tr '[:upper:]' '[:lower:]')

YEAR=$(date +"%Y")

DIR="src/main/resources/db/migration/${YEAR}"

mkdir -p "$DIR"

# Find latest Flyway version

# Find latest Flyway version
LATEST=$(find src/main/resources/db/migration \
    -type f \
    -name "V*__*.sql" \
    | grep -oE 'V[0-9]+__' \
    | sed 's/V//;s/__//' \
    | sort -n \
    | tail -1)

if [ -z "$LATEST" ]; then
    VERSION=${LATEST:-1}
else
    VERSION=$((LATEST + 1))
fi

FILE="$DIR/V${VERSION}__${DESC}.sql"

cat <<EOF > "$FILE"
-- =========================================
-- Migration: $DESC
-- Version: V${VERSION}
-- Created at: $(date)
-- Author: $(whoami)
-- =========================================

-- TODO: Write your SQL here

EOF

echo "✅ Migration created: $FILE"

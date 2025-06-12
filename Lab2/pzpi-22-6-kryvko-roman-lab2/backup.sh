#!/bin/bash

DB_NAME="apz"
DB_USER="roman"
DB_HOST="localhost"
DB_PORT="5432"
BACKUP_DIR="backup"
DATE=$(date +%F_%H-%M-%S)
BACKUP_FILE="$BACKUP_DIR/${DB_NAME}_backup_$DATE.sql"

mkdir -p "$BACKUP_DIR"

pg_dump -U "$DB_USER" -h "$DB_HOST" -p "$DB_PORT" "$DB_NAME" > "$BACKUP_FILE"

if [ $? -eq 0 ]; then
  echo "Backup successful: $BACKUP_FILE"
else
  echo "Backup failed, error: $?"
fi


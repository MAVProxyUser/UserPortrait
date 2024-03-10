import sqlite3
import pandas as pd
import os
import json

# Create an SQLite Database and Define Schema
def create_database(db_path):
    conn = sqlite3.connect(db_path)
    cursor = conn.cursor()

    # Create table for JSON data with added aircraftName column
    cursor.execute('''
        CREATE TABLE IF NOT EXISTS json_records (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            file_path TEXT,
            version INTEGER,
            aircraftName TEXT,
            subStreet TEXT,
            street TEXT,
            city TEXT,
            area TEXT,
            latitude REAL,
            longitude REAL
        )
    ''')

    # Create table for CSV data
    cursor.execute('''
        CREATE TABLE IF NOT EXISTS csv_locations (
            id INTEGER PRIMARY KEY,
            Name TEXT,
            id_sub TEXT,
            Town TEXT,
            Prefecture TEXT,
            lat REAL,
            long REAL,
            facility_type TEXT,
            corroboration_level TEXT,
            ETNAM TEXT,
            Shawn_Zhang_list TEXT,
            Shawn_Zhang_num INTEGER,
            ASPI_2020_num INTEGER,
            XJVDB TEXT,
            Media_reports TEXT,
            Researchers TEXT,
            Links TEXT,
            Links_2 TEXT,
            Links_3 TEXT,
            Links_4 TEXT
        )
    ''')

    conn.close()

# Process JSON File and Insert Data into SQLite Database
def process_json_file(json_path, db_path):
    conn = sqlite3.connect(db_path)
    cursor = conn.cursor()

    try:
        with open(json_path, 'r') as file:
            data = json.load(file)
            version = data.get('version')
            info = data.get('info', {})
            aircraft_name = info.get('aircraftName')
            subStreet = info.get('subStreet')
            street = info.get('street')
            city = info.get('city')
            area = info.get('area')
            latitude = info.get('latitude')
            longitude = info.get('longitude')

            cursor.execute('''
                INSERT INTO json_records (file_path, version, aircraftName, subStreet, street, city, area, latitude, longitude)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
            ''', (json_path, version, aircraft_name, subStreet, street, city, area, latitude, longitude))
            conn.commit()
    except json.JSONDecodeError:
        print(f"Invalid JSON in file {json_path}")

    conn.close()

# Create the SQLite database and tables
db_path = 'flight_data.db'
create_database(db_path)

# Read the CSV file, rename columns, and insert data into SQLite database
csv_path = '210720_all_locations_BFN.csv'
df_csv = pd.read_csv(csv_path, sep='\t', encoding='ISO-8859-1')

# Rename DataFrame columns to match the SQLite table schema
df_csv.columns = [
    'id', 'Name', 'id_sub', 'Town', 'Prefecture', 'lat', 'long', 'facility_type', 
    'corroboration_level', 'ETNAM', 'Shawn_Zhang_list', 'Shawn_Zhang_num', 'ASPI_2020_num', 
    'XJVDB', 'Media_reports', 'Researchers', 'Links', 'Links_2', 'Links_3', 'Links_4'
]

conn = sqlite3.connect(db_path)
df_csv.to_sql('csv_locations', conn, if_exists='append', index=False)
conn.close()

# Process and insert JSON files
directory = 'FlightLogExtract'

for root, dirs, files in os.walk(directory):
    for file in files:
        if file.endswith('.json'):
            json_file_path = os.path.join(root, file)
            process_json_file(json_file_path, db_path)

print("Data from CSV and JSON files has been successfully inserted into the SQLite database.")

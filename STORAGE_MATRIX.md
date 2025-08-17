# Storage Options Matrix

| Option    | Data type it stores | Storage capacity limits | ACID support | Backup difficulty | Example from this app |
|-----------|---------------------|--------------------------|--------------|-------------------|-----------------------|
| **Files** | Unstructured (binary/text) | Large (limited by device storage quota) | **No** | Medium (copy/move file trees) | Store image/PDF attachments under app-specific storage |
| **DataStore** | Key-value or protobuf messages (semi-structured) | Small–medium; best for preferences and small configs | **No** (atomic writes, but not full relational ACID) | Easy (preferences/one file) | Store user prefs like dark mode, sort order |
| **Room (SQLite)** | Structured relational data (tables/relations) | Large (limited by device storage) | **Yes** (SQLite transactions provide ACID) | Medium (export/import DB, or migrate) | Store Users, Projects, Tasks, and metadata |
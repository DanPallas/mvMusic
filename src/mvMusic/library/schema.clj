(ns mvMusic.library.schema
  (:use [mvMusic.global])
  (:require [clojure.java.io :as io]
            [clojure.java.jdbc :as jdb]
            [mvMusic.log :as log]))

(def db
  {:classname "org.h2.Driver"
   :subprotocol "h2"
   :subname (str (:temp-folder cfg-map) 
                 (if (= (last (:temp-folder cfg-map)) \/) 
                   "" 
                   \/) 
                 "library")})

(def folders
  (jdb/create-table-ddl :folders
                        [:folder "VARCHAR(1024)" "PRIMARY KEY"]
                        [:mod_date :TIMESTAMP]
                        [:image :bool]))

(def songs 
  (jdb/create-table-ddl :songs
                        [:file "VARCHAR(1024) PRIMARY KEY"]
                        [:track :integer]
                        [:disc :integer]
                        [:title "VARCHAR(1024)"]
                        [:artist "VARCHAR(1024)"]
                        [:time :integer]
                        [:published :integer]
                        [:extension "VARCHAR(1024)"]
                        [:genre "VARCHAR(1024)"]
                        [:folder "VARCHAR(1024)"]
                        [:filename "VARCHAR(1024)"]
                        [:path "VARCHAR(1024)"]
                        [:mod_date :TIMESTAMP]
                        [:album_title "VARCHAR(1024)"]
                        [:album_artist "VARCHAR(1024)"]
                        [:album_sort "VARCHAR(1024)"]
                        [:image :bool]
                        [:other :text]
                        ["foreign key(folder) REFERENCES folders(folder)"]))

(def downloads 
  (jdb/create-table-ddl :downloads
                        [:file "VARCHAR(1024) PRIMARY KEY"]
                        [:name "VARCHAR(1024)"]
                        [:created :timestamp]
                        [:filetypes "VARCHAR(1024)"]
                        [:bitrates "VARCHAR(1024)"]))

(def songs-downloads 
  (jdb/create-table-ddl :songs_downloads
                        [:song_file (str "VARCHAR(1024) REFERENCES songs(file)"
                                         " ON DELETE CASCADE")]
                        [:download_file 
                         (str "VARCHAR(1024) REFERENCES downloads(file)"
                              " ON DELETE CASCADE")]))



(def folder-idx "CREATE INDEX songs_folder ON songs (folder)")

(defn create-db  
  "Creates database and all tables and indexes at location in cfg-map. "
  [db]
  (try
    (jdb/db-do-commands db folders songs downloads songs-downloads)
    (jdb/db-do-commands db folder-idx)
    (catch Exception ex
      (log/error ex "Error creating database")
      #_(System/exit (:db-failure exit-codes)))))

(defn drop-db 
  "Deletes database file if it exists. Returns true if database deleted. Nil if
  database didn't exist."
  [db]
  (try 
    (jdb/db-do-commands db "DROP ALL OBJECTS DELETE FILES")
    (catch Exception ex
      (log/error ex "Error creating database")
      #_(System/exit (:db-failure exit-codes)))))

(defn recreate-db 
  "Deletes database and then recreates all tables and indexes without data."
  [db]
  (drop-db db)
  (create-db db))

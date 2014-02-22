(ns mvMusic.library.schema
  (:use [mvMusic.global])
  (:require [clojure.java.io :as io]
            [clojure.java.jdbc :as jdb]
            [mvMusic.log :as log]))

(def db
  {:classname "org.sqlite.JDBC"
   :subprotocol "sqlite"
   :subname (str (:temp-folder cfg-map) 
                 (if (= (last (:temp-folder cfg-map)) \/) "" \/) 
                 db-name)})

(def songs 
  (jdb/create-table-ddl :songs
                        [:id :integer "PRIMARY KEY AUTOINCREMENT"]
                        [:track :integer]
                        [:disc :integer]
                        [:title :text]
                        [:artist :text]
                        [:length :integer]
                        [:year :integer]
                        ["[album-id]" :integer]
                        [:extension :text]
                        [:genre :text]
                        [:folder :text]
                        [:filename :text]
                        [:path :text]
                        ["[mod-date]":integer]
                        ["foreign key([album-id]) REFERENCES album(id)"]
                        ["foreign key(folder) REFERENCES folders(path)"]))

(def albums 
  (jdb/create-table-ddl :albums
                        [:id :integer "PRIMARY KEY AUTOINCREMENT"]
                        [:title :text]
                        [:artist :text]
                        [:image :text]
                        [:sort :text]))

(def downloads 
  (jdb/create-table-ddl :downloads
                        [:id :integer "PRIMARY KEY AUTOINCREMENT"]
                        [:path :text]
                        [:file :text]))

(def songs-downloads 
  (jdb/create-table-ddl "[songs-downloads]"
                        ["[songs-id]" :integer]
                        ["[downloads-id]" :integer]
                        ["FOREIGN KEY([songs-id]) REFERENCES songs(id)"]
                        ["FOREIGN KEY([downloads-id]) REFERENCES downloads(id)"]))

(def folders
  (jdb/create-table-ddl :folders
                        [:path :text "PRIMARY KEY"]
                        ["[mod-date]" :integer]))

(def artist-idx "CREATE INDEX songs_artist ON songs (artist)")
(def folder-idx "CREATE INDEX songs_folder ON songs (folder)")

(defn create-db  
  "Creates database and all tables and indexes at location in cfg-map. "
  []
  (try
    (jdb/with-connection db
      (jdb/do-commands songs albums downloads songs-downloads 
                       folders folder-idx artist-idx))
    (catch Exception ex
      (do 
        (log/error ex "Error creating database")
        #_(System/exit (:db-failure exit-codes))))))

(defn drop-db 
  "Deletes database file if it exists. Returns true if database deleted. Nil if
  database didn't exist."
  []
  (let [x (io/as-file (:subname db))]
    (try 
      (if (.exists x)
        (io/delete-file x))
      (catch Exception ex
        (do 
          (log/error ex "Error creating database")
          #_(System/exit (:db-failure exit-codes)))))))

(defn recreate-db 
  "Deletes database and then recreates all tables and indexes without data."
  []
  (drop-db)
  (create-db))

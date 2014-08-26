(ns mvMusic.library.schema
  (:use [mvMusic.global])
  (:require [clojure.java.io :as io]
            [clojure.java.jdbc :as jdb]
            [mvMusic.log :as log]))

(defn get-db-spec 
  [& path]
  "return db spec map. If path is given then db spec for that path will be
  returned, otherwise cfg-map will determine location."
  {:classname "org.h2.Driver"
   :subprotocol "h2"
   :subname (.getAbsolutePath (io/as-file (if (first path)
              (first path)
              (str (:temp-folder cfg-map) 
                   (if (= (last (:temp-folder cfg-map)) \/) 
                     "" 
                     \/) 
                   "library"))))})

(def folders
  (jdb/create-table-ddl :folders
                        [:folder :nvarchar "PRIMARY KEY"]
                        [:mod_date :long "NOT NULL"]))

(def songs 
  (jdb/create-table-ddl :songs
                        [:id_no :identity]
                        [:file :nvarchar "UNIQUE NOT NULL"]
                        [:track :integer]
                        [:track_total :integer]
                        [:disc_no :integer]
                        [:disc_total :integer]
                        [:title :nvarchar]
                        [:artist :nvarchar]
                        [:artist_sort :nvarchar]
                        [:album_sort :nvarchar]
                        [:album :nvarchar]
                        [:album_artist :nvarchar]
                        [:year :integer]
                        [:genre :nvarchar]
                        [:comment :nvarchar]
                        [:composer :nvarchar]
                        [:original_artist :nvarchar]
                        [:remixer :nvarchar]
                        [:conductor :nvarchar]
                        [:bmp :integer]
                        [:grouping :nvarchar]
                        [:isrc :nvarchar]
                        [:record_label :nvarchar]
                        [:encoder :nvarchar]
                        [:lyricist :nvarchar]
                        [:lyrics :nclob]
                        [:artwork_mime :nvarchar]
                        [:artwork_data :blob]
                        [:bit_rate :integer]
                        [:channels :integer]
                        [:encoding_type :nvarchar]
                        [:format :nvarchar]
                        [:sample_rate :integer]
                        [:length :integer]
                        [:variable_bit_rate :boolean]
                        [:folder :nvarchar "NOT NULL"]
                        [:filename :nvarchar "NOT NULL"]
                        [:mod_date :long "NOT NULL"]))

;(def downloads 
;  (jdb/create-table-ddl :downloads
;                        [:file "VARCHAR(1024) PRIMARY KEY"]
;                        [:name "VARCHAR(1024)"]
;                        [:created :timestamp]
;                        [:filetypes "VARCHAR(1024)"]
;                        [:bitrates "VARCHAR(1024)"]))
;
;(def songs-downloads 
;  (jdb/create-table-ddl :songs_downloads
;                        [:song_file (str "VARCHAR(1024) REFERENCES songs(file)"
;                                         " ON DELETE CASCADE")]
;                        [:download_file 
;                         (str "VARCHAR(1024) REFERENCES downloads(file)"
;                              " ON DELETE CASCADE")]))



(def folder-idx "CREATE INDEX songs_folder ON songs (folder)")

(defn create-db!  
  "Creates database and all tables and indexes using db-spec"
  [db-spec]
  (try
    (jdb/db-do-commands db-spec folders songs folder-idx)
    (catch Exception ex
      (log/error ex "Error creating database")
      #_(System/exit (:db-failure exit-codes)))))

(defn drop-db! 
  "Deletes database file if it exists. Returns true if database deleted. Nil if
  database didn't exist."
  [db-spec]
  (try 
    (jdb/db-do-commands db-spec "DROP ALL OBJECTS DELETE FILES")
    (catch Exception ex
      (log/error ex "Error creating database")
      #_(System/exit (:db-failure exit-codes)))))

(defn recreate-db! 
  "Deletes database and then recreates all tables and indexes without data."
  [db-spec]
  (drop-db! db-spec)
  (create-db! db-spec))

(defn get-db!
  "Get db-spec and create db if it doesn't already exist."
  [& db-spec]
  (let [db-spec (if (empty? db-spec)  (get-db-spec) (first db-spec))]
    (when-not (.exists (io/as-file (str (:subname (get-db-spec)) ".mv.db"))) 
      (create-db! db-spec))
    db-spec))

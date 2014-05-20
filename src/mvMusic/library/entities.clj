(ns mvMusic.library.entities
  (:use [korma db core])
  (:require [mvMusic.library.schema :as sch]
            [clojure.string :as st]))

(declare songs albums downloads folders)

(defn- format-key
  [s]
  (st/lower-case (st/replace s "_" "-")))

(defn- format-column
  [s]
  (st/upper-case (st/replace s "-" "_")))

(defdb db (assoc 
            sch/db
            :naming
            {:keys format-key :fields format-column}))

(defentity songs
  (table :songs)
  (pk :file)
  (belongs-to folders {:fk :folder})
  (many-to-many downloads :songs-downloads {:lfk :song-file}))

(defentity downloads
  (table :DOWNLOADS)
  (pk :file)
  (many-to-many songs :songs-downloads {:lfk :download-file}))

(defentity folders
  (table :folders)
  (pk :folder)
  (has-many songs {:fk :folder}))

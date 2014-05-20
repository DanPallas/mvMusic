(ns mvMusic.library.access
  (:require [korma.core :as sql]
            [mvMusic.library.entities :refer [songs downloads folders]]))

(defn- store 
  "Inserts vls into table ent. No duplicate checks(created for easier testing)"
  [ent vls] 
  (sql/insert ent (sql/values vls)))

(defn fetch-folders
  [& x]
  "Retreive folders from database. Keys and values can optionally 
  be used as arguments to filter results."
  (if x
    (sql/select folders
            (sql/where (apply hash-map x)))
    (sql/select folders)))

(defn fetch-songs
  [& x]
  "Retreive folders from database. Keys and values can optionally 
  be used as arguments to filter results."
  (if x
    (sql/select songs
            (sql/where (apply hash-map x)))
    (sql/select songs)))

;TODO: fetch artists albums 
;TODO: store songs folders downloads 

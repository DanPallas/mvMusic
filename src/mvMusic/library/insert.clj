(ns mvMusic.library.insert
  (:use [mvMusic.library util])
  (:require [mvMusic.library.entities :as e]
            [mvMusic.library.fetch :as fetch]
            [clojure.set :as sets]
            [korma.core :as kc]))

(declare album-art)

(defn insert 
  "Inserts vls into table ent. (created for easier testing)"
  [ent vls] 
  (kc/insert ent (kc/values vls)))

(defn albums
  "Inserts x which is either a map of an album, or a collection of album maps
  into the library if they aren't already present."
  [x]
  (let
    [to-add (if (map? x) (vector x) x)
     existing (fetch/albums)
     to-add (remove nil? (for [i to-add]
                           (if (some #(albums=? i %) existing) nil i)))]
    (if (empty? to-add) nil (insert e/albums to-add))))

(defn songs
  "Inserts songs into the library including albums if not present in library 
  and album art. songs is either a map representation of a song including album
  information, or a collection of songs"
  [sngs]
  (let [sngs (if (map? sngs) (vector sngs) sngs) ]
    (albums (extract-albums sngs))
    (let [sngs (prep-songs sngs (fetch/albums)) ]
      (album-art sngs)
      (insert e/songs sngs))))

(defn folders
  [f & r]
  "Iserts one or more folders into the library. Checks for and will not insert
  duplicates."
  (let
    [x (sets/difference 
         (apply hash-set (conj (or r '()) f))
         (apply hash-set (fetch/folders)))]
    (when (> (count x) 0) 
      (insert e/folders x))))

(ns mvMusic.library.fetch
  (:use [korma core] 
        [mvMusic.library util])
  (:require [mvMusic.library.entities :as e]))

(defn albums
  [& x]
  "Retreive albums from database. Keys and values can optionally 
  be used as arguments to filter results."
  (if x
    (select e/albums
            (where (apply hash-map x)))
    (select e/albums)))

(defn folders
  [& x]
  "Retreive folders from database. Keys and values can optionally 
  be used as arguments to filter results."
  (if x
    (select e/folders
            (where (apply hash-map x)))
    (select e/folders)))



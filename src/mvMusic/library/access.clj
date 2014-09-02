(ns mvMusic.library.access
  (:require [mvMusic.log :as log]
            [clojure.java.io :refer [as-file]]
            [clojure.string :as s]
            [clojure.java.jdbc :as j]))

(defmacro debug^:private [label i]
  `(let [o# ~i
         _# (println (str "Debug " ~label ": " o#))]
     o#))

(defn- key->column
  [k]
  (keyword (s/replace (s/upper-case (name k)) #"-" "_")))

(defn- keys->columns
  [m]
  (reduce 
    (fn [m [k v]] (assoc m (key->column k) v))
    {}
    (seq m)))

(defn- column->key
  [k]
  (keyword (s/replace (s/lower-case (name k)) #"_" "-")))

(defn- assoc-file-folder
  [song]
  (let [file (as-file (as-file (:file song)))]
    (assoc song
           :folder (.getParent file)
           :filename (.getName file))))

(defn- remove-articles
  [string]
  (if (nil? string) 
    nil
    (apply str (interpose
                 " "
                 (remove #(#{"the" "a" "an"} (s/lower-case %)) 
                         (s/split string #"\ "))))))
(defn- assoc-sort
  [song]
  (assoc song 
         :artist-sort (remove-articles (:artist song))
         :album-sort (remove-articles (:album song))))
(defn- safe-insert!
  [db tbl-key]
  (fn
    [m]
    (try
      (apply vals (j/insert! db tbl-key m))
      (catch Exception e
        (log/error (str "failed to insert " m " into " (name tbl-key)) e)
        (str e)))))

(defn add-songs!
  "adds a song into the library"
  [db songs]
  (->>
    (map (comp assoc-sort assoc-file-folder) songs)
    (map keys->columns)
    (map (safe-insert! db :SONGS))
    (reduce 
      (fn [col entry] 
        (conj col (if (string? entry) entry true))) 
      [])))

(defn add-folders!
  "adds a folder into the library"
  [db folders]
  (->>
    (map keys->columns folders)
    (map (safe-insert! db :FOLDERS))
    (reduce #(conj %1 (if (nil? %2) true %2)) [])))
(defn get-songs
  [db & opts]
  (j/query db "SELECT * FROM SONGS" :identifiers column->key))
